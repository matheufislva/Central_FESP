package faculdade.faculdade;

public class Sala {
	private int id;
	private String situacao;
	private String nomeSala;
	private int capacidade;
	private String recursos;

	public Sala(int id, String situacao, String nomeSala, int capacidade, String recursos) {
		this.id = id;
		this.situacao = situacao;
		this.nomeSala = nomeSala;
		this.capacidade = capacidade;
		this.recursos = recursos;
	}

	public Sala(String situacao, String nomeSala, int capacidade, String recursos) {
		this.situacao = situacao;
		this.nomeSala = nomeSala;
		this.capacidade = capacidade;
		this.recursos = recursos;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getNomeSala() {
		return nomeSala;
	}

	public void setNomeSala(String nomeSala) {
		this.nomeSala = nomeSala;
	}

	public int getCapacidade() {
		return capacidade;
	}

	public void setCapacidade(int capacidade) {
		this.capacidade = capacidade;
	}

	public String getRecursos() {
		return recursos;
	}

	public void setRecursos(String recursos) {
		this.recursos = recursos;
	}
}
