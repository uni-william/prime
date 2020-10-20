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

import br.com.sis.entity.Vendedor;
import br.com.sis.util.jpa.Transactional;
import br.com.sis.util.jsf.FacesUtil;

public class VendedorRepository implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public List<Vendedor> listAll() {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Vendedor> criteriaQuery = builder.createQuery(Vendedor.class);
		Root<Vendedor> vendedorRoot = criteriaQuery.from(Vendedor.class);
		criteriaQuery.select(vendedorRoot);
		criteriaQuery.orderBy(builder.asc(vendedorRoot.get("nome")));
		TypedQuery<Vendedor> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}
	
	public List<Vendedor> listAllAtivos() {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Vendedor> criteriaQuery = builder.createQuery(Vendedor.class);
		Root<Vendedor> vendedorRoot = criteriaQuery.from(Vendedor.class);
		criteriaQuery.select(vendedorRoot);
		criteriaQuery.where(builder.equal(vendedorRoot.get("ativo"), true));
		criteriaQuery.orderBy(builder.asc(vendedorRoot.get("nome")));
		TypedQuery<Vendedor> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}
	
	public Vendedor porId(Long id) {
		return manager.find(Vendedor.class, id);
	}

	@Transactional
	public boolean remover(Vendedor vendedor) {
		try {
			vendedor = porId(vendedor.getId());
			manager.remove(vendedor);
			manager.flush();
			return true;
		} catch (PersistenceException e) {
			FacesUtil.addErroMessage("O registro não pode ser excluído.");
			return false;
		}
	}

	public Vendedor porNome(String nome) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Vendedor> criteriaQuery = builder.createQuery(Vendedor.class);
		Root<Vendedor> vendedorRoot = criteriaQuery.from(Vendedor.class);
		criteriaQuery.select(vendedorRoot);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(builder.equal(builder.lower(vendedorRoot.get("nome")), nome.toLowerCase()));
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		TypedQuery<Vendedor> query = manager.createQuery(criteriaQuery);
		List<Vendedor> lista = query.getResultList();
		Vendedor vendedor = null;
		for (Vendedor p : lista) {
			vendedor = p;
		}
		return vendedor;
	}

	public Vendedor salvar(Vendedor vendedor) {
		return manager.merge(vendedor);
	}
	
}
