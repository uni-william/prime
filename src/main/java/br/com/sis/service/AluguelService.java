package br.com.sis.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.sis.entity.Aluguel;
import br.com.sis.entity.Renovacao;
import br.com.sis.enuns.StatusAluguel;
import br.com.sis.enuns.StatusVeiculo;
import br.com.sis.repository.AluguelRepository;
import br.com.sis.repository.RenovacaoRepository;
import br.com.sis.util.jpa.Transactional;

public class AluguelService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private AluguelRepository aluguelRepository;
	
	@Inject
	private RenovacaoRepository renovacaoRepository;
	
	
	@Transactional
	public Aluguel realizarAluguel(Aluguel aluguel) {
		aluguel.getVeiculo().setStatusVeiculo(StatusVeiculo.ALUGADO);
		aluguel.getVeiculo().setQuilometragem(aluguel.getKmInicial());
		return aluguelRepository.salvar(aluguel);
		
	}
	
	@Transactional
	public Aluguel realizarRenovacao(Aluguel aluguel) {
		Aluguel aluguelAnt = aluguelRepository.porId(aluguel.getId());
		Renovacao renovacao = new Renovacao();
		renovacao.setAluguel(aluguel);
		renovacao.setDataInicioAnterior(aluguelAnt.getDataInicio());
		renovacao.setDataPrevistaAnterior(aluguelAnt.getDataPrevista());
		renovacaoRepository.salvar(renovacao);
		aluguel.getVeiculo().setStatusVeiculo(StatusVeiculo.ALUGADO);
		aluguel.getVeiculo().setQuilometragem(aluguel.getKmInicial());
		return aluguelRepository.salvar(aluguel);
		
	}	
	
	@Transactional
	public Aluguel realizarEntregaVeiculo(Aluguel aluguel) {
		aluguel.getVeiculo().setStatusVeiculo(StatusVeiculo.PARA_ALUGUEL);
		aluguel.setStatusAluguel(StatusAluguel.FINALIZADO);
		return aluguelRepository.salvar(aluguel);
	}

	@Transactional
	public Aluguel cancelarAluguel(Aluguel aluguel) {
		aluguel.getVeiculo().setStatusVeiculo(StatusVeiculo.PARA_ALUGUEL);
		aluguel.setStatusAluguel(StatusAluguel.CANCELADO);
		return aluguelRepository.salvar(aluguel);
	}
	
}
