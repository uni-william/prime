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

import org.apache.commons.lang3.StringUtils;

import br.com.sis.entity.Despesa;
import br.com.sis.repository.filter.DespesaFilter;
import br.com.sis.util.jpa.Transactional;
import br.com.sis.util.jsf.FacesUtil;

public class DespesaRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public List<Despesa> listAll(DespesaFilter filter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Despesa> criteriaQuery = builder.createQuery(Despesa.class);
		Root<Despesa> root = criteriaQuery.from(Despesa.class);
		root.fetch("tipoDespesa");
		criteriaQuery.select(root);
		criteriaQuery.where(criarRestricoes(filter, builder, root));
		criteriaQuery.orderBy(builder.asc(root.get("data")));
		TypedQuery<Despesa> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}

	public Despesa porId(Long id) {
		return manager.find(Despesa.class, id);
	}

	@Transactional
	public boolean remover(Despesa despesa) {
		try {
			despesa = porId(despesa.getId());
			manager.remove(despesa);
			manager.flush();
			return true;
		} catch (PersistenceException e) {
			FacesUtil.addErroMessage("O registro não pode ser excluído.");
			return false;
		}
	}

	public Despesa salvar(Despesa despesa) {
		return manager.merge(despesa);
	}

	private Predicate[] criarRestricoes(DespesaFilter filter, CriteriaBuilder builder, Root<Despesa> root) {
		List<Predicate> predicates = new ArrayList<>();

		if (!StringUtils.isEmpty(filter.getDescricao()))
			predicates.add(builder.like(builder.lower(root.get("descricao")),
					"%" + filter.getDescricao().toLowerCase() + "%"));

		if (filter.getDtIni() != null)
			predicates.add(builder.greaterThanOrEqualTo(root.get("data"), filter.getDtIni()));

		if (filter.getDtFim() != null)
			predicates.add(builder.lessThanOrEqualTo(root.get("data"), filter.getDtFim()));

		if (filter.getTipoDespesa() != null)
			predicates.add(builder.equal(root.get("tipoDespesa"), filter.getTipoDespesa()));

		return predicates.toArray(new Predicate[predicates.size()]);
	}
}
