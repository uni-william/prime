package br.com.sis.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.sis.entity.Configuracoes;
import br.com.sis.repository.ConfiguracoesRepository;
import br.com.sis.util.jpa.Transactional;

public class ConfiguracaoEmailService implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Inject
	private ConfiguracoesRepository configuracoesEmail;
	
	@Transactional
	public Configuracoes salvar(Configuracoes configuracaoEmail) {
		return configuracoesEmail.salvar(configuracaoEmail);
	}

}
