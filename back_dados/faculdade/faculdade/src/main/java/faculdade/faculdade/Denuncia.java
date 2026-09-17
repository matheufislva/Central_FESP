package faculdade.faculdade;


public class Denuncia {
	private int id;
	private String protocolo;
	private String titulo;
	private String descricao;
	private String dataRegistro; 
	private String gravidade; 
	private String status; 
	private int idUsuario;

	public Denuncia(int id, String protocolo, String titulo, String descricao, String dataRegistro, String gravidade,
			String status, int idUsuario) {
		this.id = id;
		this.protocolo = protocolo;
		this.titulo = titulo;
		this.descricao = descricao;
		this.dataRegistro = dataRegistro;
		this.gravidade = gravidade;
		this.status = status;
		this.idUsuario = idUsuario;
	}

	public Denuncia(String protocolo, String titulo, String descricao, String dataRegistro, String gravidade,
			String status, int idUsuario) {
		this.protocolo = protocolo;
		this.titulo = titulo;
		this.descricao = descricao;
		this.dataRegistro = dataRegistro;
		this.gravidade = gravidade;
		this.status = status;
		this.idUsuario = idUsuario;
	}

	public boolean isAnonima() {
		return idUsuario <= 0;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProtocolo() {
		return protocolo;
	}

	public void setProtocolo(String protocolo) {
		this.protocolo = protocolo;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getDataRegistro() {
		return dataRegistro;
	}

	public void setDataRegistro(String dataRegistro) {
		this.dataRegistro = dataRegistro;
	}

	public String getGravidade() {
		return gravidade;
	}

	public void setGravidade(String gravidade) {
		this.gravidade = gravidade;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public void exibirDados() {
		System.out.println("ID: " + id + " | Protocolo: " + protocolo);
		System.out.println("Titulo: " + titulo);
		System.out.println("Descricao: " + descricao);
		System.out.println("Data: " + dataRegistro + " | Gravidade: " + gravidade + " | Status: " + status);
		System.out.println("Registrada por: " + (isAnonima() ? "ANONIMA" : "usuario " + idUsuario));
		System.out.println("-------------------------");
	}
}
