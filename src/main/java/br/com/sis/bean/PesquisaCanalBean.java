package br.com.sis.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import br.com.sis.entity.Canal;
import br.com.sis.repository.CanalRepository;
import br.com.sis.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaCanalBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private CanalRepository repository;

	private List<Canal> listaCanals;
	private Canal canalSelecionado;

	private boolean desabilitarBotoes;

	public boolean isDesabilitarBotoes() {
		return desabilitarBotoes;
	}

	public void setDesabilitarBotoes(boolean desabilitarBotoes) {
		this.desabilitarBotoes = desabilitarBotoes;
	}

	public Canal getCanalSelecionado() {
		return canalSelecionado;
	}

	public void setCanalSelecionado(Canal canalSelecionado) {
		this.canalSelecionado = canalSelecionado;
	}

	public List<Canal> getlistaCanals() {
		return listaCanals;
	}

	public void inicializar() {
		listaCanals = repository.listAll();
		canalSelecionado = new Canal();
		this.desabilitarBotoes = true;
	}

	public void excluir() {
		if (repository.remover(canalSelecionado) == true) {
			canalSelecionado = new Canal();
			listaCanals = repository.listAll();
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
