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

import br.com.sis.entity.Canal;
import br.com.sis.util.jpa.Transactional;
import br.com.sis.util.jsf.FacesUtil;

public class CanalRepository implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public List<Canal> listAll() {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Canal> criteriaQuery = builder.createQuery(Canal.class);
		Root<Canal> canalRoot = criteriaQuery.from(Canal.class);
		criteriaQuery.select(canalRoot);
		criteriaQuery.orderBy(builder.asc(canalRoot.get("descricao")));
		TypedQuery<Canal> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}
	
	public Canal porId(Long id) {
		return manager.find(Canal.class, id);
	}

	@Transactional
	public boolean remover(Canal canal) {
		try {
			canal = porId(canal.getId());
			manager.remove(canal);
			manager.flush();
			return true;
		} catch (PersistenceException e) {
			FacesUtil.addErroMessage("O canal não pode ser excluído.");
			return false;
		}
	}

	public Canal porDescricao(String descricao) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Canal> criteriaQuery = builder.createQuery(Canal.class);
		Root<Canal> canalRoot = criteriaQuery.from(Canal.class);
		criteriaQuery.select(canalRoot);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(builder.equal(builder.lower(canalRoot.get("descricao")), descricao.toLowerCase()));
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		TypedQuery<Canal> query = manager.createQuery(criteriaQuery);
		List<Canal> lista = query.getResultList();
		Canal canal = null;
		for (Canal p : lista) {
			canal = p;
		}
		return canal;
	}

	public Canal salvar(Canal canal) {
		return manager.merge(canal);
	}
	
}
