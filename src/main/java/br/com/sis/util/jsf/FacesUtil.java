package br.com.sis.util.jsf;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class FacesUtil {

	public static boolean isPostBack() {
		return FacesContext.getCurrentInstance().isPostback();
	}

	public static boolean isNotPostBack() {
		return !isPostBack();
	}

	public static void addErroMessage(String summary, String message) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, message));
	}

	public static void addInfoMessage(String summary, String message) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, summary, message));
	}

	public static void addWarnMessage(String summary, String message) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_WARN, summary, message));
	}

	
	public static void addErroMessage(String message) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
	}

	public static void addInfoMessage(String message) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, message, message));
	}

	public static void addWarnMessage(String message) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_WARN, message, message));
	}

	
	public static String localFotos() {
		return FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/images/");
	}
	
	public static void redirecionarPagina(String pagina) {
        FacesContext context = FacesContext.getCurrentInstance();
        String url = context.getExternalContext().getRequestContextPath();
        try {
            context.getExternalContext().redirect(url + "/" + pagina);
        } catch (java.io.IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
	
	public static void redirecionarPaginaComMensagem(String pagina, String msg) {
        FacesContext context = FacesContext.getCurrentInstance();
        String url = context.getExternalContext().getRequestContextPath();
        try {
        	context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg));
        	context.getExternalContext().getFlash().setKeepMessages(true);
            context.getExternalContext().redirect(url + "/" + pagina);
        } catch (java.io.IOException ex) {
            System.out.println(ex.getMessage());
        }		
		
	}
	
	public static boolean validarTamanhoImagem(byte[] imagem) {
		return imagem.length < 999999;
	}
	
}
