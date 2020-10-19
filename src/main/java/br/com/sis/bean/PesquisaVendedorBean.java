package br.com.sis.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import br.com.sis.entity.Vendedor;
import br.com.sis.repository.VendedorRepository;
import br.com.sis.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaVendedorBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private VendedorRepository repository;

	private List<Vendedor> listaVendedores;
	private Vendedor vendedorSelecionado;

	private boolean desabilitarBotoes;

	public boolean isDesabilitarBotoes() {
		return desabilitarBotoes;
	}

	public void setDesabilitarBotoes(boolean desabilitarBotoes) {
		this.desabilitarBotoes = desabilitarBotoes;
	}

	public Vendedor getVendedorSelecionado() {
		return vendedorSelecionado;
	}

	public void setVendedorSelecionado(Vendedor vendedorSelecionado) {
		this.vendedorSelecionado = vendedorSelecionado;
	}

	public List<Vendedor> getlistaVendedores() {
		return listaVendedores;
	}

	public void inicializar() {
		listaVendedores = repository.listAll();
		vendedorSelecionado = new Vendedor();
		this.desabilitarBotoes = true;
	}

	public void excluir() {
		if (repository.remover(vendedorSelecionado) == true) {
			vendedorSelecionado = new Vendedor();
			listaVendedores = repository.listAll();
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
