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

import br.com.sis.entity.Manutencao;
import br.com.sis.entity.Veiculo;
import br.com.sis.util.jpa.Transactional;
import br.com.sis.util.jsf.FacesUtil;

public class ManutencaoRepository implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public List<Manutencao> listAll() {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Manutencao> criteriaQuery = builder.createQuery(Manutencao.class);
		Root<Manutencao> ManutencaoRoot = criteriaQuery.from(Manutencao.class);
		criteriaQuery.select(ManutencaoRoot);
		TypedQuery<Manutencao> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}
	
	public List<Manutencao> manutencoesVeiculo(Veiculo veiculo) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Manutencao> criteriaQuery = builder.createQuery(Manutencao.class);
		Root<Manutencao> manutencaoRoot = criteriaQuery.from(Manutencao.class);
		criteriaQuery.select(manutencaoRoot);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(builder.equal(manutencaoRoot.get("veiculo"), veiculo));
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(manutencaoRoot.get("data")));
		TypedQuery<Manutencao> query = manager.createQuery(criteriaQuery);
		return query.getResultList();		
	}
	
	public Manutencao porId(Long id) {
		return manager.find(Manutencao.class, id);
	}
	
	public BigDecimal totalManutencoesPorVeiculo(Veiculo veiculo) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<BigDecimal> criteriaQuery = builder.createQuery(BigDecimal.class);
		Root<Manutencao> manRoot = criteriaQuery.from(Manutencao.class);
		criteriaQuery.select(builder.sum(manRoot.<BigDecimal>get("valor")));
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(builder.equal(manRoot.get("veiculo"), veiculo));
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		TypedQuery<BigDecimal> query = manager.createQuery(criteriaQuery);
		BigDecimal total = query.getSingleResult();
		return total;
		
	}
	
	public BigDecimal totalManutencoesPorVeiculoId(Long id) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<BigDecimal> criteriaQuery = builder.createQuery(BigDecimal.class);
		Root<Manutencao> manRoot = criteriaQuery.from(Manutencao.class);
		criteriaQuery.select(builder.sum(manRoot.<BigDecimal>get("valor")));
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(builder.equal(manRoot.get("veiculo").get("id"), id));
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		TypedQuery<BigDecimal> query = manager.createQuery(criteriaQuery);
		BigDecimal total = query.getSingleResult();
		return total;
		
	}	

	@Transactional
	public boolean remover(Manutencao manutencao) {
		try {
			manutencao = porId(manutencao.getId());
			manager.remove(manutencao);
			manager.flush();
			return true;
		} catch (PersistenceException e) {
			FacesUtil.addErroMessage("O Manutenção não pode ser excluída.");
			return false;
		}
	}

	public Manutencao salvar(Manutencao manutencao) {
		return manager.merge(manutencao);
	}
	
}
