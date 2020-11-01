package br.com.sis.bean;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sis.entity.FormaPagamento;
import br.com.sis.security.Seguranca;
import br.com.sis.service.FormaPagamentoService;
import br.com.sis.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroFormaPagamentoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private FormaPagamentoService service;

	@Inject
	private Seguranca seguranca;

	private FormaPagamento formaPagamento;

	public FormaPagamento getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(FormaPagamento formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public void inicializar() {
		if (formaPagamento == null) {
			formaPagamento = new FormaPagamento();
		}
	}

	public void salvar() {
		formaPagamento.setDescricao(formaPagamento.getDescricao().trim());
		if (service.salvar(formaPagamento) != null) {
			FacesUtil.addInfoMessage("FormaPagamento salvo com sucesso!");
			formaPagamento = new FormaPagamento();
		}
	}

	public boolean isEditando() {
		return this.formaPagamento.getId() != null;
	}

	public boolean isPodeSalvar() {
		return (isEditando() && seguranca.isFormaPagamentoEditar()) || (!isEditando() && seguranca.isFormaPagamentoInserir());
	}

}
