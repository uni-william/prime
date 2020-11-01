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

import br.com.sis.entity.FormaPagamento;
import br.com.sis.util.jpa.Transactional;
import br.com.sis.util.jsf.FacesUtil;

public class FormaPagamentoRepository implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public List<FormaPagamento> listAll() {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<FormaPagamento> criteriaQuery = builder.createQuery(FormaPagamento.class);
		Root<FormaPagamento> formaPagamentoRoot = criteriaQuery.from(FormaPagamento.class);
		criteriaQuery.select(formaPagamentoRoot);
		TypedQuery<FormaPagamento> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}
	
	public FormaPagamento porId(Long id) {
		return manager.find(FormaPagamento.class, id);
	}

	@Transactional
	public boolean remover(FormaPagamento formaPagamento) {
		try {
			formaPagamento = porId(formaPagamento.getId());
			manager.remove(formaPagamento);
			manager.flush();
			return true;
		} catch (PersistenceException e) {
			FacesUtil.addErroMessage("A forma de pagamento não pode ser excluída.");
			return false;
		}
	}

	public FormaPagamento porDescricao(String descricao) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<FormaPagamento> criteriaQuery = builder.createQuery(FormaPagamento.class);
		Root<FormaPagamento> formaPagamentoRoot = criteriaQuery.from(FormaPagamento.class);
		criteriaQuery.select(formaPagamentoRoot);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(builder.equal(builder.lower(formaPagamentoRoot.get("descricao")), descricao.toLowerCase()));
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		TypedQuery<FormaPagamento> query = manager.createQuery(criteriaQuery);
		List<FormaPagamento> lista = query.getResultList();
		FormaPagamento formaPagamento = null;
		for (FormaPagamento p : lista) {
			formaPagamento = p;
		}
		return formaPagamento;
	}

	public FormaPagamento salvar(FormaPagamento formaPagamento) {
		return manager.merge(formaPagamento);
	}
	
}
