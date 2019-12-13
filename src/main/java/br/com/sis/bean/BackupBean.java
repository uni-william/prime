package br.com.sis.bean;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import br.com.sis.service.JavaMailService;
import br.com.sis.util.jsf.FacesUtil;

@Named
@ViewScoped
public class BackupBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private JavaMailService javaMailService;

	@NotEmpty
	@Email
	private String destinatario;

	@NotEmpty
	private String subject;
	@NotEmpty
	private String content;

	public String getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public void enviarBackup() {
		if (fazerBackup()) {
			String caminhoBackup = FacesUtil.localFiles() +  "db_prime_backup.sql";
			javaMailService.enviarEmail(destinatario, subject, content, caminhoBackup);
			FacesUtil.addInfoMessage("Backup enviado com sucesso");
		}
	}
	
	public boolean fazerBackup() {
		String caminhoBackup = FacesUtil.localFiles() +  "db_prime_backup.sql";
		Process proc = null;
		Map<String, String> result = new HashMap<>(); 
		try {
			proc = Runtime.getRuntime().exec("mysqldump --databases primedb -u primeroot -pprime > " + caminhoBackup);
			result.put("input", inputStreamToString(proc.getInputStream()));
			FileWriter arq = new FileWriter(caminhoBackup);
			PrintWriter gravarArq = new PrintWriter(arq);
			gravarArq.printf(result.get("input"));
			arq.close();
			return true;
		} catch (IOException e) {
			FacesUtil.addErroMessage(e.getMessage());
			return false;
		}
	}
	
	private String inputStreamToString(InputStream isr) {
	     try {
	        BufferedReader br = new BufferedReader(new InputStreamReader(isr));
	        StringBuilder sb = new StringBuilder();
	        String s = null;
	        while ((s = br.readLine()) != null) {
	                  sb.append(s + "\n");
	        }
	        return sb.toString();
	     } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	        return null;
	     }
	  }	

}
