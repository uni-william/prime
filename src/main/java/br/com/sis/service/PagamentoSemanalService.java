package br.com.sis.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.sis.entity.PagamentoSemanal;
import br.com.sis.repository.PagamentoSemanalRepository;
import br.com.sis.util.jpa.Transactional;

public class PagamentoSemanalService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private PagamentoSemanalRepository pagamentoSemanalRepository;
	
	@Transactional
	public PagamentoSemanal salvar(PagamentoSemanal pagamentoSemanal) {
		return pagamentoSemanalRepository.salvar(pagamentoSemanal);
	}

}
