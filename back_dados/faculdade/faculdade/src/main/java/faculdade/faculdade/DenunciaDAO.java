package faculdade.faculdade;

import java.sql.*;
import java.util.*;

public class DenunciaDAO {
	private final String url;

	public DenunciaDAO(String url) {
		this.url = url;
	}

	
	public void init() throws SQLException {
		String sql = """
				    CREATE TABLE IF NOT EXISTS denuncia (
				        id_denuncia INTEGER PRIMARY KEY AUTOINCREMENT,
				        protocolo VARCHAR(50) NOT NULL UNIQUE,
				        titulo VARCHAR(100) NOT NULL,
				        descricao TEXT NOT NULL,
				        data_registro DATE NOT NULL,
				        gravidade VARCHAR(50) NOT NULL
				            CHECK (gravidade IN ('BAIXA', 'MEDIA', 'ALTA')),
				        status VARCHAR(30) NOT NULL
				            CHECK (status IN ('PENDENTE', 'EM_ANDAMENTO', 'CONCLUIDA', 'CANCELADA')),
				        id_usuario INTEGER,
				        FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
				    )
				""";

		try (Connection c = DriverManager.getConnection(url); Statement st = c.createStatement()) {
			st.execute(sql);
		}
	}

	public int create(Denuncia d) throws SQLException {
		String sql = "INSERT INTO denuncia(protocolo, titulo, descricao, data_registro, gravidade, status, id_usuario) VALUES(?, ?, ?, ?, ?, ?, ?)";
		try (Connection c = DriverManager.getConnection(url);
				PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			ps.setString(1, d.getProtocolo());
			ps.setString(2, d.getTitulo());
			ps.setString(3, d.getDescricao());
			ps.setString(4, d.getDataRegistro());
			ps.setString(5, d.getGravidade());
			ps.setString(6, d.getStatus());

			// denuncia anonima e gravada com id_usuario nulo (canal confidencial)
			if (d.isAnonima()) {
				ps.setNull(7, Types.INTEGER);
			} else {
				ps.setInt(7, d.getIdUsuario());
			}

			ps.executeUpdate();

			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next()) {
					int id = rs.getInt(1);
					d.setId(id);
					return id;
				}
			}
		}
		return -1;
	}

	public Optional<Denuncia> getById(int id) throws SQLException {
		String sql = "SELECT id_denuncia, protocolo, titulo, descricao, data_registro, gravidade, status, id_usuario FROM denuncia WHERE id_denuncia = ?";
		try (Connection c = DriverManager.getConnection(url); PreparedStatement ps = c.prepareStatement(sql)) {

			ps.setInt(1, id);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return Optional.of(new Denuncia(rs.getInt("id_denuncia"), rs.getString("protocolo"),
							rs.getString("titulo"), rs.getString("descricao"), rs.getString("data_registro"),
							rs.getString("gravidade"), rs.getString("status"), rs.getInt("id_usuario")));
				}
				return Optional.empty();
			}
		}
	}

	public List<Denuncia> listAll() throws SQLException {
		String sql = "SELECT id_denuncia, protocolo, titulo, descricao, data_registro, gravidade, status, id_usuario FROM denuncia ORDER BY id_denuncia";
		List<Denuncia> out = new ArrayList<>();

		try (Connection c = DriverManager.getConnection(url);
				PreparedStatement ps = c.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				out.add(new Denuncia(rs.getInt("id_denuncia"), rs.getString("protocolo"), rs.getString("titulo"),
						rs.getString("descricao"), rs.getString("data_registro"), rs.getString("gravidade"),
						rs.getString("status"), rs.getInt("id_usuario")));
			}
		}
		return out;
	}

	
	public List<Denuncia> listByStatus(String status) throws SQLException {
		String sql = "SELECT id_denuncia, protocolo, titulo, descricao, data_registro, gravidade, status, id_usuario FROM denuncia WHERE status = ? ORDER BY id_denuncia";
		List<Denuncia> out = new ArrayList<>();

		try (Connection c = DriverManager.getConnection(url); PreparedStatement ps = c.prepareStatement(sql)) {

			ps.setString(1, status);

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					out.add(new Denuncia(rs.getInt("id_denuncia"), rs.getString("protocolo"), rs.getString("titulo"),
							rs.getString("descricao"), rs.getString("data_registro"), rs.getString("gravidade"),
							rs.getString("status"), rs.getInt("id_usuario")));
				}
			}
		}
		return out;
	}

	public boolean update(Denuncia d) throws SQLException {
		String sql = "UPDATE denuncia SET protocolo = ?, titulo = ?, descricao = ?, data_registro = ?, gravidade = ?, status = ?, id_usuario = ? WHERE id_denuncia = ?";
		try (Connection c = DriverManager.getConnection(url); PreparedStatement ps = c.prepareStatement(sql)) {

			ps.setString(1, d.getProtocolo());
			ps.setString(2, d.getTitulo());
			ps.setString(3, d.getDescricao());
			ps.setString(4, d.getDataRegistro());
			ps.setString(5, d.getGravidade());
			ps.setString(6, d.getStatus());

			if (d.isAnonima()) {
				ps.setNull(7, Types.INTEGER);
			} else {
				ps.setInt(7, d.getIdUsuario());
			}

			ps.setInt(8, d.getId());

			return ps.executeUpdate() > 0;
		}
	}

	public boolean delete(int id) throws SQLException {
		String sql = "DELETE FROM denuncia WHERE id_denuncia = ?";
		try (Connection c = DriverManager.getConnection(url); PreparedStatement ps = c.prepareStatement(sql)) {

			ps.setInt(1, id);
			return ps.executeUpdate() > 0;
		}
	}
}
