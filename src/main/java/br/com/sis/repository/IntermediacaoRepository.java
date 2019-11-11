package br.com.sis.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.com.sis.entity.Intermediacao;
import br.com.sis.entity.Modelo;
import br.com.sis.entity.Pessoa;
import br.com.sis.entity.Veiculo;
import br.com.sis.entity.vo.TotalIntermediacao;
import br.com.sis.entity.vo.TotalPorData;
import br.com.sis.enuns.StatusVenda;
import br.com.sis.repository.filter.IntermediacaoFilter;
import br.com.sis.util.jpa.Transactional;
import br.com.sis.util.jsf.FacesUtil;

public class IntermediacaoRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Intermediacao porId(Long id) {
		return manager.find(Intermediacao.class, id);
	}
	
	@Transactional
	public boolean remover(Intermediacao intermediacao) {
		try {
			intermediacao = porId(intermediacao.getId());
			manager.remove(intermediacao);
			manager.flush();
			return true;
		} catch (PersistenceException e) {
			FacesUtil.addErroMessage("A intermediação não pode ser excluída.");
			return false;
		}
	}

	public Intermediacao salvar(Intermediacao intermediacao) {
		return manager.merge(intermediacao);
	}
	
	public List<Intermediacao> intermedicoesPorCliente(Pessoa cliente) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Intermediacao> criteriaQuery = builder.createQuery(Intermediacao.class);
		Root<Intermediacao> intermediacaoRoot = criteriaQuery.from(Intermediacao.class);
		criteriaQuery.select(intermediacaoRoot);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(builder.equal(intermediacaoRoot.get("cliente"), cliente));
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.desc(intermediacaoRoot.get("data")));
		TypedQuery<Intermediacao> query = manager.createQuery(criteriaQuery);
		return query.getResultList();		
	}
	
	public List<Intermediacao> intermedicoesPorVeiculo(Veiculo veiculo) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Intermediacao> criteriaQuery = builder.createQuery(Intermediacao.class);
		Root<Intermediacao> intermediacaoRoot = criteriaQuery.from(Intermediacao.class);
		criteriaQuery.select(intermediacaoRoot);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(builder.equal(intermediacaoRoot.get("veiculo"), veiculo));
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.desc(intermediacaoRoot.get("data")));
		TypedQuery<Intermediacao> query = manager.createQuery(criteriaQuery);
		return query.getResultList();		
	}	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Intermediacao> filtrados(IntermediacaoFilter filter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Intermediacao> criteriaQuery = builder.createQuery(Intermediacao.class);
		Root<Intermediacao> intermediacaoRoot = criteriaQuery.from(Intermediacao.class);
		Join<Veiculo, Intermediacao> modeloRoot = (Join) intermediacaoRoot.fetch("veiculo");
		Join<Modelo, Veiculo> fabricanteRoot = (Join) modeloRoot.fetch("modelo");
		fabricanteRoot.fetch("fabricante", JoinType.INNER);
		Join<Pessoa, Intermediacao> funcRoot = (Join) intermediacaoRoot.fetch("funcionario");
		funcRoot.fetch("usuario", JoinType.INNER);
		Join<Pessoa, Intermediacao> cliRoot = (Join) intermediacaoRoot.fetch("cliente");
		cliRoot.fetch("usuario", JoinType.LEFT);
		Join<Pessoa, Intermediacao> parcRoot = (Join) intermediacaoRoot.fetch("parceiro");
		parcRoot.fetch("usuario", JoinType.LEFT);
		criteriaQuery.select(intermediacaoRoot);
		List<Predicate> predicates = new ArrayList<>();

		if (filter.getDtIni() != null) {
			predicates.add(builder.greaterThanOrEqualTo(intermediacaoRoot.get("data"), filter.getDtIni()));
		}

		if (filter.getDtFim() != null) {
			predicates.add(builder.lessThanOrEqualTo(intermediacaoRoot.get("data"), filter.getDtFim()));
		}

		if (filter.getParceiro() != null) {
			predicates.add(builder.equal(intermediacaoRoot.get("parceiro"), filter.getParceiro()));
		}


		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.desc(intermediacaoRoot.get("data")));
		TypedQuery<Intermediacao> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}
	
	public List<TotalPorData> totalPorData(Date dataInicial, Date dataFinal) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<TotalPorData> criteriaQuery = builder.createQuery(TotalPorData.class);
		Root<Intermediacao> intRoot = criteriaQuery.from(Intermediacao.class);

		// Select
		criteriaQuery.select(builder
				.construct(TotalPorData.class, intRoot.get("data"), builder.sum(intRoot.get("valorFinanciado"))).alias("valor"));

		// Where
		List<Predicate> predicates = new ArrayList<>();
		if (dataInicial != null) {
			predicates.add(builder.greaterThanOrEqualTo(intRoot.get("data"), dataInicial));
		}

		if (dataFinal != null) {
			predicates.add(builder.lessThanOrEqualTo(intRoot.get("data"), dataFinal));
		}
		
		predicates.add(builder.equal(intRoot.get("statusVenda"), StatusVenda.CONCLUIDA));

		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		// Group By
		criteriaQuery.groupBy(intRoot.get("data"));

		// Order By
		Order order = builder.desc(builder.sum(intRoot.get("valorFinanciado")));
		criteriaQuery.orderBy(order);

		TypedQuery<TotalPorData> query = manager.createQuery(criteriaQuery);

		List<TotalPorData> resultado = query.getResultList();
		return resultado;

	}
	
	public List<TotalIntermediacao> totalIntermedicaoJPA(Date dataInicial, Date dataFinal) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<TotalIntermediacao> criteriaQuery = builder.createQuery(TotalIntermediacao.class);
		Root<Intermediacao> movRoot = criteriaQuery.from(Intermediacao.class);

		// Select
		criteriaQuery.select(builder
				.construct(TotalIntermediacao.class,
						builder.count(movRoot.get("id")).alias("qtd"), builder.sum(movRoot.get("valorFinanciado")))
				.alias("valor"));

		// Where
		List<Predicate> predicates = new ArrayList<>();
		if (dataInicial != null) {
			predicates.add(builder.greaterThanOrEqualTo(movRoot.get("data"), dataInicial));
		}

		if (dataFinal != null) {
			predicates.add(builder.lessThanOrEqualTo(movRoot.get("data"), dataFinal));
		}
		
		predicates.add(builder.equal(movRoot.get("statusVenda"), StatusVenda.CONCLUIDA));

		criteriaQuery.where(predicates.toArray(new Predicate[0]));


		TypedQuery<TotalIntermediacao> query = manager.createQuery(criteriaQuery);
		List<TotalIntermediacao> resultado = query.getResultList();
		return resultado;

	}	
	

}
