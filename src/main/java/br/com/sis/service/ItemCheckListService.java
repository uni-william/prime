package br.com.sis.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.sis.entity.ItemCheckList;
import br.com.sis.repository.ItemCheckListRepository;
import br.com.sis.util.jpa.Transactional;
import br.com.sis.util.jsf.FacesUtil;

public class ItemCheckListService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ItemCheckListRepository itemCheckListRepository;

	@Transactional
	public ItemCheckList salvar(ItemCheckList itemCheckList) {
		ItemCheckList itemCheckListExistente = itemCheckListRepository.porDescricao(itemCheckList.getDescricao());
		if (itemCheckListExistente != null && !itemCheckListExistente.equals(itemCheckList)) {
			FacesUtil.addErroMessage("Já existe um Item de CheckList cadastrado com essa descrição.");
			return null;
		} else {
			return itemCheckListRepository.salvar(itemCheckList);
		}

	}

}
