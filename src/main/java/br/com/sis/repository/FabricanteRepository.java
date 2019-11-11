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

import br.com.sis.entity.Fabricante;
import br.com.sis.util.jpa.Transactional;
import br.com.sis.util.jsf.FacesUtil;

public class FabricanteRepository implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public List<Fabricante> listAll() {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Fabricante> criteriaQuery = builder.createQuery(Fabricante.class);
		Root<Fabricante> fabricanteRoot = criteriaQuery.from(Fabricante.class);
		criteriaQuery.select(fabricanteRoot);
		criteriaQuery.orderBy(builder.asc(fabricanteRoot.get("descricao")));
		TypedQuery<Fabricante> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}
	
	public Fabricante porId(Long id) {
		return manager.find(Fabricante.class, id);
	}

	@Transactional
	public boolean remover(Fabricante fabricante) {
		try {
			fabricante = porId(fabricante.getId());
			manager.remove(fabricante);
			manager.flush();
			return true;
		} catch (PersistenceException e) {
			FacesUtil.addErroMessage("O fabricante não pode ser excluído.");
			return false;
		}
	}

	public Fabricante porDescricao(String descricao) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Fabricante> criteriaQuery = builder.createQuery(Fabricante.class);
		Root<Fabricante> fabricanteRoot = criteriaQuery.from(Fabricante.class);
		criteriaQuery.select(fabricanteRoot);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(builder.equal(builder.lower(fabricanteRoot.get("descricao")), descricao.toLowerCase()));
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		TypedQuery<Fabricante> query = manager.createQuery(criteriaQuery);
		List<Fabricante> lista = query.getResultList();
		Fabricante fabricante = null;
		for (Fabricante p : lista) {
			fabricante = p;
		}
		return fabricante;
	}

	public Fabricante salvar(Fabricante fabricante) {
		return manager.merge(fabricante);
	}
	
}
