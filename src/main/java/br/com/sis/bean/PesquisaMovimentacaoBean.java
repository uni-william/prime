package br.com.sis.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sis.entity.Movimentacao;
import br.com.sis.enuns.TipoOperacao;
import br.com.sis.repository.MovimentacaoRepository;
import br.com.sis.repository.filter.MovimentacaoFilter;
import br.com.sis.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaMovimentacaoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private MovimentacaoRepository repository;

	private List<Movimentacao> lista;
	private Movimentacao movimentacaoSelecionado;
	private MovimentacaoFilter filter;

	public MovimentacaoFilter getFilter() {
		return filter;
	}

	public void setFilter(MovimentacaoFilter filter) {
		this.filter = filter;
	}

	public Movimentacao getmovimentacaoSelecionado() {
		return movimentacaoSelecionado;
	}

	public void setmovimentacaoSelecionado(Movimentacao movimentacaoSelecionado) {
		this.movimentacaoSelecionado = movimentacaoSelecionado;
	}

	public List<Movimentacao> getLista() {
		return lista;
	}

	public void inicializar() {
		movimentacaoSelecionado = new Movimentacao();
		filter = new MovimentacaoFilter();
	}
	
	public TipoOperacao[] getTipoOperacoes() {
		return TipoOperacao.values();
	}

	public void pesquisar() {
		lista = repository.filtrados(filter);
	}

	public void excluir() {
		if (repository.remover(movimentacaoSelecionado) == true) {
			movimentacaoSelecionado = new Movimentacao();
			lista = repository.filtrados(filter);
			FacesUtil.addInfoMessage("Registro excluído com sucesso!");

		}
	}
	
	public void selecionar(Movimentacao mov) {
		if (mov.getTipoOperacao().equals(TipoOperacao.COMPRA)) {
			FacesUtil.redirecionarPagina("/movimentacoes/CadastroCompra.xhtml?compra=" + mov.getId());
		} else {
			FacesUtil.redirecionarPagina("/movimentacoes/CadastroVenda.xhtml?venda=" + mov.getId());
		}
	}
}