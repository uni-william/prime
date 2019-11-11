package br.com.sis.bean;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sis.entity.Acessorio;
import br.com.sis.security.Seguranca;
import br.com.sis.service.AcessorioService;
import br.com.sis.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroAcessorioBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private AcessorioService service;

	@Inject
	private Seguranca seguranca;

	private Acessorio acessorio;

	public Acessorio getAcessorio() {
		return acessorio;
	}

	public void setAcessorio(Acessorio acessorio) {
		this.acessorio = acessorio;
	}

	public void inicializar() {
		if (acessorio == null) {
			acessorio = new Acessorio();
		}
	}

	public void salvar() {
		acessorio.setDescricao(acessorio.getDescricao().trim());
		if (service.salvar(acessorio) != null) {
			FacesUtil.addInfoMessage("Acessorio salvo com sucesso!");
			acessorio = new Acessorio();
		}
	}

	public boolean isEditando() {
		return this.acessorio.getId() != null;
	}

	public boolean isPodeSalvar() {
		return (isEditando() && seguranca.isAcessorioEditar()) || (!isEditando() && seguranca.isAcessorioInserir());
	}

}
