package br.com.sis.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.sis.entity.Fabricante;
import br.com.sis.repository.FabricanteRepository;
import br.com.sis.util.jpa.Transactional;
import br.com.sis.util.jsf.FacesUtil;

public class FabricanteService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private FabricanteRepository fabricanteRepository;

	@Transactional
	public Fabricante salvar(Fabricante fabricante) {
		Fabricante fabricanteExistente = fabricanteRepository.porDescricao(fabricante.getDescricao());
		if (fabricanteExistente != null && !fabricanteExistente.equals(fabricante)) {
			FacesUtil.addErroMessage("Já existe um fabricante cadastrado com essa descrição.");
			return null;
		} else {
			return fabricanteRepository.salvar(fabricante);
		}

	}

}
