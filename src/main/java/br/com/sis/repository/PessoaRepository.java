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
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import br.com.sis.entity.Pessoa;
import br.com.sis.enuns.Tipo;
import br.com.sis.repository.filter.PessoaFilter;
import br.com.sis.util.Utils;
import br.com.sis.util.jpa.Transactional;
import br.com.sis.util.jsf.FacesUtil;

public class PessoaRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Pessoa porId(Long id) {
		return manager.find(Pessoa.class, id);
	}

	public Pessoa salvar(Pessoa pessoa) {
		return manager.merge(pessoa);
	}

	@Transactional
	public boolean remover(Pessoa pessoa) {
		try {
			pessoa = porId(pessoa.getId());
			manager.remove(pessoa);
			manager.flush();
			return true;
		} catch (PersistenceException e) {
			FacesUtil.addErroMessage("Pessoa não pode ser excluída.");
			return false;
		}
	}

	public List<Pessoa> clientesFiltrados(PessoaFilter filter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Pessoa> criteriaQuery = builder.createQuery(Pessoa.class);

		List<Predicate> predicates = new ArrayList<>();

		Root<Pessoa> pessoaRoot = criteriaQuery.from(Pessoa.class);
		pessoaRoot.fetch("usuario", JoinType.LEFT);

		predicates.add(builder.or(builder.equal(pessoaRoot.get("tipo"), Tipo.CLIENTE),
				builder.equal(pessoaRoot.get("tipo"), Tipo.FUNC_CLI)));

		if (StringUtils.isNotBlank(filter.getNome())) {
			predicates.add(
					builder.like(builder.lower(pessoaRoot.get("nome")), "%" + filter.getNome().toLowerCase() + "%"));
		}

		if (StringUtils.isNotBlank(filter.getDocumentoReceita())) {
			predicates.add(builder.equal(pessoaRoot.get("documentoReceita"), Utils.removerCaracter(
					Utils.removerCaracter(Utils.removerCaracter(filter.getDocumentoReceita(), "."), "-"), "/")));
		}

		if (StringUtils.isNotBlank(filter.getRg())) {
			predicates.add(builder.equal(pessoaRoot.get("rg"), filter.getRg()));
		}

		if (filter.getAtivo() != null && filter.getAtivo().equals(true)) {
			predicates.add(builder.equal(pessoaRoot.get("ativo"), true));
		}

		if (filter.getInativo() != null && filter.getInativo().equals(true)) {
			predicates.add(builder.equal(pessoaRoot.get("ativo"), false));
		}

		criteriaQuery.select(pessoaRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(pessoaRoot.get("nome")));

		TypedQuery<Pessoa> query = manager.createQuery(criteriaQuery);
		return query.getResultList();

	}

	public Pessoa porCpf(String documentoReceita) {
		documentoReceita = Utils
				.removerCaracter(Utils.removerCaracter(Utils.removerCaracter(documentoReceita, "."), "-"), "/");
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Pessoa> criteriaQuery = builder.createQuery(Pessoa.class);

		List<Predicate> predicates = new ArrayList<>();

		Root<Pessoa> funcionarioRoot = criteriaQuery.from(Pessoa.class);

		predicates.add(builder.equal(funcionarioRoot.get("documentoReceita"), documentoReceita));
		criteriaQuery.select(funcionarioRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));

		TypedQuery<Pessoa> query = manager.createQuery(criteriaQuery);

		try {
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}

	}

	public List<Pessoa> funcionariosFiltrados(PessoaFilter filter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Pessoa> criteriaQuery = builder.createQuery(Pessoa.class);

		List<Predicate> predicates = new ArrayList<>();

		Root<Pessoa> pessoaRoot = criteriaQuery.from(Pessoa.class);
		pessoaRoot.fetch("usuario", JoinType.LEFT);

		predicates.add(builder.or(builder.equal(pessoaRoot.get("tipo"), Tipo.FUNCIONARIO),
				builder.equal(pessoaRoot.get("tipo"), Tipo.FUNC_CLI)));

		if (StringUtils.isNotBlank(filter.getNome())) {
			predicates.add(
					builder.like(builder.lower(pessoaRoot.get("nome")), "%" + filter.getNome().toLowerCase() + "%"));
		}

		if (StringUtils.isNotBlank(filter.getDocumentoReceita())) {
			predicates.add(builder.equal(pessoaRoot.get("documentoReceita"), Utils.removerCaracter(
					Utils.removerCaracter(Utils.removerCaracter(filter.getDocumentoReceita(), "."), "-"), "/")));
		}

		if (StringUtils.isNotBlank(filter.getRg())) {
			predicates.add(builder.equal(pessoaRoot.get("rg"), filter.getRg()));
		}

		if (filter.getAtivo().equals(true)) {
			predicates.add(builder.equal(pessoaRoot.get("ativo"), true));
		}

		if (filter.getInativo().equals(true)) {
			predicates.add(builder.equal(pessoaRoot.get("ativo"), false));
		}

		criteriaQuery.select(pessoaRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(pessoaRoot.get("nome")));

		TypedQuery<Pessoa> query = manager.createQuery(criteriaQuery);
		return query.getResultList();

	}

	public List<Pessoa> parceirosFiltrados(PessoaFilter filter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Pessoa> criteriaQuery = builder.createQuery(Pessoa.class);

		List<Predicate> predicates = new ArrayList<>();

		Root<Pessoa> pessoaRoot = criteriaQuery.from(Pessoa.class);
		pessoaRoot.fetch("usuario", JoinType.LEFT);

		predicates.add(builder.equal(pessoaRoot.get("tipo"), Tipo.PARCEIRO));

		if (StringUtils.isNotBlank(filter.getNome())) {
			predicates.add(
					builder.like(builder.lower(pessoaRoot.get("nome")), "%" + filter.getNome().toLowerCase() + "%"));
		}

		if (StringUtils.isNotBlank(filter.getDocumentoReceita())) {
			predicates.add(builder.equal(pessoaRoot.get("documentoReceita"), Utils.removerCaracter(
					Utils.removerCaracter(Utils.removerCaracter(filter.getDocumentoReceita(), "."), "-"), "/")));
		}

		if (StringUtils.isNotBlank(filter.getRg())) {
			predicates.add(builder.equal(pessoaRoot.get("rg"), filter.getRg()));
		}

		if (filter.getAtivo() != null && filter.getAtivo().equals(true)) {
			predicates.add(builder.equal(pessoaRoot.get("ativo"), true));
		}

		if (filter.getInativo() != null && filter.getInativo().equals(true)) {
			predicates.add(builder.equal(pessoaRoot.get("ativo"), false));
		}

		criteriaQuery.select(pessoaRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(pessoaRoot.get("nome")));

		TypedQuery<Pessoa> query = manager.createQuery(criteriaQuery);
		return query.getResultList();

	}

	public List<Pessoa> listaPessoasSemUsuario() {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Pessoa> criteriaQuery = builder.createQuery(Pessoa.class);
		Root<Pessoa> funcionarioRoot = criteriaQuery.from(Pessoa.class);
		funcionarioRoot.fetch("usuario", JoinType.LEFT);

		criteriaQuery.select(funcionarioRoot);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(builder.isNull(funcionarioRoot.get("usuario").get("id")));
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(funcionarioRoot.get("nome")));
		TypedQuery<Pessoa> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}

	public List<Pessoa> listAllFuncionarios() {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Pessoa> criteriaQuery = builder.createQuery(Pessoa.class);
		Root<Pessoa> pessoaRoot = criteriaQuery.from(Pessoa.class);
		pessoaRoot.fetch("usuario", JoinType.LEFT);
		criteriaQuery.select(pessoaRoot);
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(builder.or(builder.equal(pessoaRoot.get("tipo"), Tipo.FUNCIONARIO),
				builder.equal(pessoaRoot.get("tipo"), Tipo.FUNC_CLI)));
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(pessoaRoot.get("nome")));
		TypedQuery<Pessoa> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}

	public List<Pessoa> clientesAtivosPorNome(String nome) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Pessoa> criteriaQuery = builder.createQuery(Pessoa.class);
		List<Predicate> predicates = new ArrayList<>();
		Root<Pessoa> pessoaRoot = criteriaQuery.from(Pessoa.class);
		predicates.add(builder.or(builder.equal(pessoaRoot.get("tipo"), Tipo.CLIENTE),
				builder.equal(pessoaRoot.get("tipo"), Tipo.FUNC_CLI)));
		predicates.add(builder.like(builder.lower(pessoaRoot.get("nome")), nome.toLowerCase() + "%"));
		predicates.add(builder.equal(pessoaRoot.get("ativo"), true));
		criteriaQuery.select(pessoaRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(pessoaRoot.get("nome")));
		TypedQuery<Pessoa> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}

	public List<Pessoa> parceirosAtivosPorNome(String nome) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Pessoa> criteriaQuery = builder.createQuery(Pessoa.class);
		List<Predicate> predicates = new ArrayList<>();
		Root<Pessoa> pessoaRoot = criteriaQuery.from(Pessoa.class);
		predicates.add(builder.equal(pessoaRoot.get("tipo"), Tipo.PARCEIRO));
		predicates.add(builder.like(builder.lower(pessoaRoot.get("nome")), nome.toLowerCase() + "%"));
		predicates.add(builder.equal(pessoaRoot.get("ativo"), true));
		criteriaQuery.select(pessoaRoot);
		criteriaQuery.where(predicates.toArray(new Predicate[0]));
		criteriaQuery.orderBy(builder.asc(pessoaRoot.get("nome")));
		TypedQuery<Pessoa> query = manager.createQuery(criteriaQuery);
		return query.getResultList();
	}

}
