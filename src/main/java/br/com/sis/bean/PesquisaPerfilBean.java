package br.com.sis.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import br.com.sis.entity.Perfil;
import br.com.sis.repository.PerfilRepository;
import br.com.sis.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaPerfilBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private PerfilRepository repository;

	private List<Perfil> listaPerfis;
	private Perfil perfilSelecionado;

	private boolean desabilitarBotoes;

	public boolean isDesabilitarBotoes() {
		return desabilitarBotoes;
	}

	public void setDesabilitarBotoes(boolean desabilitarBotoes) {
		this.desabilitarBotoes = desabilitarBotoes;
	}

	public Perfil getPerfilSelecionado() {
		return perfilSelecionado;
	}

	public void setPerfilSelecionado(Perfil perfilSelecionado) {
		this.perfilSelecionado = perfilSelecionado;
	}

	public List<Perfil> getListaPerfis() {
		return listaPerfis;
	}

	public void inicializar() {
		listaPerfis = repository.listAll();
		perfilSelecionado = new Perfil();
		this.desabilitarBotoes = true;
	}

	public void excluir() {
		if (repository.remover(perfilSelecionado) == true) {
			perfilSelecionado = new Perfil();
			listaPerfis = repository.listAll();
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
