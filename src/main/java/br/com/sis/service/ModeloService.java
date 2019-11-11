package br.com.sis.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.sis.entity.Modelo;
import br.com.sis.repository.ModeloRepository;
import br.com.sis.util.jpa.Transactional;

public class ModeloService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ModeloRepository modeloRepository;

	@Transactional
	public Modelo salvar(Modelo modelo) {
		return modeloRepository.salvar(modelo);
	}

}
