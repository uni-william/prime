package br.com.sis.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import br.com.sis.entity.Pessoa;
import br.com.sis.repository.PessoaRepository;
import br.com.sis.repository.filter.PessoaFilter;
import br.com.sis.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaClienteBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private PessoaRepository repository;

	private List<Pessoa> listaFiltrada;
	private Pessoa clienteSelecionado;
	private PessoaFilter filter;

	private boolean desabilitarBotoes;

	public Pessoa getClienteSelecionado() {
		return clienteSelecionado;
	}

	public void setClienteSelecionado(Pessoa clienteSelecionado) {
		this.clienteSelecionado = clienteSelecionado;
	}

	public PessoaFilter getFilter() {
		return filter;
	}

	public void setFilter(PessoaFilter filter) {
		this.filter = filter;
	}

	public List<Pessoa> getListaFiltrada() {
		return listaFiltrada;
	}

	public boolean isDesabilitarBotoes() {
		return desabilitarBotoes;
	}

	public Pessoa getFuncionarioSelecionado() {
		return clienteSelecionado;
	}

	public void setFuncionarioSelecionado(Pessoa clienteSelecionado) {
		this.clienteSelecionado = clienteSelecionado;
	}

	public void setDesabilitarBotoes(boolean desabilitarBotoes) {
		this.desabilitarBotoes = desabilitarBotoes;
	}

	public void inicializar() {
		filter = new PessoaFilter();
		desabilitarBotoes = true;
	}

	public void pesquisar() {
		listaFiltrada = repository.clientesFiltrados(filter);
	}

	public void onRowSelect(SelectEvent event) {
		desabilitarBotoes = false;
	}

	public void onRowUnSelect(UnselectEvent event) {
		desabilitarBotoes = true;
	}

	public void excluir() {
		if (repository.remover(clienteSelecionado) == true) {
			clienteSelecionado = new Pessoa();
			listaFiltrada = repository.clientesFiltrados(filter);
			FacesUtil.addInfoMessage("Registro excluído com sucesso!");
			desabilitarBotoes = true;
		}
	}

}
