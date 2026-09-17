package faculdade.faculdade;


public class Agendamento {
	private int id;
	private String data; 
	private String horaInicio; 
	private String horaFinal; 
	private int idSala;
	private int idUsuarioResponsavel;
	private String finalidade;

	public Agendamento(int id, String data, String horaInicio, String horaFinal, int idSala, int idUsuarioResponsavel,
			String finalidade) {
		this.id = id;
		this.data = data;
		this.horaInicio = horaInicio;
		this.horaFinal = horaFinal;
		this.idSala = idSala;
		this.idUsuarioResponsavel = idUsuarioResponsavel;
		this.finalidade = finalidade;
	}

	public Agendamento(String data, String horaInicio, String horaFinal, int idSala, int idUsuarioResponsavel,
			String finalidade) {
		this.data = data;
		this.horaInicio = horaInicio;
		this.horaFinal = horaFinal;
		this.idSala = idSala;
		this.idUsuarioResponsavel = idUsuarioResponsavel;
		this.finalidade = finalidade;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(String horaInicio) {
		this.horaInicio = horaInicio;
	}

	public String getHoraFinal() {
		return horaFinal;
	}

	public void setHoraFinal(String horaFinal) {
		this.horaFinal = horaFinal;
	}

	public int getIdSala() {
		return idSala;
	}

	public void setIdSala(int idSala) {
		this.idSala = idSala;
	}

	public int getIdUsuarioResponsavel() {
		return idUsuarioResponsavel;
	}

	public void setIdUsuarioResponsavel(int idUsuarioResponsavel) {
		this.idUsuarioResponsavel = idUsuarioResponsavel;
	}

	public String getFinalidade() {
		return finalidade;
	}

	public void setFinalidade(String finalidade) {
		this.finalidade = finalidade;
	}

	public void exibirDados() {
		System.out.println("ID: " + id);
		System.out.println("Data: " + data + " das " + horaInicio + " as " + horaFinal);
		System.out.println("Sala: " + idSala + " | Responsavel: " + idUsuarioResponsavel);
		System.out.println("Finalidade: " + finalidade);
		System.out.println("-------------------------");
	}
}
