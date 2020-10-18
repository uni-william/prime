package br.com.sis.bean;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sis.entity.TipoDespesa;
import br.com.sis.security.Seguranca;
import br.com.sis.service.TipoDespesaService;
import br.com.sis.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroTipoDespesaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private TipoDespesaService service;

	@Inject
	private Seguranca seguranca;

	private TipoDespesa tipoDespesa;

	public TipoDespesa getTipoDespesa() {
		return tipoDespesa;
	}

	public void setTipoDespesa(TipoDespesa tipoDespesa) {
		this.tipoDespesa = tipoDespesa;
	}

	public void inicializar() {
		if (tipoDespesa == null) {
			tipoDespesa = new TipoDespesa();
		}
	}

	public void salvar() {
		tipoDespesa.setDescricao(tipoDespesa.getDescricao().trim());
		if (service.salvar(tipoDespesa) != null) {
			FacesUtil.addInfoMessage("TipoDespesa salvo com sucesso!");
			tipoDespesa = new TipoDespesa();
		}
	}

	public boolean isEditando() {
		return this.tipoDespesa.getId() != null;
	}

	public boolean isPodeSalvar() {
		return (isEditando() && seguranca.isTipoDespesaEditar()) || (!isEditando() && seguranca.isTipoDespesaInserir());
	}

}
