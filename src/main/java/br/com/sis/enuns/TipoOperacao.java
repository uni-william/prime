package br.com.sis.enuns;

public enum TipoOperacao {

	COMPRA("Compra"),
	VENDA("Venda"),
	INTERMEDIACAO("Intermediacao");
	
	TipoOperacao(String descricao) {
		this.descricao = descricao;
	}

	private String descricao;

	public String getDescricao() {
		return descricao;
	}

}
