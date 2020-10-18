package br.com.sis.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.sis.entity.TipoDespesa;
import br.com.sis.repository.TipoDespesaRepository;
import br.com.sis.util.jpa.Transactional;
import br.com.sis.util.jsf.FacesUtil;

public class TipoDespesaService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private TipoDespesaRepository tipoDespesaRepository;

	@Transactional
	public TipoDespesa salvar(TipoDespesa tipoDespesa) {
		TipoDespesa tipoDespesaExistente = tipoDespesaRepository.porDescricao(tipoDespesa.getDescricao());
		if (tipoDespesaExistente != null && !tipoDespesaExistente.equals(tipoDespesa)) {
			FacesUtil.addErroMessage("Já existe um tipo de despesa cadastrado com essa descrição.");
			return null;
		} else {
			return tipoDespesaRepository.salvar(tipoDespesa);
		}

	}

}
