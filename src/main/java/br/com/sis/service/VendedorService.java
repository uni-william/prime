package br.com.sis.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.sis.entity.Vendedor;
import br.com.sis.repository.VendedorRepository;
import br.com.sis.util.jpa.Transactional;
import br.com.sis.util.jsf.FacesUtil;

public class VendedorService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private VendedorRepository vendedorRepository;

	@Transactional
	public Vendedor salvar(Vendedor vendedor) {
		Vendedor vendedorExistente = vendedorRepository.porNome(vendedor.getNome());
		if (vendedorExistente != null && !vendedorExistente.equals(vendedor)) {
			FacesUtil.addErroMessage("Já existe vendedor com esse nome.");
			return null;
		} else {
			return vendedorRepository.salvar(vendedor);
		}

	}

}
