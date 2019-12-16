package br.com.sis.bean;

import java.io.BufferedReader;
import java.io.File;
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

import br.com.sis.entity.Configuracoes;
import br.com.sis.repository.ConfiguracoesRepository;
import br.com.sis.service.JavaMailService;
import br.com.sis.util.jsf.FacesUtil;

@Named
@ViewScoped
public class BackupBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private JavaMailService javaMailService;
	
	@Inject
	private ConfiguracoesRepository configuracoesRepository;
	
	private String sucess;
	private String errors;
	

	public String getSucess() {
		return sucess;
	}

	public void setSucess(String sucess) {
		this.sucess = sucess;
	}

	public String getErrors() {
		return errors;
	}

	public void setErrors(String errors) {
		this.errors = errors;
	}

	public void enviarBackup() {
		Configuracoes configuracoes = configuracoesRepository.configuracoesGerais();
		if (fazerBackup()) {
			String caminhoBackup = FacesUtil.localFiles() +  "db_prime_backup.sql";
			javaMailService.enviarEmail(configuracoes.getEmailRecebimentoSistema(), "Backup Base", "Backup da base", caminhoBackup);
			FacesUtil.addInfoMessage("Backup enviado com sucesso");
		}
	}
	
	public boolean fazerBackup() {
		String caminhoBackup = FacesUtil.localFiles();
		File diretorio = new File(caminhoBackup);
		if(!diretorio.exists()) {
			diretorio.mkdir();
		}
		caminhoBackup = FacesUtil.localFiles() +  "db_prime_backup.sql";
		Process proc = null;
		Map<String, String> result = new HashMap<>(); 
		try {
			proc = Runtime.getRuntime().exec("mysqldump --databases primedb -u primeroot -pprime > " + caminhoBackup);
			result.put("input", inputStreamToString(proc.getInputStream()));
			result.put("error", inputStreamToString(proc.getErrorStream()));
			FileWriter arq = new FileWriter(caminhoBackup);
			PrintWriter gravarArq = new PrintWriter(arq);
			gravarArq.printf(result.get("input"));
			arq.close();
			sucess = result.get("input");
			errors = result.get("error");
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
