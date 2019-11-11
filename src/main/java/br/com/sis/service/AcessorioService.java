package br.com.sis.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.sis.entity.Acessorio;
import br.com.sis.repository.AcessorioRepository;
import br.com.sis.util.jpa.Transactional;
import br.com.sis.util.jsf.FacesUtil;

public class AcessorioService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private AcessorioRepository acessorioRepository;

	@Transactional
	public Acessorio salvar(Acessorio acessorio) {
		Acessorio acessorioExistente = acessorioRepository.porDescricao(acessorio.getDescricao());
		if (acessorioExistente != null && !acessorioExistente.equals(acessorio)) {
			FacesUtil.addErroMessage("Já existe um acessorio cadastrado com essa descrição.");
			return null;
		} else {
			return acessorioRepository.salvar(acessorio);
		}

	}

}
