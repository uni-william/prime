package br.com.sis.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sis.entity.Intermediacao;
import br.com.sis.entity.Pessoa;
import br.com.sis.enuns.TipoOperacao;
import br.com.sis.repository.IntermediacaoRepository;
import br.com.sis.repository.PessoaRepository;
import br.com.sis.repository.filter.IntermediacaoFilter;
import br.com.sis.repository.filter.PessoaFilter;
import br.com.sis.service.IntermediacaoService;
import br.com.sis.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaIntermediacaoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private IntermediacaoRepository repository;

	@Inject
	private PessoaRepository pessoaRepository;
	
	@Inject
	private IntermediacaoService service;

	private List<Intermediacao> lista;
	private Intermediacao intermediacaoSelecionado;
	private IntermediacaoFilter filter;

	private List<Pessoa> parceiros;

	public IntermediacaoFilter getFilter() {
		return filter;
	}

	public void setFilter(IntermediacaoFilter filter) {
		this.filter = filter;
	}

	public Intermediacao getintermediacaoSelecionado() {
		return intermediacaoSelecionado;
	}

	public void setintermediacaoSelecionado(Intermediacao intermediacaoSelecionado) {
		this.intermediacaoSelecionado = intermediacaoSelecionado;
	}

	public List<Intermediacao> getLista() {
		return lista;
	}

	public List<Pessoa> getParceiros() {
		return parceiros;
	}

	public void setParceiros(List<Pessoa> parceiros) {
		this.parceiros = parceiros;
	}

	public void inicializar() {
		PessoaFilter parceiroFilter = new PessoaFilter();
		parceiros = pessoaRepository.parceirosFiltrados(parceiroFilter);
		intermediacaoSelecionado = new Intermediacao();
		filter = new IntermediacaoFilter();
	}

	public TipoOperacao[] getTipoOperacoes() {
		return TipoOperacao.values();
	}

	public void pesquisar() {
		lista = repository.filtrados(filter);
	}

	public void excluir() {
		if (repository.remover(intermediacaoSelecionado) == true) {
			intermediacaoSelecionado = new Intermediacao();
			lista = repository.filtrados(filter);
			FacesUtil.addInfoMessage("Registro excluído com sucesso!");

		}
	}
	
	public void cancelarVenda() {
		intermediacaoSelecionado = service.cancelarVenda(intermediacaoSelecionado);
		if (intermediacaoSelecionado != null) {
			FacesUtil.addInfoMessage("Operação realizada com sucesso");
		}
		
	}	
}