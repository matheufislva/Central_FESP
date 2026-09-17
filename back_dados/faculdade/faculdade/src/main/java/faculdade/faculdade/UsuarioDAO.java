package faculdade.faculdade;

import java.sql.*;
import java.util.*;

public class UsuarioDAO {
	private final String url;

	public UsuarioDAO(String url) {
		this.url = url;
	}

	
	public void init() throws SQLException {
		String sql = """
				    CREATE TABLE IF NOT EXISTS usuario (
				        id_usuario INTEGER PRIMARY KEY AUTOINCREMENT,
				        nome VARCHAR(150) NOT NULL,
				        email VARCHAR(150) NOT NULL UNIQUE,
				        senha VARCHAR(255) NOT NULL,
				        id_perfil INTEGER,
				        id_setor INTEGER
				    )
				""";

		try (Connection c = DriverManager.getConnection(url); Statement st = c.createStatement()) {
			st.execute(sql);
		}
	}

	public int create(Usuario u) throws SQLException {
		String sql = "INSERT INTO usuario(nome, email, senha, id_perfil, id_setor) VALUES(?, ?, ?, ?, ?)";
		try (Connection c = DriverManager.getConnection(url);
				PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			ps.setString(1, u.getNome());
			ps.setString(2, u.getEmail());
			ps.setString(3, u.getSenha());
			ps.setInt(4, u.getIdPerfil());
			ps.setInt(5, u.getIdSetor());
			ps.executeUpdate();

			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next()) {
					int id = rs.getInt(1);
					u.setId(id);
					return id;
				}
			}
		}
		return -1;
	}

	public Optional<Usuario> getById(int id) throws SQLException {
		String sql = "SELECT id_usuario, nome, email, senha, id_perfil, id_setor FROM usuario WHERE id_usuario = ?";
		try (Connection c = DriverManager.getConnection(url); PreparedStatement ps = c.prepareStatement(sql)) {

			ps.setInt(1, id);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return Optional.of(montar(rs));
				}
				return Optional.empty();
			}
		}
	}

	public List<Usuario> listAll() throws SQLException {
		String sql = "SELECT id_usuario, nome, email, senha, id_perfil, id_setor FROM usuario ORDER BY id_usuario";
		List<Usuario> out = new ArrayList<>();

		try (Connection c = DriverManager.getConnection(url);
				PreparedStatement ps = c.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				out.add(montar(rs));
			}
		}
		return out;
	}

	public boolean update(Usuario u) throws SQLException {
		String sql = "UPDATE usuario SET nome = ?, email = ?, senha = ?, id_perfil = ?, id_setor = ? WHERE id_usuario = ?";
		try (Connection c = DriverManager.getConnection(url); PreparedStatement ps = c.prepareStatement(sql)) {

			ps.setString(1, u.getNome());
			ps.setString(2, u.getEmail());
			ps.setString(3, u.getSenha());
			ps.setInt(4, u.getIdPerfil());
			ps.setInt(5, u.getIdSetor());
			ps.setInt(6, u.getId());

			return ps.executeUpdate() > 0;
		}
	}

	public boolean delete(int id) throws SQLException {
		String sql = "DELETE FROM usuario WHERE id_usuario = ?";
		try (Connection c = DriverManager.getConnection(url); PreparedStatement ps = c.prepareStatement(sql)) {

			ps.setInt(1, id);
			return ps.executeUpdate() > 0;
		}
	}

	
	private Usuario montar(ResultSet rs) throws SQLException {
		return new Usuario(rs.getInt("id_usuario"), rs.getString("nome"), rs.getString("email"), rs.getString("senha"),
				rs.getInt("id_perfil"), rs.getInt("id_setor"));
	}
}
