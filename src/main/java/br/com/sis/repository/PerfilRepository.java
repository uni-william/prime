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

import br.com.sis.entity.Perfil;
import br.com.sis.util.jpa.Transactional;
import br.com.sis.util.jsf.FacesUtil;

public class PerfilRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public List<Perfil> listAll() {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Perfil> criteriaQuery = builder.createQuery(Perfil.class);
		Root<Perfil> perfilRoot = criteriaQuery.from(Perfil.class);
		criteriaQuery.select(perfilRoot);
		TypedQuery<Perfil> query = manager.createQuery(criteriaQuery);
		criteriaQuery.orderBy(builder.asc(perfilRoot.get("descricao")));
		return query.getResultList();
	}
			
	public Perfil porId(Long id) {
		return manager.find(Perfil.class, id);
	}

	@Transactional
	public boolean remover(Perfil perfil) {
		try {
			perfil = porId(perfil.getId());
			manager.remove(perfil);
			manager.flush();
			return true;
		} catch (PersistenceException e) {
			FacesUtil.addErroMessage("O perfil não pode ser excluído.");
			return false;
		}
	}

	public Perfil porDescricao(String descricao) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Perfil> criteriaQuery = builder.createQuery(Perfil.class);
		Root<Perfil> perfilRoot = criteriaQuery.from(Perfil.class);
		criteriaQuery.select(perfilRoot);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(builder.equal(builder.lower(perfilRoot.get("descricao")), descricao.toLowerCase()));
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		TypedQuery<Perfil> query = manager.createQuery(criteriaQuery);
		List<Perfil> lista = query.getResultList();
		Perfil perfil = null;
		for (Perfil p : lista) {
			perfil = p;
		}
		return perfil;
	}

	public Perfil salvar(Perfil perfil) {
		return manager.merge(perfil);
	}

}
