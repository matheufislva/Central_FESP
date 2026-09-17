package faculdade.faculdade;

public class DashboardMetrics {
	private int totalAgendamentos;
	private double taxaOcupacaoSalas;
	private int denunciasPendentes;
	private int totalDenuncias;

	public DashboardMetrics(int totalAgendamentos, double taxaOcupacaoSalas, int denunciasPendentes,
			int totalDenuncias) {
		this.totalAgendamentos = totalAgendamentos;
		this.taxaOcupacaoSalas = taxaOcupacaoSalas;
		this.denunciasPendentes = denunciasPendentes;
		this.totalDenuncias = totalDenuncias;
	}

	public int getTotalAgendamentos() {
		return totalAgendamentos;
	}

	public double getTaxaOcupacaoSalas() {
		return taxaOcupacaoSalas;
	}

	public int getDenunciasPendentes() {
		return denunciasPendentes;
	}

	public int getTotalDenuncias() {
		return totalDenuncias;
	}

	@Override
	public String toString() {
		return String.format("=== DASHBOARD - CENTRAL DE RELACIONAMENTO ===%n"
				+ "- Agendamentos no periodo: %d%n"
				+ "- Ocupacao de salas: %.1f%%%n"
				+ "- Denuncias pendentes: %d%n"
				+ "- Total de denuncias: %d%n"
				+ "=============================================",
				totalAgendamentos, taxaOcupacaoSalas, denunciasPendentes, totalDenuncias);
	}
}
