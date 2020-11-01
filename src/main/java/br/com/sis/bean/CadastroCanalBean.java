package br.com.sis.bean;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sis.entity.Canal;
import br.com.sis.security.Seguranca;
import br.com.sis.service.CanalService;
import br.com.sis.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroCanalBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private CanalService service;

	@Inject
	private Seguranca seguranca;

	private Canal canal;

	public Canal getCanal() {
		return canal;
	}

	public void setCanal(Canal canal) {
		this.canal = canal;
	}

	public void inicializar() {
		if (canal == null) {
			canal = new Canal();
		}
	}

	public void salvar() {
		canal.setDescricao(canal.getDescricao().trim());
		if (service.salvar(canal) != null) {
			FacesUtil.addInfoMessage("Canal salvo com sucesso!");
			canal = new Canal();
		}
	}

	public boolean isEditando() {
		return this.canal.getId() != null;
	}

	public boolean isPodeSalvar() {
		return (isEditando() && seguranca.isCanalEditar()) || (!isEditando() && seguranca.isCanalInserir());
	}

}
