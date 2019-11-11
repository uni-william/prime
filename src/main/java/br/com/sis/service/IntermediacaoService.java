package br.com.sis.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.sis.entity.Intermediacao;
import br.com.sis.entity.Veiculo;
import br.com.sis.enuns.StatusVeiculo;
import br.com.sis.enuns.StatusVenda;
import br.com.sis.repository.IntermediacaoRepository;
import br.com.sis.repository.VeiculoRepository;
import br.com.sis.util.jpa.Transactional;

public class IntermediacaoService implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Inject
	private IntermediacaoRepository intermediacoesRepository;
	
	@Inject
	private VeiculoRepository veiculoRepository;
	
	
	@Transactional
	public Intermediacao efetuarIntermediacao(Intermediacao intermediacao) {
		intermediacao.getVeiculo().setValorVenda(intermediacao.getValorFinanciado());
		intermediacao.getVeiculo().setStatusVeiculo(StatusVeiculo.VENDIDO);
		intermediacao.setStatusVenda(StatusVenda.CONCLUIDA);
		return intermediacoesRepository.salvar(intermediacao);
	}
	
	@Transactional
	public Intermediacao cancelarVenda(Intermediacao intermediacao) {
		Veiculo v = veiculoRepository.porId(intermediacao.getVeiculo().getId());
		v.setStatusVeiculo(StatusVeiculo.PATIO);
		v = veiculoRepository.salvar(v);
		intermediacao.setVeiculo(v);
		intermediacao.setStatusVenda(StatusVenda.CANCELADA);
		return intermediacoesRepository.salvar(intermediacao);
	}
	
	

}
