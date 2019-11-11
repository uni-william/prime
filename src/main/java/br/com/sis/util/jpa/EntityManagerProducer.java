package br.com.sis.util.jpa;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.Session;

@ApplicationScoped
public class EntityManagerProducer {

	private EntityManagerFactory factory;
	
	/*
	  Vai sair daqui --> Session session = this.manager.unwrap(Session.class);
	  para cá agora nas classe de Repositoty ou DAO --> Session session = (Session) manager;  
	 */

	public EntityManagerProducer() {
		this.factory = Persistence.createEntityManagerFactory("primeweb");
	}

	@Produces
	@RequestScoped
	public Session create() {
		return (Session) factory.createEntityManager();
	}

	public void close(@Disposes Session manager) {
		manager.close();
	}
	
}
