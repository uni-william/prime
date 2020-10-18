package br.com.sis.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import br.com.sis.entity.TipoDespesa;
import br.com.sis.repository.TipoDespesaRepository;
import br.com.sis.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaTipoDespesaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private TipoDespesaRepository repository;

	private List<TipoDespesa> listaTipoDespesas;
	private TipoDespesa tipoDespesaSelecionado;

	private boolean desabilitarBotoes;

	public boolean isDesabilitarBotoes() {
		return desabilitarBotoes;
	}

	public void setDesabilitarBotoes(boolean desabilitarBotoes) {
		this.desabilitarBotoes = desabilitarBotoes;
	}

	public TipoDespesa getTipoDespesaSelecionado() {
		return tipoDespesaSelecionado;
	}

	public void setTipoDespesaSelecionado(TipoDespesa tipoDespesaSelecionado) {
		this.tipoDespesaSelecionado = tipoDespesaSelecionado;
	}

	public List<TipoDespesa> getlistaTipoDespesas() {
		return listaTipoDespesas;
	}

	public void inicializar() {
		listaTipoDespesas = repository.listAll();
		tipoDespesaSelecionado = new TipoDespesa();
		this.desabilitarBotoes = true;
	}

	public void excluir() {
		if (repository.remover(tipoDespesaSelecionado) == true) {
			tipoDespesaSelecionado = new TipoDespesa();
			listaTipoDespesas = repository.listAll();
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
