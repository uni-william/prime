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
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;

import br.com.sis.entity.Modelo;
import br.com.sis.entity.Movimentacao;
import br.com.sis.entity.Pessoa;
import br.com.sis.entity.Veiculo;
import br.com.sis.entity.vo.CarrosMaisVendidos;
import br.com.sis.entity.vo.TotalPorData;
import br.com.sis.entity.vo.TotalPorOperacao;
import br.com.sis.enuns.StatusVenda;
import br.com.sis.enuns.TipoOperacao;
import br.com.sis.repository.filter.MovimentacaoFilter;
import br.com.sis.util.jpa.Transactional;
import br.com.sis.util.jsf.FacesUtil;

public class MovimentacaoRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public List<Movimentacao> listAll() {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Movimentacao> criteriaQuery = builder.createQuery(Movimentacao.class);
		Root<Movimentacao> movimentacaoRoot = criteriaQuery.from(Movimentacao.class);
		movimentacaoRoot.fetch("veiculo", JoinType.INNER);
		movimentacaoRoot.fetch("funcionario", JoinType.INNER);
		movimentacaoRoot.fetch("cliente", JoinType.INNER);
		criteriaQuery.select(movimentacaoRoot);
		criteriaQuery.orderBy(builder.desc(movimentacaoRoot.get("data")));
		TypedQuery<Movimentacao> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Movimentacao> filtrados(MovimentacaoFilter filter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Movimentacao> criteriaQuery = builder.createQuery(Movimentacao.class);
		Root<Movimentacao> movimentacaoRoot = criteriaQuery.from(Movimentacao.class);
		Join<Veiculo, Movimentacao> modeloRoot = (Join) movimentacaoRoot.fetch("veiculo");
		Join<Modelo, Veiculo> fabricanteRoot = (Join) modeloRoot.fetch("modelo");
		fabricanteRoot.fetch("fabricante", JoinType.INNER);
		Join<Pessoa, Movimentacao> funcRoot = (Join) movimentacaoRoot.fetch("funcionario");
		funcRoot.fetch("usuario", JoinType.INNER);
		Join<Pessoa, Movimentacao> cliRoot = (Join) movimentacaoRoot.fetch("cliente");
		cliRoot.fetch("usuario", JoinType.LEFT);
		criteriaQuery.select(movimentacaoRoot);
		List<Predicate> predicates = new ArrayList<>();

		if (filter.getDtIni() != null) {
			predicates.add(builder.greaterThanOrEqualTo(movimentacaoRoot.get("data"), filter.getDtIni()));
		}

		if (filter.getDtFim() != null) {
			predicates.add(builder.lessThanOrEqualTo(movimentacaoRoot.get("data"), filter.getDtFim()));
		}

		if (filter.getTipoOperacao() != null) {
			predicates.add(builder.equal(movimentacaoRoot.get("tipoOperacao"), filter.getTipoOperacao()));
		}

		if (StringUtils.isNotBlank(filter.getNome())) {
			predicates
					.add(builder.like(builder.lower(cliRoot.get("nome")), "%" + filter.getNome().toLowerCase() + "%"));
		}

		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.desc(movimentacaoRoot.get("data")));
		TypedQuery<Movimentacao> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}
	
	public List<Movimentacao> movimentacoesPorCliente(Pessoa cliente) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Movimentacao> criteriaQuery = builder.createQuery(Movimentacao.class);
		Root<Movimentacao> movimentacaoRoot = criteriaQuery.from(Movimentacao.class);
		criteriaQuery.select(movimentacaoRoot);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(builder.equal(movimentacaoRoot.get("cliente"), cliente));
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.desc(movimentacaoRoot.get("data")));
		TypedQuery<Movimentacao> query = manager.createQuery(criteriaQuery);
		return query.getResultList();		
	}
	
	public List<Movimentacao> movimentacoesPorVeic(Veiculo veiculo) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Movimentacao> criteriaQuery = builder.createQuery(Movimentacao.class);
		Root<Movimentacao> movimentacaoRoot = criteriaQuery.from(Movimentacao.class);
		criteriaQuery.select(movimentacaoRoot);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(builder.equal(movimentacaoRoot.get("veiculo"), veiculo));
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.desc(movimentacaoRoot.get("data")));
		TypedQuery<Movimentacao> query = manager.createQuery(criteriaQuery);
		return query.getResultList();		
	}
	
	public boolean possuiVendaAtiva(Veiculo veiculo) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
		Root<Movimentacao> movimentacaoRoot = criteriaQuery.from(Movimentacao.class);
		criteriaQuery.select(builder.count(movimentacaoRoot));
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(builder.equal(movimentacaoRoot.get("veiculo"), veiculo));
		predicates.add(builder.equal(movimentacaoRoot.get("tipoOperacao"), TipoOperacao.VENDA));
		predicates.add(builder.equal(movimentacaoRoot.get("statusVenda"), StatusVenda.CONCLUIDA));
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		TypedQuery<Long> query = manager.createQuery(criteriaQuery);
		Long total = query.getSingleResult();
		return total > 0;		
	}	

	public Movimentacao porId(Long id) {
		return manager.find(Movimentacao.class, id);
	}

	@Transactional
	public boolean remover(Movimentacao Movimentacao) {
		try {
			Movimentacao = porId(Movimentacao.getId());
			manager.remove(Movimentacao);
			manager.flush();
			return true;
		} catch (PersistenceException e) {
			FacesUtil.addErroMessage("A Movimentação não pode ser excluída.");
			return false;
		}
	}

	public Movimentacao salvar(Movimentacao Movimentacao) {
		return manager.merge(Movimentacao);
	}

	public List<TotalPorOperacao> totalPorOperacaoJPA(Date dataInicial, Date dataFinal) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<TotalPorOperacao> criteriaQuery = builder.createQuery(TotalPorOperacao.class);
		Root<Movimentacao> movRoot = criteriaQuery.from(Movimentacao.class);

		// Select
		criteriaQuery.select(builder
				.construct(TotalPorOperacao.class, movRoot.get("tipoOperacao"),
						builder.count(movRoot.get("tipoOperacao")).alias("qtd"), builder.sum(movRoot.get("valor")))
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
		// Group By
		criteriaQuery.groupBy(movRoot.get("tipoOperacao"));

		// Order By
		Order order = builder.desc(builder.sum(movRoot.get("valor")));
		criteriaQuery.orderBy(order);

		TypedQuery<TotalPorOperacao> query = manager.createQuery(criteriaQuery);
		List<TotalPorOperacao> resultado = query.getResultList();
		return resultado;

	}

	public List<TotalPorData> totalPorData(Date dataInicial, Date dataFinal, TipoOperacao tipoOperacao) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<TotalPorData> criteriaQuery = builder.createQuery(TotalPorData.class);
		Root<Movimentacao> movRoot = criteriaQuery.from(Movimentacao.class);

		// Select
		criteriaQuery.select(builder
				.construct(TotalPorData.class, movRoot.get("data"), builder.sum(movRoot.get("valor"))).alias("valor"));

		// Where
		List<Predicate> predicates = new ArrayList<>();
		if (dataInicial != null) {
			predicates.add(builder.greaterThanOrEqualTo(movRoot.get("data"), dataInicial));
		}

		if (dataFinal != null) {
			predicates.add(builder.lessThanOrEqualTo(movRoot.get("data"), dataFinal));
		}

		predicates.add(builder.equal(movRoot.get("tipoOperacao"), tipoOperacao));
		predicates.add(builder.equal(movRoot.get("statusVenda"), StatusVenda.CONCLUIDA));

		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		// Group By
		criteriaQuery.groupBy(movRoot.get("data"));

		// Order By
		Order order = builder.desc(builder.sum(movRoot.get("valor")));
		criteriaQuery.orderBy(order);

		TypedQuery<TotalPorData> query = manager.createQuery(criteriaQuery);

		List<TotalPorData> resultado = query.getResultList();
		return resultado;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<CarrosMaisVendidos> carrosMaisVendidos(Date dataInicial, Date dataFinal) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<CarrosMaisVendidos> criteriaQuery = builder.createQuery(CarrosMaisVendidos.class);
		Root<Movimentacao> movRoot = criteriaQuery.from(Movimentacao.class);
		Join<Movimentacao, Veiculo> veiculoRoot = (Join) movRoot.join("veiculo");
		veiculoRoot.join("modelo", JoinType.INNER);
		Expression<Modelo> groupExp = veiculoRoot.get("modelo").as(Modelo.class);
		Expression<Long> countExp = builder.count(groupExp);
		CriteriaQuery<CarrosMaisVendidos> select = criteriaQuery.multiselect(groupExp, countExp);
		criteriaQuery.groupBy(groupExp);
		criteriaQuery.orderBy(builder.desc(countExp), builder.asc(veiculoRoot.get("modelo").get("descricao")));

		// Where
		List<Predicate> predicates = new ArrayList<>();
		if (dataInicial != null) {
			predicates.add(builder.greaterThanOrEqualTo(movRoot.get("data"), dataInicial));
		}

		if (dataFinal != null) {
			predicates.add(builder.lessThanOrEqualTo(movRoot.get("data"), dataFinal));
		}
		predicates.add(builder.equal(movRoot.get("tipoOperacao"), TipoOperacao.VENDA));
		predicates.add(builder.equal(movRoot.get("statusVenda"), StatusVenda.CONCLUIDA));
		criteriaQuery.where(predicates.toArray(new Predicate[0]));		
		TypedQuery<CarrosMaisVendidos> query = manager.createQuery(select);
		query.setFirstResult(0);
		query.setMaxResults(5);
		List<CarrosMaisVendidos> resultado = query.getResultList();
		return resultado;
		
	}

}
