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

import br.com.sis.entity.TipoDespesa;
import br.com.sis.util.jpa.Transactional;
import br.com.sis.util.jsf.FacesUtil;

public class TipoDespesaRepository implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public List<TipoDespesa> listAll() {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<TipoDespesa> criteriaQuery = builder.createQuery(TipoDespesa.class);
		Root<TipoDespesa> tipoDespesaRoot = criteriaQuery.from(TipoDespesa.class);
		criteriaQuery.select(tipoDespesaRoot);
		criteriaQuery.orderBy(builder.asc(tipoDespesaRoot.get("descricao")));
		TypedQuery<TipoDespesa> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}
	
	public TipoDespesa porId(Long id) {
		return manager.find(TipoDespesa.class, id);
	}

	@Transactional
	public boolean remover(TipoDespesa tipoDespesa) {
		try {
			tipoDespesa = porId(tipoDespesa.getId());
			manager.remove(tipoDespesa);
			manager.flush();
			return true;
		} catch (PersistenceException e) {
			FacesUtil.addErroMessage("O registro não pode ser excluído.");
			return false;
		}
	}

	public TipoDespesa porDescricao(String descricao) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<TipoDespesa> criteriaQuery = builder.createQuery(TipoDespesa.class);
		Root<TipoDespesa> tipoDespesaRoot = criteriaQuery.from(TipoDespesa.class);
		criteriaQuery.select(tipoDespesaRoot);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(builder.equal(builder.lower(tipoDespesaRoot.get("descricao")), descricao.toLowerCase()));
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		TypedQuery<TipoDespesa> query = manager.createQuery(criteriaQuery);
		List<TipoDespesa> lista = query.getResultList();
		TipoDespesa tipoDespesa = null;
		for (TipoDespesa p : lista) {
			tipoDespesa = p;
		}
		return tipoDespesa;
	}

	public TipoDespesa salvar(TipoDespesa tipoDespesa) {
		return manager.merge(tipoDespesa);
	}
	
}
