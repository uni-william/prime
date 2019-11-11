package br.com.sis.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;

import br.com.sis.entity.Modelo;
import br.com.sis.entity.Veiculo;
import br.com.sis.enuns.StatusVeiculo;
import br.com.sis.repository.filter.VeiculoFilter;
import br.com.sis.util.jpa.Transactional;
import br.com.sis.util.jsf.FacesUtil;

public class VeiculoRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public List<Veiculo> listAll() {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Veiculo> criteriaQuery = builder.createQuery(Veiculo.class);
		Root<Veiculo> veiculoRoot = criteriaQuery.from(Veiculo.class);
		criteriaQuery.select(veiculoRoot);
		TypedQuery<Veiculo> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Veiculo> filtrados(VeiculoFilter filter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Veiculo> criteriaQuery = builder.createQuery(Veiculo.class);
		Root<Veiculo> veiculoRoot = criteriaQuery.from(Veiculo.class);

		Join<Modelo, Veiculo> modeloRoot = (Join) veiculoRoot.fetch("modelo");
		modeloRoot.fetch("fabricante", JoinType.INNER);

		criteriaQuery.select(veiculoRoot);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(builder.equal(veiculoRoot.get("veiculoDeParceiro"), false));
		predicates.add(builder.equal(veiculoRoot.get("veiculoAluguel"), false));
		if (StringUtils.isNotBlank(filter.getChassi())) {
			predicates.add(builder.equal(veiculoRoot.get("chassi"), filter.getChassi()));
		}
		if (StringUtils.isNotBlank(filter.getPlaca())) {
			predicates.add(builder.equal(veiculoRoot.get("placa"), filter.getPlaca()));
		}
		if (StringUtils.isNotBlank(filter.getRenavan())) {
			predicates.add(builder.equal(veiculoRoot.get("renavan"), filter.getRenavan()));
		}
		if (filter.getModelo() != null) {
			predicates.add(builder.equal(veiculoRoot.get("modelo"), filter.getModelo()));
		}
		if (filter.getStatusVeiculo() != null) {
			predicates.add(builder.equal(veiculoRoot.get("statusVeiculo"), filter.getStatusVeiculo()));
		}

		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		TypedQuery<Veiculo> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Veiculo> filtradosDeParceiros(VeiculoFilter filter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Veiculo> criteriaQuery = builder.createQuery(Veiculo.class);
		Root<Veiculo> veiculoRoot = criteriaQuery.from(Veiculo.class);

		Join<Modelo, Veiculo> modeloRoot = (Join) veiculoRoot.fetch("modelo");
		modeloRoot.fetch("fabricante", JoinType.INNER);

		criteriaQuery.select(veiculoRoot);
		List<Predicate> predicates = new ArrayList<>();

		predicates.add(builder.equal(veiculoRoot.get("veiculoDeParceiro"), true));
		predicates.add(builder.equal(veiculoRoot.get("veiculoAluguel"), false));

		if (StringUtils.isNotBlank(filter.getChassi())) {
			predicates.add(builder.equal(veiculoRoot.get("chassi"), filter.getChassi()));
		}
		if (StringUtils.isNotBlank(filter.getPlaca())) {
			predicates.add(builder.equal(veiculoRoot.get("placa"), filter.getPlaca()));
		}
		if (StringUtils.isNotBlank(filter.getRenavan())) {
			predicates.add(builder.equal(veiculoRoot.get("renavan"), filter.getRenavan()));
		}
		if (filter.getModelo() != null) {
			predicates.add(builder.equal(veiculoRoot.get("modelo"), filter.getModelo()));
		}
		if (filter.getStatusVeiculo() != null) {
			predicates.add(builder.equal(veiculoRoot.get("statusVeiculo"), filter.getStatusVeiculo()));
		}

		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		TypedQuery<Veiculo> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Veiculo> filtradosAluguel(VeiculoFilter filter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Veiculo> criteriaQuery = builder.createQuery(Veiculo.class);
		Root<Veiculo> veiculoRoot = criteriaQuery.from(Veiculo.class);

		Join<Modelo, Veiculo> modeloRoot = (Join) veiculoRoot.fetch("modelo");
		modeloRoot.fetch("fabricante", JoinType.INNER);

		criteriaQuery.select(veiculoRoot);
		List<Predicate> predicates = new ArrayList<>();

		predicates.add(builder.equal(veiculoRoot.get("veiculoDeParceiro"), false));
		predicates.add(builder.equal(veiculoRoot.get("veiculoAluguel"), true));

		if (StringUtils.isNotBlank(filter.getChassi())) {
			predicates.add(builder.equal(veiculoRoot.get("chassi"), filter.getChassi()));
		}
		if (StringUtils.isNotBlank(filter.getPlaca())) {
			predicates.add(builder.equal(veiculoRoot.get("placa"), filter.getPlaca()));
		}
		if (StringUtils.isNotBlank(filter.getRenavan())) {
			predicates.add(builder.equal(veiculoRoot.get("renavan"), filter.getRenavan()));
		}
		if (filter.getModelo() != null) {
			predicates.add(builder.equal(veiculoRoot.get("modelo"), filter.getModelo()));
		}

		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		TypedQuery<Veiculo> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}	

	public Veiculo porId(Long id) {
		return manager.find(Veiculo.class, id);
	}

	@Transactional
	public boolean remover(Veiculo veiculo) {
		try {
			veiculo = porId(veiculo.getId());
			manager.remove(veiculo);
			manager.flush();
			return true;
		} catch (PersistenceException e) {
			FacesUtil.addErroMessage("O veiculo não pode ser excluído.");
			return false;
		}
	}

	public Veiculo porChassi(String chassi) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Veiculo> criteriaQuery = builder.createQuery(Veiculo.class);
		Root<Veiculo> veiculoRoot = criteriaQuery.from(Veiculo.class);
		criteriaQuery.select(veiculoRoot);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(builder.equal(builder.lower(veiculoRoot.get("chassi")), chassi.toLowerCase()));
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		TypedQuery<Veiculo> query = manager.createQuery(criteriaQuery);
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}

	}

	public Veiculo porPlaca(String placa) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Veiculo> criteriaQuery = builder.createQuery(Veiculo.class);
		Root<Veiculo> veiculoRoot = criteriaQuery.from(Veiculo.class);
		criteriaQuery.select(veiculoRoot);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(builder.equal(builder.lower(veiculoRoot.get("placa")), placa.toLowerCase()));
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		TypedQuery<Veiculo> query = manager.createQuery(criteriaQuery);
		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Veiculo> listaDeCarrosNoPatio() {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Veiculo> criteriaQuery = builder.createQuery(Veiculo.class);
		Root<Veiculo> veiculoRoot = criteriaQuery.from(Veiculo.class);
		Join<Modelo, Veiculo> modeloRoot = (Join) veiculoRoot.fetch("modelo");
		modeloRoot.fetch("fabricante", JoinType.INNER);
		criteriaQuery.select(veiculoRoot);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(builder.equal(veiculoRoot.get("veiculoDeParceiro"), false));
		predicates.add(builder.equal(veiculoRoot.get("veiculoAluguel"), false));
		predicates.add(builder.equal(veiculoRoot.get("statusVeiculo"), StatusVeiculo.PATIO));
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(modeloRoot.get("fabricante")), builder.asc(modeloRoot.get("descricao")));
		TypedQuery<Veiculo> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}

	public Veiculo salvar(Veiculo veiculo) {
		return manager.merge(veiculo);
	}

}
