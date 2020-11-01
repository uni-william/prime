package br.com.sis.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import br.com.sis.entity.FormaPagamento;
import br.com.sis.repository.FormaPagamentoRepository;
import br.com.sis.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaFormaPagamentoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private FormaPagamentoRepository repository;

	private List<FormaPagamento> listaFormaPagamentos;
	private FormaPagamento formaPagamentoSelecionado;

	private boolean desabilitarBotoes;

	public boolean isDesabilitarBotoes() {
		return desabilitarBotoes;
	}

	public void setDesabilitarBotoes(boolean desabilitarBotoes) {
		this.desabilitarBotoes = desabilitarBotoes;
	}

	public FormaPagamento getFormaPagamentoSelecionado() {
		return formaPagamentoSelecionado;
	}

	public void setFormaPagamentoSelecionado(FormaPagamento formaPagamentoSelecionado) {
		this.formaPagamentoSelecionado = formaPagamentoSelecionado;
	}

	public List<FormaPagamento> getlistaFormaPagamentos() {
		return listaFormaPagamentos;
	}

	public void inicializar() {
		listaFormaPagamentos = repository.listAll();
		formaPagamentoSelecionado = new FormaPagamento();
		this.desabilitarBotoes = true;
	}

	public void excluir() {
		if (repository.remover(formaPagamentoSelecionado) == true) {
			formaPagamentoSelecionado = new FormaPagamento();
			listaFormaPagamentos = repository.listAll();
			FacesUtil.addInfoMessage("Registro excluído com sucesso!");
			setDesabilitarBotoes(true);
		}
	}

	public void onRowSelect(SelectEvent event) {
		desabilitarBotoes = false;
	}

	public void onRowUnSelect(UnselectEvent event) {
		desabilitarBotoes = true;
	}
}
