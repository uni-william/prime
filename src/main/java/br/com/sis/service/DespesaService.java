package br.com.sis.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.sis.entity.Despesa;
import br.com.sis.repository.DespesaRepository;
import br.com.sis.util.jpa.Transactional;

public class DespesaService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private DespesaRepository despesaRepository;

	@Transactional
	public Despesa salvar(Despesa despesa) {
		return despesaRepository.salvar(despesa);
	}

}
