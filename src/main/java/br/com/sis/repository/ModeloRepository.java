package br.com.sis.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.com.sis.entity.Fabricante;
import br.com.sis.entity.Modelo;

public class ModeloRepository implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public List<Modelo> listAll() {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Modelo> criteriaQuery = builder.createQuery(Modelo.class);
		Root<Modelo> modeloRoot = criteriaQuery.from(Modelo.class);
		modeloRoot.fetch("fabricante", JoinType.INNER);
		criteriaQuery.select(modeloRoot);
		criteriaQuery.orderBy(builder.asc(modeloRoot.get("descricao")));
		TypedQuery<Modelo> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}
	
	
	public List<Modelo> modelosPorFabricante(Fabricante fabricante) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Modelo> criteriaQuery = builder.createQuery(Modelo.class);
		Root<Modelo> modeloRoot = criteriaQuery.from(Modelo.class);
		modeloRoot.fetch("fabricante", JoinType.INNER);
		criteriaQuery.select(modeloRoot);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(builder.equal(modeloRoot.get("fabricante"), fabricante));
		criteriaQuery.where(predicates.toArray(new Predicate[0]));		
		criteriaQuery.orderBy(builder.asc(modeloRoot.get("descricao")));
		TypedQuery<Modelo> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}

	
	public Modelo porId(Long id) {
		return manager.find(Modelo.class, id);
	}
	
	public Modelo salvar(Modelo modelo) {
		return manager.merge(modelo);
	}
	
}
