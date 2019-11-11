package br.com.sis.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.sis.entity.Perfil;
import br.com.sis.repository.PerfilRepository;
import br.com.sis.util.jpa.Transactional;
import br.com.sis.util.jsf.FacesUtil;

public class PerfilService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private PerfilRepository perfilRepository;

	@Transactional
	public Perfil salvar(Perfil perfil) {
		Perfil perfilExistente = perfilRepository.porDescricao(perfil.getDescricao());
		if (perfilExistente != null && !perfilExistente.equals(perfil)) {
			FacesUtil.addErroMessage("Já existe um perfil cadastrado com essa descrição.");
			return null;
		} else {
			return perfilRepository.salvar(perfil);
		}

	}

}
