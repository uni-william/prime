package br.com.sis.service;

import java.io.Serializable;

import javax.inject.Inject;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;

import br.com.sis.entity.Configuracoes;
import br.com.sis.repository.ConfiguracoesRepository;
import br.com.sis.util.Utils;

public class CommonsEmailService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ConfiguracoesRepository configuracoesEmail;

	private Configuracoes configuracaoEmail;

	public boolean enviarEmail(String nomeDestinatario, String emailDestinatario, String subject, String content,
			boolean formatoHtml) {

		configuracaoEmail = configuracoesEmail.configuracoesGerais();
		
		String senha = Utils.decripto(configuracaoEmail.getSenhaUsuarioEmail());

		Email email;
		if (formatoHtml == true) {
			email = new HtmlEmail();
		} else {
			email = new SimpleEmail();
		}

		email.setHostName(configuracaoEmail.getHost());

		try {
			email.setAuthentication(configuracaoEmail.getUsuarioEnviaEmail(), senha);
			email.setSSLOnConnect(configuracaoEmail.isSslOnConection());
			email.setSmtpPort(Integer.parseInt(configuracaoEmail.getPorta()));
			email.setSslSmtpPort(configuracaoEmail.getPorta());
			email.setStartTLSRequired(configuracaoEmail.isTlsRequired());
			email.setFrom(configuracaoEmail.getEmailEnvio());
			email.setDebug(true);
			email.addTo(emailDestinatario, nomeDestinatario);
			email.setSubject(subject);
			email.setMsg(content);
			email.send();
			return true;
		} catch (EmailException e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

}
