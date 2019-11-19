package br.com.sis.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import br.com.sis.entity.ItemCheckList;
import br.com.sis.repository.ItemCheckListRepository;
import br.com.sis.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaItemCheckListBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ItemCheckListRepository repository;

	private List<ItemCheckList> listaItemCheckLists;
	private ItemCheckList itemCheckListSelecionado;

	private boolean desabilitarBotoes;

	public boolean isDesabilitarBotoes() {
		return desabilitarBotoes;
	}

	public void setDesabilitarBotoes(boolean desabilitarBotoes) {
		this.desabilitarBotoes = desabilitarBotoes;
	}

	public ItemCheckList getItemCheckListSelecionado() {
		return itemCheckListSelecionado;
	}

	public void setItemCheckListSelecionado(ItemCheckList itemCheckListSelecionado) {
		this.itemCheckListSelecionado = itemCheckListSelecionado;
	}

	public List<ItemCheckList> getlistaItemCheckLists() {
		return listaItemCheckLists;
	}

	public void inicializar() {
		listaItemCheckLists = repository.listAll();
		itemCheckListSelecionado = new ItemCheckList();
		this.desabilitarBotoes = true;
	}

	public void excluir() {
		if (repository.remover(itemCheckListSelecionado) == true) {
			itemCheckListSelecionado = new ItemCheckList();
			listaItemCheckLists = repository.listAll();
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
