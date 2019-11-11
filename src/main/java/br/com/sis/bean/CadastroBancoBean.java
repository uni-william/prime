package br.com.sis.bean;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sis.entity.Banco;
import br.com.sis.security.Seguranca;
import br.com.sis.service.BancoService;
import br.com.sis.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroBancoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private BancoService service;

	@Inject
	private Seguranca seguranca;

	private Banco banco;

	public Banco getBanco() {
		return banco;
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
	}

	public void inicializar() {
		if (banco == null) {
			banco = new Banco();
		}
	}

	public void salvar() {
		banco.setDescricao(banco.getDescricao().trim());
		if (service.salvar(banco) != null) {
			FacesUtil.addInfoMessage("Banco salvo com sucesso!");
			banco = new Banco();
		}
	}

	public boolean isEditando() {
		return this.banco.getId() != null;
	}

	public boolean isPodeSalvar() {
		return (isEditando() && seguranca.isBancoEditar()) || (!isEditando() && seguranca.isBancoInserir());
	}

}
