package br.com.sis.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.sis.entity.Manutencao;
import br.com.sis.repository.ManutencaoRepository;
import br.com.sis.util.jpa.Transactional;

public class ManutencaoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ManutencaoRepository ManutencaoRepository;

	@Transactional
	public Manutencao salvar(Manutencao Manutencao) {
		return ManutencaoRepository.salvar(Manutencao);
	}

}
