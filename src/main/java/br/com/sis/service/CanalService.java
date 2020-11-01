package br.com.sis.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.sis.entity.Canal;
import br.com.sis.repository.CanalRepository;
import br.com.sis.util.jpa.Transactional;
import br.com.sis.util.jsf.FacesUtil;

public class CanalService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private CanalRepository canalRepository;

	@Transactional
	public Canal salvar(Canal canal) {
		Canal canalExistente = canalRepository.porDescricao(canal.getDescricao());
		if (canalExistente != null && !canalExistente.equals(canal)) {
			FacesUtil.addErroMessage("Já existe um canal cadastrado com essa descrição.");
			return null;
		} else {
			return canalRepository.salvar(canal);
		}

	}

}
