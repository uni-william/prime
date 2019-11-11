package br.com.sis.enuns;

public enum TipoVeiculo {

	CARRO("Carro"),
	PICKUP("Utilitário"),
	MOTO("Moto");
	
	TipoVeiculo(String descricao) {
		this.descricao = descricao;
	}

	private String descricao;

	public String getDescricao() {
		return descricao;
	}

}
