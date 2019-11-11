package br.com.sis.enuns;

public enum StatusAluguel {

	ABERTO("Em  aberto"),
	FINALIZADO("Finalizado"),
	CANCELADO("Cancelado");
	
	StatusAluguel(String descricao) {
		this.descricao = descricao;
	}

	private String descricao;

	public String getDescricao() {
		return descricao;
	}

}
