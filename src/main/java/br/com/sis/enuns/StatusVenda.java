package br.com.sis.enuns;

public enum StatusVenda {

	ANDAMENTO("Em andamento"),
	CONCLUIDA("Concluída"),
	CANCELADA("Cancelada");
	
	StatusVenda(String descricao) {
		this.descricao = descricao;
	}

	private String descricao;

	public String getDescricao() {
		return descricao;
	}

}
