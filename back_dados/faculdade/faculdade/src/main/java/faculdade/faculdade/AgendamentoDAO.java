package faculdade.faculdade;

import java.sql.*;
import java.util.*;

public class AgendamentoDAO {
	private final String url;

	public AgendamentoDAO(String url) {
		this.url = url;
	}

	
	public void init() throws SQLException {
		String sql = """
				    CREATE TABLE IF NOT EXISTS agendamento (
				        id_agendamento INTEGER PRIMARY KEY AUTOINCREMENT,
				        data DATE NOT NULL,
				        hora_inicio TIME NOT NULL,
				        hora_final TIME NOT NULL,
				        id_sala INTEGER NOT NULL,
				        id_usuario_responsavel INTEGER NOT NULL,
				        finalidade TEXT,
				        CHECK (hora_final > hora_inicio),
				        FOREIGN KEY (id_sala) REFERENCES sala(id_sala),
				        FOREIGN KEY (id_usuario_responsavel) REFERENCES usuario(id_usuario)
				    )
				""";

		try (Connection c = DriverManager.getConnection(url); Statement st = c.createStatement()) {
			st.execute(sql);
		}
	}

	public int create(Agendamento a) throws SQLException {
		
		if (existeConflito(a.getData(), a.getHoraInicio(), a.getHoraFinal(), a.getIdSala())) {
			throw new IllegalStateException("Sala indisponivel: ja existe reserva em " + a.getData() + " entre "
					+ a.getHoraInicio() + " e " + a.getHoraFinal() + ".");
		}

		String sql = "INSERT INTO agendamento(data, hora_inicio, hora_final, id_sala, id_usuario_responsavel, finalidade) VALUES(?, ?, ?, ?, ?, ?)";
		try (Connection c = DriverManager.getConnection(url);
				PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			ps.setString(1, a.getData());
			ps.setString(2, a.getHoraInicio());
			ps.setString(3, a.getHoraFinal());
			ps.setInt(4, a.getIdSala());
			ps.setInt(5, a.getIdUsuarioResponsavel());
			ps.setString(6, a.getFinalidade());
			ps.executeUpdate();

			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next()) {
					int id = rs.getInt(1);
					a.setId(id);
					return id;
				}
			}
		}
		return -1;
	}

	public Optional<Agendamento> getById(int id) throws SQLException {
		String sql = "SELECT id_agendamento, data, hora_inicio, hora_final, id_sala, id_usuario_responsavel, finalidade FROM agendamento WHERE id_agendamento = ?";
		try (Connection c = DriverManager.getConnection(url); PreparedStatement ps = c.prepareStatement(sql)) {

			ps.setInt(1, id);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return Optional.of(new Agendamento(rs.getInt("id_agendamento"), rs.getString("data"),
							rs.getString("hora_inicio"), rs.getString("hora_final"), rs.getInt("id_sala"),
							rs.getInt("id_usuario_responsavel"), rs.getString("finalidade")));
				}
				return Optional.empty();
			}
		}
	}

	public List<Agendamento> listAll() throws SQLException {
		String sql = "SELECT id_agendamento, data, hora_inicio, hora_final, id_sala, id_usuario_responsavel, finalidade FROM agendamento ORDER BY data, hora_inicio";
		List<Agendamento> out = new ArrayList<>();

		try (Connection c = DriverManager.getConnection(url);
				PreparedStatement ps = c.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				out.add(new Agendamento(rs.getInt("id_agendamento"), rs.getString("data"), rs.getString("hora_inicio"),
						rs.getString("hora_final"), rs.getInt("id_sala"), rs.getInt("id_usuario_responsavel"),
						rs.getString("finalidade")));
			}
		}
		return out;
	}

	
	public List<Agendamento> listByData(String data) throws SQLException {
		String sql = "SELECT id_agendamento, data, hora_inicio, hora_final, id_sala, id_usuario_responsavel, finalidade FROM agendamento WHERE data = ? ORDER BY hora_inicio";
		List<Agendamento> out = new ArrayList<>();

		try (Connection c = DriverManager.getConnection(url); PreparedStatement ps = c.prepareStatement(sql)) {

			ps.setString(1, data);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					out.add(new Agendamento(rs.getInt("id_agendamento"), rs.getString("data"),
							rs.getString("hora_inicio"), rs.getString("hora_final"), rs.getInt("id_sala"),
							rs.getInt("id_usuario_responsavel"), rs.getString("finalidade")));
				}
			}
		}
		return out;
	}


	public List<String> listAllDetalhado() throws SQLException {
		String sql = """
				    SELECT a.id_agendamento, a.data, a.hora_inicio, a.hora_final, a.finalidade,
				           s.nome_sala, s.capacidade, u.nome AS responsavel
				    FROM agendamento a
				    INNER JOIN sala s ON s.id_sala = a.id_sala
				    INNER JOIN usuario u ON u.id_usuario = a.id_usuario_responsavel
				    ORDER BY a.data, a.hora_inicio
				""";
		List<String> out = new ArrayList<>();

		try (Connection c = DriverManager.getConnection(url);
				PreparedStatement ps = c.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				out.add(rs.getInt("id_agendamento") + " - " + rs.getString("data") + " - " + rs.getString("hora_inicio")
						+ " as " + rs.getString("hora_final") + " - Sala: " + rs.getString("nome_sala") + " (cap. "
						+ rs.getInt("capacidade") + ") - Responsavel: " + rs.getString("responsavel") + " - "
						+ rs.getString("finalidade"));
			}
		}
		return out;
	}

	
	public boolean existeConflito(String data, String horaInicio, String horaFinal, int idSala) throws SQLException {
		String sql = "SELECT COUNT(*) FROM agendamento WHERE id_sala = ? AND data = ? AND hora_inicio < ? AND hora_final > ?";
		try (Connection c = DriverManager.getConnection(url); PreparedStatement ps = c.prepareStatement(sql)) {

			ps.setInt(1, idSala);
			ps.setString(2, data);
			ps.setString(3, horaFinal);
			ps.setString(4, horaInicio);

			try (ResultSet rs = ps.executeQuery()) {
				return rs.next() && rs.getInt(1) > 0;
			}
		}
	}

	public boolean update(Agendamento a) throws SQLException {
		String sql = "UPDATE agendamento SET data = ?, hora_inicio = ?, hora_final = ?, id_sala = ?, id_usuario_responsavel = ?, finalidade = ? WHERE id_agendamento = ?";
		try (Connection c = DriverManager.getConnection(url); PreparedStatement ps = c.prepareStatement(sql)) {

			ps.setString(1, a.getData());
			ps.setString(2, a.getHoraInicio());
			ps.setString(3, a.getHoraFinal());
			ps.setInt(4, a.getIdSala());
			ps.setInt(5, a.getIdUsuarioResponsavel());
			ps.setString(6, a.getFinalidade());
			ps.setInt(7, a.getId());

			return ps.executeUpdate() > 0;
		}
	}

	public boolean delete(int id) throws SQLException {
		String sql = "DELETE FROM agendamento WHERE id_agendamento = ?";
		try (Connection c = DriverManager.getConnection(url); PreparedStatement ps = c.prepareStatement(sql)) {

			ps.setInt(1, id);
			return ps.executeUpdate() > 0;
		}
	}
}
