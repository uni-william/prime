package br.com.sis.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.sis.entity.FormaPagamento;
import br.com.sis.repository.FormaPagamentoRepository;
import br.com.sis.util.jpa.Transactional;
import br.com.sis.util.jsf.FacesUtil;

public class FormaPagamentoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private FormaPagamentoRepository formaPagamentoRepository;

	@Transactional
	public FormaPagamento salvar(FormaPagamento formaPagamento) {
		FormaPagamento formaPagamentoExistente = formaPagamentoRepository.porDescricao(formaPagamento.getDescricao());
		if (formaPagamentoExistente != null && !formaPagamentoExistente.equals(formaPagamento)) {
			FacesUtil.addErroMessage("Já existe uma forma de pagamento cadastrada com essa descrição.");
			return null;
		} else {
			return formaPagamentoRepository.salvar(formaPagamento);
		}

	}

}
