package br.com.sis.service;

import java.io.Serializable;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.inject.Inject;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import br.com.sis.entity.Configuracoes;
import br.com.sis.repository.ConfiguracoesRepository;
import br.com.sis.util.Utils;

public class JavaMailService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ConfiguracoesRepository configuracoesEmail;

	private Configuracoes configuracaoEmail;

	public boolean enviarEmail(String emailDestinatario, String subject, String content, String anexo) {
		configuracaoEmail = configuracoesEmail.configuracoesGerais();

		String senha = Utils.decripto(configuracaoEmail.getSenhaUsuarioEmail());

		Properties props = new Properties();
		props.put("mail.smtp.host", configuracaoEmail.getHost());
		props.put("mail.smtp.socketFactory.port", configuracaoEmail.getPorta());
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		if (configuracaoEmail.isSslOnConection()) {
			props.put("mail.smtp.auth", "true");
		} else {
			props.put("mail.smtp.auth", "false");
		}
		
		props.put("mail.smtp.port", configuracaoEmail.getPorta());

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(configuracaoEmail.getUsuarioEnviaEmail(), senha);
			}
		});

		session.setDebug(true);

		try {
			Message message = new MimeMessage(session);
			MimeBodyPart mbpText = new MimeBodyPart();
			MimeBodyPart mbp = new MimeBodyPart();
			Multipart mp = new MimeMultipart();

			message.setFrom(new InternetAddress(configuracaoEmail.getEmailEnvio()));
			Address[] toUser = InternetAddress // Destinatário(s)
					.parse(emailDestinatario);
			message.setRecipients(Message.RecipientType.TO, toUser);
			message.setSubject(subject);// Assunto
			mbpText.setText(content);
			mp.addBodyPart(mbpText);

			if (anexo != null) {
				//enviando anexo
				DataSource fds = new FileDataSource(anexo);
				mbp.setDataHandler(new DataHandler(fds));
				mbp.setFileName(fds.getName());   
				mp.addBodyPart(mbp);
			}
			message.setContent(mp);

			Transport.send(message);
			return true;

		} catch (MessagingException e) {
			e.getMessage();
			return false;
		}
	}

}
