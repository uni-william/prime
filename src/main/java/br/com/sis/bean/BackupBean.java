package br.com.sis.bean;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UnknownFormatConversionException;

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
		File arq = new File(caminhoBackup);
		if (arq.exists())
			arq.delete();
		Process proc = null;
		 
		try {
			proc = Runtime.getRuntime().exec("mysqldump --databases primedb -u primeroot -pprime > " + caminhoBackup);

			Path path = Paths.get(caminhoBackup);
			String line = inputStreamToString(proc.getInputStream());
			Files.createFile(path);
		    //printa o retorno
		   	Files.write(path, line.getBytes(), StandardOpenOption.APPEND);
			return true;
		} catch (IOException | UnknownFormatConversionException e) {
			FacesUtil.addErroMessage(e.getMessage() != null ? e.getMessage(): e.getCause().toString());		
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
