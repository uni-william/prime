package br.com.sis.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.sis.entity.Banco;
import br.com.sis.repository.BancoRepository;
import br.com.sis.util.jpa.Transactional;
import br.com.sis.util.jsf.FacesUtil;

public class BancoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private BancoRepository bancoRepository;

	@Transactional
	public Banco salvar(Banco banco) {
		Banco bancoExistente = bancoRepository.porDescricao(banco.getDescricao());
		if (bancoExistente != null && !bancoExistente.equals(banco)) {
			FacesUtil.addErroMessage("Já existe um banco cadastrado com essa descrição.");
			return null;
		} else {
			return bancoRepository.salvar(banco);
		}

	}

}
