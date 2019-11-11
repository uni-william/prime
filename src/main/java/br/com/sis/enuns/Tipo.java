package br.com.sis.enuns;

public enum Tipo {

	FUNCIONARIO("Funcionário"),
	CLIENTE("Cliente"),
	FUNC_CLI("Cliente e Funcionário"),
	CLI_PARC("Cliente de Parceiro"),
	PARCEIRO("Parceiro");
	
	Tipo(String descricao) {
		this.descricao = descricao;
	}

	private String descricao;

	public String getDescricao() {
		return descricao;
	}

}
