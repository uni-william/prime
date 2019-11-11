package br.com.sis.enuns;

public enum StatusVeiculo {

	ADQUIRIDO("Adquirido"),
	MANUTENCAO("Em manutenção"),
	PATIO("No pátio"),
	VENDIDO("Vendido"),
	PARA_ALUGUEL("Para aluguel"),
	ALUGADO("Alugado");
	
	StatusVeiculo(String descricao) {
		this.descricao = descricao;
	}

	private String descricao;

	public String getDescricao() {
		return descricao;
	}

}
