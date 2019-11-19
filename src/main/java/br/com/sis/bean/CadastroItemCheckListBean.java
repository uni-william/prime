package br.com.sis.bean;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sis.entity.ItemCheckList;
import br.com.sis.service.ItemCheckListService;
import br.com.sis.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroItemCheckListBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ItemCheckListService service;

	private ItemCheckList itemCheckList;

	public ItemCheckList getItemCheckList() {
		return itemCheckList;
	}

	public void setItemCheckList(ItemCheckList itemCheckList) {
		this.itemCheckList = itemCheckList;
	}

	public void inicializar() {
		if (itemCheckList == null) {
			itemCheckList = new ItemCheckList();
		}
	}

	public void salvar() {
		itemCheckList.setDescricao(itemCheckList.getDescricao().trim());
		if (service.salvar(itemCheckList) != null) {
			FacesUtil.addInfoMessage("Item de CheckList salvo com sucesso!");
			itemCheckList = new ItemCheckList();
		}
	}

	public boolean isEditando() {
		return this.itemCheckList.getId() != null;
	}

}
