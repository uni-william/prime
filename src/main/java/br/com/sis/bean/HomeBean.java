package br.com.sis.bean;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import br.com.sis.entity.Usuario;
import br.com.sis.repository.UsuarioRepository;
import br.com.sis.security.Seguranca;
import br.com.sis.util.jsf.FacesUtil;

@Named
@ViewScoped
public class HomeBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private HttpServletRequest request;

	@Inject
	private Seguranca seguranca;
	
	@Inject
	private UsuarioRepository usuarioRepository;	

	public void inicializar() {
		if ("true".equals(request.getParameter("trocada"))) {

		} else {
			Usuario usuario = usuarioRepository.porId(seguranca.getUsuarioLogado().getUsuario().getId());
			if (usuario.isTrocarSenha()) {
				FacesUtil.redirecionarPagina("/TrocaSenha.xhtml?troca=true");
			}
		}
	}
}
