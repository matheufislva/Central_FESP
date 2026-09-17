package faculdade.faculdade;

import java.sql.*;
import java.util.*;

public class SalaDAO {
	private final String url;

	public SalaDAO(String url) {
		this.url = url;
	}

	public void init() throws SQLException {
		String sql = """
				    CREATE TABLE IF NOT EXISTS sala (
				        id_sala INTEGER PRIMARY KEY AUTOINCREMENT,
				        situacao VARCHAR(50) NOT NULL
				            CHECK (situacao IN ('DISPONIVEL', 'EM_MANUTENCAO', 'INDISPONIVEL')),
				        nome_sala VARCHAR(100) NOT NULL UNIQUE,
				        capacidade INTEGER NOT NULL CHECK (capacidade > 0),
				        recursos TEXT
				    )
				""";

		try (Connection c = DriverManager.getConnection(url); Statement st = c.createStatement()) {
			st.execute(sql);
		}
	}

	public int create(Sala s) throws SQLException {
		String sql = "INSERT INTO sala(situacao, nome_sala, capacidade, recursos) VALUES(?, ?, ?, ?)";
		try (Connection c = DriverManager.getConnection(url);
				PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			ps.setString(1, s.getSituacao());
			ps.setString(2, s.getNomeSala());
			ps.setInt(3, s.getCapacidade());
			ps.setString(4, s.getRecursos());
			ps.executeUpdate();

			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next()) {
					int id = rs.getInt(1);
					s.setId(id);
					return id;
				}
			}
		}
		return -1;
	}

	public Optional<Sala> getById(int id) throws SQLException {
		String sql = "SELECT id_sala, situacao, nome_sala, capacidade, recursos FROM sala WHERE id_sala = ?";
		try (Connection c = DriverManager.getConnection(url); PreparedStatement ps = c.prepareStatement(sql)) {

			ps.setInt(1, id);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return Optional.of(new Sala(rs.getInt("id_sala"), rs.getString("situacao"),
							rs.getString("nome_sala"), rs.getInt("capacidade"), rs.getString("recursos")));
				}
				return Optional.empty();
			}
		}
	}

	public List<Sala> listAll() throws SQLException {
		String sql = "SELECT id_sala, situacao, nome_sala, capacidade, recursos FROM sala ORDER BY id_sala";
		List<Sala> out = new ArrayList<>();

		try (Connection c = DriverManager.getConnection(url);
				PreparedStatement ps = c.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				out.add(new Sala(rs.getInt("id_sala"), rs.getString("situacao"), rs.getString("nome_sala"),
						rs.getInt("capacidade"), rs.getString("recursos")));
			}
		}
		return out;
	}

	public boolean update(Sala s) throws SQLException {
		String sql = "UPDATE sala SET situacao = ?, nome_sala = ?, capacidade = ?, recursos = ? WHERE id_sala = ?";
		try (Connection c = DriverManager.getConnection(url); PreparedStatement ps = c.prepareStatement(sql)) {

			ps.setString(1, s.getSituacao());
			ps.setString(2, s.getNomeSala());
			ps.setInt(3, s.getCapacidade());
			ps.setString(4, s.getRecursos());
			ps.setInt(5, s.getId());

			return ps.executeUpdate() > 0;
		}
	}

	public boolean delete(int id) throws SQLException {
		String sql = "DELETE FROM sala WHERE id_sala = ?";
		try (Connection c = DriverManager.getConnection(url); PreparedStatement ps = c.prepareStatement(sql)) {

			ps.setInt(1, id);
			return ps.executeUpdate() > 0;
		}
	}
}
