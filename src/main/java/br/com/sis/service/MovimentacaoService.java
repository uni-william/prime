package br.com.sis.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.sis.entity.Movimentacao;
import br.com.sis.entity.Veiculo;
import br.com.sis.enuns.StatusVeiculo;
import br.com.sis.enuns.StatusVenda;
import br.com.sis.repository.MovimentacaoRepository;
import br.com.sis.repository.VeiculoRepository;
import br.com.sis.util.jpa.Transactional;

public class MovimentacaoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private MovimentacaoRepository movimentacaoRepository;
	
	@Inject
	private VeiculoRepository veiculoRepository;

	@Transactional
	public Movimentacao efetuarCompra(Movimentacao movimentacao) {
		movimentacao.getVeiculo().setValorCompra(movimentacao.getValor());
		movimentacao.getVeiculo().setStatusVeiculo(StatusVeiculo.ADQUIRIDO);
		movimentacao.setStatusVenda(StatusVenda.CONCLUIDA);
		return movimentacaoRepository.salvar(movimentacao);
	}
	
	@Transactional
	public Movimentacao efetuarVenda(Movimentacao movimentacao) {
		Veiculo v = veiculoRepository.porId(movimentacao.getVeiculo().getId());
		v.setValorVenda(movimentacao.getValor());
		v.setStatusVeiculo(StatusVeiculo.VENDIDO);
		v = veiculoRepository.salvar(v);
		movimentacao.setVeiculo(v);
		movimentacao.setStatusVenda(StatusVenda.CONCLUIDA);
		return movimentacaoRepository.salvar(movimentacao);
	}
	
	@Transactional
	public Movimentacao cancelarVenda(Movimentacao movimentacao) {
		Veiculo v = veiculoRepository.porId(movimentacao.getVeiculo().getId());
		v.setStatusVeiculo(StatusVeiculo.PATIO);
		v = veiculoRepository.salvar(v);
		movimentacao.setVeiculo(v);
		movimentacao.setStatusVenda(StatusVenda.CANCELADA);
		return movimentacaoRepository.salvar(movimentacao);
	}
	
	@Transactional
	public Movimentacao salvar(Movimentacao movimentacao) {
		return movimentacaoRepository.salvar(movimentacao);
	}
	

}
