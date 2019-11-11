package br.com.sis.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import br.com.sis.entity.Configuracoes;
import br.com.sis.service.NegocioException;
import br.com.sis.util.jpa.Transactional;

public class ConfiguracoesRepository implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public Configuracoes configuracoesGerais() {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Configuracoes> criteriaQuery = builder.createQuery(Configuracoes.class);
		Root<Configuracoes> configuracaoRoot = criteriaQuery.from(Configuracoes.class);
		criteriaQuery.select(configuracaoRoot);
		TypedQuery<Configuracoes> query = manager.createQuery(criteriaQuery);
		List<Configuracoes> lista = query.getResultList();
		Configuracoes configuracaoEmail = null;
		for (Configuracoes email : lista) {
			configuracaoEmail = email;
		}

		return configuracaoEmail;
	}

	public Configuracoes porId(Long id) {
		return manager.find(Configuracoes.class, id);
	}
	
	@Transactional
	public void remover(Configuracoes configuracaoEmail) {
		try {
			configuracaoEmail = porId(configuracaoEmail.getId());
			manager.remove(configuracaoEmail);
			manager.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Configuração não pode ser excluída.");
		}
	}
		
	public Configuracoes salvar(Configuracoes configuracaoEmail) {
		return manager.merge(configuracaoEmail);
	}
}
