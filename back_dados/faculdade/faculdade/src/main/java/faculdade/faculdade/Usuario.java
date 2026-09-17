package faculdade.faculdade;

public class Usuario {
	private int id;
	private String nome;
	private String email;
	private String senha;
	private int idPerfil;
	private int idSetor;

	public Usuario(int id, String nome, String email, String senha, int idPerfil, int idSetor) {
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.senha = senha;
		this.idPerfil = idPerfil;
		this.idSetor = idSetor;
	}

	public Usuario(String nome, String email, String senha, int idPerfil, int idSetor) {
		this.nome = nome;
		this.email = email;
		this.senha = senha;
		this.idPerfil = idPerfil;
		this.idSetor = idSetor;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public int getIdPerfil() {
		return idPerfil;
	}

	public void setIdPerfil(int idPerfil) {
		this.idPerfil = idPerfil;
	}

	public int getIdSetor() {
		return idSetor;
	}

	public void setIdSetor(int idSetor) {
		this.idSetor = idSetor;
	}

	// exibe os dados do usuario no console (senha sempre mascarada)
	public void exibirDados() {
		System.out.println("ID: " + id);
		System.out.println("Nome: " + nome);
		System.out.println("E-mail: " + email);
		System.out.println("Senha: ********");
		System.out.println("ID do Perfil: " + idPerfil);
		System.out.println("ID do Setor: " + idSetor);
		System.out.println("-------------------------");
	}
}
