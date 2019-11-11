package br.com.sis.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import br.com.sis.entity.Banco;
import br.com.sis.repository.BancoRepository;
import br.com.sis.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaBancoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private BancoRepository repository;

	private List<Banco> listaBancos;
	private Banco bancoSelecionado;

	private boolean desabilitarBotoes;

	public boolean isDesabilitarBotoes() {
		return desabilitarBotoes;
	}

	public void setDesabilitarBotoes(boolean desabilitarBotoes) {
		this.desabilitarBotoes = desabilitarBotoes;
	}

	public Banco getBancoSelecionado() {
		return bancoSelecionado;
	}

	public void setBancoSelecionado(Banco bancoSelecionado) {
		this.bancoSelecionado = bancoSelecionado;
	}

	public List<Banco> getlistaBancos() {
		return listaBancos;
	}

	public void inicializar() {
		listaBancos = repository.listAll();
		bancoSelecionado = new Banco();
		this.desabilitarBotoes = true;
	}

	public void excluir() {
		if (repository.remover(bancoSelecionado) == true) {
			bancoSelecionado = new Banco();
			listaBancos = repository.listAll();
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
