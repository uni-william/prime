package br.com.sis.bean;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.hibernate.validator.constraints.NotEmpty;

import br.com.sis.service.UsuarioService;
import br.com.sis.util.jsf.FacesUtil;

@Named
@ViewScoped
public class EsqueciSenhaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private UsuarioService usuarioService;

	private String login;

	@NotEmpty
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public void redefinirSenha() {
		if (usuarioService.esqueciSenha(login) != null) {
			FacesUtil.redirecionarPagina("/Login.xhtml");
		} else {
			FacesUtil.addErroMessage("Erro ao redefinir senha de usuário");
		}

	}

}
