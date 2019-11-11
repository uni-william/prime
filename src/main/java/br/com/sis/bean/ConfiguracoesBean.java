package br.com.sis.bean;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sis.entity.Configuracoes;
import br.com.sis.repository.ConfiguracoesRepository;
import br.com.sis.service.ConfiguracaoEmailService;
import br.com.sis.util.Utils;
import br.com.sis.util.jsf.FacesUtil;

@Named
@ViewScoped
public class ConfiguracoesBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ConfiguracoesRepository configuracoesEmail;

	private Configuracoes configuracoes;

	@Inject
	private ConfiguracaoEmailService configuracaoEmailService;

	private String senha;

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}


	public Configuracoes getConfiguracoes() {
		return configuracoes;
	}

	public void setConfiguracoes(Configuracoes configuracoes) {
		this.configuracoes = configuracoes;
	}

	public void inicializar() {
		configuracoes = configuracoesEmail.configuracoesGerais();
		if (configuracoes == null) {
			configuracoes = new Configuracoes();
		} else {
			senha = Utils.decripto(configuracoes.getSenhaUsuarioEmail());
		}
	}

	public void salvar() {
		configuracoes.setSenhaUsuarioEmail(Utils.cripto(senha));
		configuracoes = configuracaoEmailService.salvar(configuracoes);
		FacesUtil.addInfoMessage("Configurações salva com sucesso!");
	}

}
