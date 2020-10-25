package br.com.sis.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import br.com.sis.entity.Aluguel;
import br.com.sis.entity.Modelo;
import br.com.sis.entity.Pessoa;
import br.com.sis.entity.Veiculo;
import br.com.sis.enuns.StatusAluguel;
import br.com.sis.repository.filter.AluguelFilter;
import br.com.sis.util.Utils;
import br.com.sis.util.jpa.Transactional;
import br.com.sis.util.jsf.FacesUtil;

public class AluguelRepository implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Aluguel> filtrados(AluguelFilter filter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Aluguel> criteriaQuery = builder.createQuery(Aluguel.class);
		Root<Aluguel> aluguelRoot = criteriaQuery.from(Aluguel.class);
		Join<Veiculo, Aluguel> modeloRoot = (Join) aluguelRoot.fetch("veiculo");
		Join<Modelo, Veiculo> fabricanteRoot = (Join) modeloRoot.fetch("modelo");
		fabricanteRoot.fetch("fabricante", JoinType.INNER);
		Join<Pessoa, Aluguel> funcRoot = (Join) aluguelRoot.fetch("funcionario");
		funcRoot.fetch("usuario", JoinType.INNER);
		Join<Pessoa, Aluguel> cliRoot = (Join) aluguelRoot.fetch("cliente");
		cliRoot.fetch("usuario", JoinType.LEFT);
		
		
		criteriaQuery.select(aluguelRoot);
		List<Predicate> predicates = new ArrayList<>();
		if (filter.getDtIni() != null) {
			predicates.add(builder.greaterThanOrEqualTo(aluguelRoot.get("dataInicio"), filter.getDtIni()));
		}

		if (filter.getDtFim() != null) {
			predicates.add(builder.lessThanOrEqualTo(aluguelRoot.get("dataInicio"), filter.getDtFim()));
		}
		if (StringUtils.isNotBlank(filter.getDocumentoReceita())) {
			predicates.add(builder.equal(cliRoot.get("documentoReceita"), Utils.removerCaracter(
					Utils.removerCaracter(Utils.removerCaracter(filter.getDocumentoReceita(), "."), "-"), "/")));
		}		
		if (StringUtils.isNotBlank(filter.getNome())) {
			predicates
					.add(builder.like(builder.lower(cliRoot.get("nome")), "%" + filter.getNome().toLowerCase() + "%"));
		}		
		if (filter.getStatusAluguel() != null) {
			predicates.add(builder.equal(aluguelRoot.get("statusAluguel"), filter.getStatusAluguel()));
		}
		
		if (filter.getPagtoSemanal() != null) {
			predicates.add(builder.equal(aluguelRoot.get("pagamentoSemanal"), filter.getPagtoSemanal()));
		}
		
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		TypedQuery<Aluguel> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Aluguel> topContratosAVencerVencidos(int top, int dias) {
		Calendar dt = Calendar.getInstance();
		Calendar dtf = Calendar.getInstance();
		dt.add(Calendar.DAY_OF_MONTH, dias);
		dtf.setTime(dt.getTime());
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Aluguel> criteriaQuery = builder.createQuery(Aluguel.class);
		Root<Aluguel> aluguelRoot = criteriaQuery.from(Aluguel.class);
		Join<Veiculo, Aluguel> modeloRoot = (Join) aluguelRoot.fetch("veiculo");
		Join<Modelo, Veiculo> fabricanteRoot = (Join) modeloRoot.fetch("modelo");
		fabricanteRoot.fetch("fabricante", JoinType.INNER);
		Join<Pessoa, Aluguel> funcRoot = (Join) aluguelRoot.fetch("funcionario");
		funcRoot.fetch("usuario", JoinType.INNER);
		Join<Pessoa, Aluguel> cliRoot = (Join) aluguelRoot.fetch("cliente");
		cliRoot.fetch("usuario", JoinType.LEFT);
		
		criteriaQuery.select(aluguelRoot);
		
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(builder.equal(aluguelRoot.get("statusAluguel"), StatusAluguel.ABERTO));
		predicates.add(builder.lessThanOrEqualTo(aluguelRoot.get("dataPrevista"), dtf.getTime()));
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(aluguelRoot.get("dataPrevista")));
		TypedQuery<Aluguel> query = manager.createQuery(criteriaQuery).setMaxResults(top);
		return query.getResultList();		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Aluguel> topPagamentosSemanalAVencerVencidos(int top, int dias) {
		Calendar dt = Calendar.getInstance();
		Calendar dtf = Calendar.getInstance();
		dt.add(Calendar.DAY_OF_MONTH, dias);
		dtf.setTime(dt.getTime());
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Aluguel> criteriaQuery = builder.createQuery(Aluguel.class);
		Root<Aluguel> aluguelRoot = criteriaQuery.from(Aluguel.class);
		Join<Veiculo, Aluguel> modeloRoot = (Join) aluguelRoot.fetch("veiculo");
		Join<Modelo, Veiculo> fabricanteRoot = (Join) modeloRoot.fetch("modelo");
		fabricanteRoot.fetch("fabricante", JoinType.INNER);
		Join<Pessoa, Aluguel> funcRoot = (Join) aluguelRoot.fetch("funcionario");
		funcRoot.fetch("usuario", JoinType.INNER);
		Join<Pessoa, Aluguel> cliRoot = (Join) aluguelRoot.fetch("cliente");
		cliRoot.fetch("usuario", JoinType.LEFT);
		
		criteriaQuery.select(aluguelRoot);
		
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(builder.equal(aluguelRoot.get("statusAluguel"), StatusAluguel.ABERTO));
		predicates.add(builder.equal(aluguelRoot.get("pagamentoSemanal"), true));
		predicates.add(builder.lessThanOrEqualTo(aluguelRoot.get("dataProximoPagamento"), dtf.getTime()));
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(aluguelRoot.get("dataProximoPagamento")));
		TypedQuery<Aluguel> query = manager.createQuery(criteriaQuery).setMaxResults(top);
		return query.getResultList();		
	}	
	

	public Aluguel porId(Long id) {
		return manager.find(Aluguel.class, id);
	}

	@Transactional
	public boolean remover(Aluguel aluguel) {
		try {
			aluguel = porId(aluguel.getId());
			manager.remove(aluguel);
			manager.flush();
			return true;
		} catch (PersistenceException e) {
			FacesUtil.addErroMessage("O aluguel não pode ser excluído.");
			return false;
		}
	}

	public Aluguel salvar(Aluguel aluguel) {
		return manager.merge(aluguel);
	}
	
}
