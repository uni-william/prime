package br.com.sis.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import br.com.sis.entity.Usuario;
import br.com.sis.repository.UsuarioRepository;
import br.com.sis.repository.filter.UsuarioFilter;
import br.com.sis.service.UsuarioService;
import br.com.sis.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaUsuarioBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private UsuarioRepository repository;

	@Inject
	private UsuarioService usuarioService;

	private List<Usuario> listaUsuarios;

	private Usuario usuarioSelecionado;

	private UsuarioFilter filter;

	private boolean desabilitarBotoes;

	public boolean isDesabilitarBotoes() {
		return desabilitarBotoes;
	}

	public void setDesabilitarBotoes(boolean desabilitarBotoes) {
		this.desabilitarBotoes = desabilitarBotoes;
	}

	public Usuario getUsuarioSelecionado() {
		return usuarioSelecionado;
	}

	public void setUsuarioSelecionado(Usuario usuarioSelecionado) {
		this.usuarioSelecionado = usuarioSelecionado;
	}

	public UsuarioFilter getFilter() {
		return filter;
	}

	public void setFilter(UsuarioFilter filter) {
		this.filter = filter;
	}

	public List<Usuario> getListaUsuarios() {
		return listaUsuarios;
	}

	public void inicalizar() {
		filter = new UsuarioFilter();
		desabilitarBotoes = true;
	}

	public void pesquisar() {
		listaUsuarios = repository.filtrados(filter);
	}

	public void onRowSelect(SelectEvent event) {
		desabilitarBotoes = false;
	}

	public void onRowUnSelect(UnselectEvent event) {
		desabilitarBotoes = true;
	}

	public void excluir() {
		if (repository.remover(usuarioSelecionado) == true) {
			usuarioSelecionado = new Usuario();
			listaUsuarios = repository.filtrados(filter);
			FacesUtil.addInfoMessage("Registro excluído com sucesso!");
			desabilitarBotoes = true;
		}
	}
	
	public void gerarSenhaESalvar() {
		usuarioService.gerarSenhaESalvar(usuarioSelecionado);
	}
	

}
