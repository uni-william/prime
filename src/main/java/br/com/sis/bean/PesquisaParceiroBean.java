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
public class PesquisaParceiroBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private PessoaRepository repository;

	private List<Pessoa> listaFiltrada;
	private Pessoa parceiroSelecionado;
	private PessoaFilter filter;

	private boolean desabilitarBotoes;

	public Pessoa getParceiroSelecionado() {
		return parceiroSelecionado;
	}

	public void setParceiroSelecionado(Pessoa parceiroSelecionado) {
		this.parceiroSelecionado = parceiroSelecionado;
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
		return parceiroSelecionado;
	}

	public void setFuncionarioSelecionado(Pessoa parceiroSelecionado) {
		this.parceiroSelecionado = parceiroSelecionado;
	}

	public void setDesabilitarBotoes(boolean desabilitarBotoes) {
		this.desabilitarBotoes = desabilitarBotoes;
	}

	public void inicializar() {
		filter = new PessoaFilter();
		desabilitarBotoes = true;
	}

	public void pesquisar() {
		listaFiltrada = repository.parceirosFiltrados(filter);
	}

	public void onRowSelect(SelectEvent event) {
		desabilitarBotoes = false;
	}

	public void onRowUnSelect(UnselectEvent event) {
		desabilitarBotoes = true;
	}

	public void excluir() {
		if (repository.remover(parceiroSelecionado) == true) {
			parceiroSelecionado = new Pessoa();
			listaFiltrada = repository.parceirosFiltrados(filter);
			FacesUtil.addInfoMessage("Registro excluído com sucesso!");
			desabilitarBotoes = true;
		}
	}

}
