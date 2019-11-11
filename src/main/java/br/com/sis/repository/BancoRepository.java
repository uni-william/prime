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

import br.com.sis.entity.Banco;
import br.com.sis.util.jpa.Transactional;
import br.com.sis.util.jsf.FacesUtil;

public class BancoRepository implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public List<Banco> listAll() {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Banco> criteriaQuery = builder.createQuery(Banco.class);
		Root<Banco> bancoRoot = criteriaQuery.from(Banco.class);
		criteriaQuery.select(bancoRoot);
		criteriaQuery.orderBy(builder.asc(bancoRoot.get("descricao")));
		TypedQuery<Banco> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}
	
	public Banco porId(Long id) {
		return manager.find(Banco.class, id);
	}

	@Transactional
	public boolean remover(Banco banco) {
		try {
			banco = porId(banco.getId());
			manager.remove(banco);
			manager.flush();
			return true;
		} catch (PersistenceException e) {
			FacesUtil.addErroMessage("O banco não pode ser excluído.");
			return false;
		}
	}

	public Banco porDescricao(String descricao) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Banco> criteriaQuery = builder.createQuery(Banco.class);
		Root<Banco> bancoRoot = criteriaQuery.from(Banco.class);
		criteriaQuery.select(bancoRoot);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(builder.equal(builder.lower(bancoRoot.get("descricao")), descricao.toLowerCase()));
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		TypedQuery<Banco> query = manager.createQuery(criteriaQuery);
		List<Banco> lista = query.getResultList();
		Banco banco = null;
		for (Banco p : lista) {
			banco = p;
		}
		return banco;
	}

	public Banco salvar(Banco banco) {
		return manager.merge(banco);
	}
	
}
