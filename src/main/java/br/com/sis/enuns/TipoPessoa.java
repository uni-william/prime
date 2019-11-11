package br.com.sis.enuns;

public enum TipoPessoa {

	FISICA("Física"),
	JURIDICA("Jurídica");
	
	TipoPessoa(String descricao) {
		this.descricao = descricao;
	}

	private String descricao;

	public String getDescricao() {
		return descricao;
	}

}
