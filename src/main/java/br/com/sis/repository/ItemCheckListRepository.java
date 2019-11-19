package br.com.sis.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.com.sis.entity.ItemCheckList;
import br.com.sis.util.jpa.Transactional;
import br.com.sis.util.jsf.FacesUtil;

public class ItemCheckListRepository implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public List<ItemCheckList> listAll() {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<ItemCheckList> criteriaQuery = builder.createQuery(ItemCheckList.class);
		Root<ItemCheckList> itemCheckListRoot = criteriaQuery.from(ItemCheckList.class);
		criteriaQuery.select(itemCheckListRoot);
		criteriaQuery.orderBy(builder.asc(itemCheckListRoot.get("descricao")));
		TypedQuery<ItemCheckList> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}
	
	public ItemCheckList porId(Long id) {
		return manager.find(ItemCheckList.class, id);
	}

	@Transactional
	public boolean remover(ItemCheckList itemCheckList) {
		try {
			itemCheckList = porId(itemCheckList.getId());
			manager.remove(itemCheckList);
			manager.flush();
			return true;
		} catch (PersistenceException e) {
			FacesUtil.addErroMessage("O Item não pode ser excluído.");
			return false;
		}
	}

	public ItemCheckList porDescricao(String descricao) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<ItemCheckList> criteriaQuery = builder.createQuery(ItemCheckList.class);
		Root<ItemCheckList> itemCheckListRoot = criteriaQuery.from(ItemCheckList.class);
		criteriaQuery.select(itemCheckListRoot);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(builder.equal(builder.lower(itemCheckListRoot.get("descricao")), descricao.toLowerCase()));
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		TypedQuery<ItemCheckList> query = manager.createQuery(criteriaQuery);
		List<ItemCheckList> lista = query.getResultList();
		ItemCheckList itemCheckList = null;
		for (ItemCheckList p : lista) {
			itemCheckList = p;
		}
		return itemCheckList;
	}

	public ItemCheckList salvar(ItemCheckList itemCheckList) {
		return manager.merge(itemCheckList);
	}
	
}
