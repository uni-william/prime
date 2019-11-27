package br.com.sis.repository;

import java.io.Serializable;
import java.math.BigDecimal;
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

import br.com.sis.entity.Aluguel;
import br.com.sis.entity.PagamentoSemanal;
import br.com.sis.util.jpa.Transactional;
import br.com.sis.util.jsf.FacesUtil;

public class PagamentoSemanalRepository implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;


	public PagamentoSemanal porId(Long id) {
		return manager.find(PagamentoSemanal.class, id);
	}
	
	public List<PagamentoSemanal> pagamentosPorContrato(Aluguel aluguel) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<PagamentoSemanal> criteriaQuery = builder.createQuery(PagamentoSemanal.class);
		Root<PagamentoSemanal> pgtoRoot = criteriaQuery.from(PagamentoSemanal.class);
		criteriaQuery.select(pgtoRoot);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(builder.equal(pgtoRoot.get("aluguel"), aluguel));
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(pgtoRoot.get("dataPagto")));
		TypedQuery<PagamentoSemanal> query = manager.createQuery(criteriaQuery);
		return query.getResultList();		
	}
	
	public BigDecimal totalPagoSemana(Aluguel aluguel) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<BigDecimal> criteriaQuery = builder.createQuery(BigDecimal.class);
		Root<PagamentoSemanal> pgtoRoot = criteriaQuery.from(PagamentoSemanal.class);
		criteriaQuery.select(builder.sum(pgtoRoot.<BigDecimal>get("valorPago")));
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(builder.greaterThan(pgtoRoot.get("dataPagto"), pgtoRoot.get("aluguel").get("dataInicio")));
		predicates.add(builder.equal(pgtoRoot.get("aluguel"), aluguel));
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		TypedQuery<BigDecimal> query = manager.createQuery(criteriaQuery);
		return query.getSingleResult();		
		
	}

	@Transactional
	public boolean remover(PagamentoSemanal pagamentoSemanal) {
		try {
			pagamentoSemanal = porId(pagamentoSemanal.getId());
			manager.remove(pagamentoSemanal);
			manager.flush();
			return true;
		} catch (PersistenceException e) {
			FacesUtil.addErroMessage("O Pagamento Semanal não pode ser excluído.");
			return false;
		}
	}

	public PagamentoSemanal salvar(PagamentoSemanal pagamentoSemanal) {
		return manager.merge(pagamentoSemanal);
	}
	
}
