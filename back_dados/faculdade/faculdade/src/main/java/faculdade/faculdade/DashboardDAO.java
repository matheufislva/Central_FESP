package faculdade.faculdade;

import java.sql.*;
import java.util.*;

public class DashboardDAO {
	private final String url;

	public DashboardDAO(String url) {
		this.url = url;
	}

	
	public void init() throws SQLException {
		String sql = """
				    CREATE TABLE IF NOT EXISTS dashboard_metrics (
				        id INTEGER PRIMARY KEY AUTOINCREMENT,
				        total_agendamentos INTEGER NOT NULL,
				        taxa_ocupacao REAL NOT NULL,
				        denuncias_pendentes INTEGER NOT NULL,
				        total_denuncias INTEGER NOT NULL,
				        data_registro DATETIME DEFAULT CURRENT_TIMESTAMP
				    )
				""";

		try (Connection c = DriverManager.getConnection(url); Statement st = c.createStatement()) {
			st.execute(sql);
		}
	}

	
	public DashboardMetrics calcular(String dataInicio, String dataFim) throws SQLException {
		int totalAgendamentos = 0;
		int salasOcupadas = 0;
		int totalSalas = 0;
		int denunciasPendentes = 0;
		int totalDenuncias = 0;

		try (Connection c = DriverManager.getConnection(url)) {

			String sqlAgenda = "SELECT COUNT(*) FROM agendamento WHERE data BETWEEN ? AND ?";
			try (PreparedStatement ps = c.prepareStatement(sqlAgenda)) {
				ps.setString(1, dataInicio);
				ps.setString(2, dataFim);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						totalAgendamentos = rs.getInt(1);
					}
				}
			}

			String sqlOcupadas = "SELECT COUNT(DISTINCT id_sala) FROM agendamento WHERE data BETWEEN ? AND ?";
			try (PreparedStatement ps = c.prepareStatement(sqlOcupadas)) {
				ps.setString(1, dataInicio);
				ps.setString(2, dataFim);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						salasOcupadas = rs.getInt(1);
					}
				}
			}

			try (Statement st = c.createStatement(); ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM sala")) {
				if (rs.next()) {
					totalSalas = rs.getInt(1);
				}
			}

			try (Statement st = c.createStatement();
					ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM denuncia WHERE status = 'PENDENTE'")) {
				if (rs.next()) {
					denunciasPendentes = rs.getInt(1);
				}
			}

			try (Statement st = c.createStatement(); ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM denuncia")) {
				if (rs.next()) {
					totalDenuncias = rs.getInt(1);
				}
			}
		}

		
		double taxaOcupacao = totalSalas == 0 ? 0.0 : (salasOcupadas * 100.0) / totalSalas;

		return new DashboardMetrics(totalAgendamentos, taxaOcupacao, denunciasPendentes, totalDenuncias);
	}

	public int create(DashboardMetrics m) throws SQLException {
		String sql = "INSERT INTO dashboard_metrics(total_agendamentos, taxa_ocupacao, denuncias_pendentes, total_denuncias) VALUES(?, ?, ?, ?)";
		try (Connection c = DriverManager.getConnection(url);
				PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			ps.setInt(1, m.getTotalAgendamentos());
			ps.setDouble(2, m.getTaxaOcupacaoSalas());
			ps.setInt(3, m.getDenunciasPendentes());
			ps.setInt(4, m.getTotalDenuncias());
			ps.executeUpdate();

			try (ResultSet rs = ps.getGeneratedKeys()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
		}
		return -1;
	}

	public Optional<DashboardMetrics> getUltimasMetricas() throws SQLException {
		String sql = "SELECT total_agendamentos, taxa_ocupacao, denuncias_pendentes, total_denuncias FROM dashboard_metrics ORDER BY id DESC LIMIT 1";
		try (Connection c = DriverManager.getConnection(url);
				PreparedStatement ps = c.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			if (rs.next()) {
				return Optional.of(new DashboardMetrics(rs.getInt("total_agendamentos"), rs.getDouble("taxa_ocupacao"),
						rs.getInt("denuncias_pendentes"), rs.getInt("total_denuncias")));
			}
		}
		return Optional.empty();
	}

	public List<DashboardMetrics> listAll() throws SQLException {
		String sql = "SELECT total_agendamentos, taxa_ocupacao, denuncias_pendentes, total_denuncias FROM dashboard_metrics ORDER BY id DESC";
		List<DashboardMetrics> out = new ArrayList<>();

		try (Connection c = DriverManager.getConnection(url);
				PreparedStatement ps = c.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				out.add(new DashboardMetrics(rs.getInt("total_agendamentos"), rs.getDouble("taxa_ocupacao"),
						rs.getInt("denuncias_pendentes"), rs.getInt("total_denuncias")));
			}
		}
		return out;
	}

	public boolean delete(int id) throws SQLException {
		String sql = "DELETE FROM dashboard_metrics WHERE id = ?";
		try (Connection c = DriverManager.getConnection(url); PreparedStatement ps = c.prepareStatement(sql)) {

			ps.setInt(1, id);
			return ps.executeUpdate() > 0;
		}
	}
}
