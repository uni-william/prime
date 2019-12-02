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

import br.com.sis.entity.Aluguel;
import br.com.sis.entity.Renovacao;
import br.com.sis.util.jpa.Transactional;
import br.com.sis.util.jsf.FacesUtil;

public class RenovacaoRepository implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;


	public Renovacao porId(Long id) {
		return manager.find(Renovacao.class, id);
	}
	
	public List<Renovacao> renovacoesPorContrato(Aluguel aluguel) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Renovacao> criteriaQuery = builder.createQuery(Renovacao.class);
		Root<Renovacao> pgtoRoot = criteriaQuery.from(Renovacao.class);
		criteriaQuery.select(pgtoRoot);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(builder.equal(pgtoRoot.get("aluguel"), aluguel));
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(pgtoRoot.get("dataInicioAnterior")));
		TypedQuery<Renovacao> query = manager.createQuery(criteriaQuery);
		return query.getResultList();		
	}
	
	@Transactional
	public boolean remover(Renovacao renovacao) {
		try {
			renovacao = porId(renovacao.getId());
			manager.remove(renovacao);
			manager.flush();
			return true;
		} catch (PersistenceException e) {
			FacesUtil.addErroMessage("A renovação não pode ser excluída.");
			return false;
		}
	}

	public Renovacao salvar(Renovacao renovacao) {
		return manager.merge(renovacao);
	}
	
}
