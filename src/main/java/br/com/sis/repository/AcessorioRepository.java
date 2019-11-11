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

import br.com.sis.entity.Acessorio;
import br.com.sis.util.jpa.Transactional;
import br.com.sis.util.jsf.FacesUtil;

public class AcessorioRepository implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public List<Acessorio> listAll() {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Acessorio> criteriaQuery = builder.createQuery(Acessorio.class);
		Root<Acessorio> acessorioRoot = criteriaQuery.from(Acessorio.class);
		criteriaQuery.select(acessorioRoot);
		criteriaQuery.orderBy(builder.asc(acessorioRoot.get("descricao")));
		TypedQuery<Acessorio> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}
	
	public Acessorio porId(Long id) {
		return manager.find(Acessorio.class, id);
	}

	@Transactional
	public boolean remover(Acessorio acessorio) {
		try {
			acessorio = porId(acessorio.getId());
			manager.remove(acessorio);
			manager.flush();
			return true;
		} catch (PersistenceException e) {
			FacesUtil.addErroMessage("O acessorio não pode ser excluído.");
			return false;
		}
	}

	public Acessorio porDescricao(String descricao) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Acessorio> criteriaQuery = builder.createQuery(Acessorio.class);
		Root<Acessorio> acessorioRoot = criteriaQuery.from(Acessorio.class);
		criteriaQuery.select(acessorioRoot);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(builder.equal(builder.lower(acessorioRoot.get("descricao")), descricao.toLowerCase()));
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		TypedQuery<Acessorio> query = manager.createQuery(criteriaQuery);
		List<Acessorio> lista = query.getResultList();
		Acessorio acessorio = null;
		for (Acessorio p : lista) {
			acessorio = p;
		}
		return acessorio;
	}

	public Acessorio salvar(Acessorio acessorio) {
		return manager.merge(acessorio);
	}
	
}
