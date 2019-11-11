package br.com.sis.bean;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.sis.entity.Usuario;
import br.com.sis.repository.UsuarioRepository;
import br.com.sis.security.Seguranca;
import br.com.sis.service.UsuarioService;
import br.com.sis.util.jsf.FacesUtil;

@Named
@ViewScoped
public class TrocaSenhaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private HttpServletRequest request;

	@Inject
	private Seguranca seguranca;

	@Inject
	private UsuarioRepository usuarioRepository;

	@Inject
	private UsuarioService usuarioService;

	private String novaSenha;
	private String confirmaSenha;

	private Usuario usuario;

	@NotEmpty
	public String getNovaSenha() {
		return novaSenha;
	}

	public void setNovaSenha(String novaSenha) {
		this.novaSenha = novaSenha;
	}

	@NotEmpty
	public String getConfirmaSenha() {
		return confirmaSenha;
	}

	public void setConfirmaSenha(String confirmaSenha) {
		this.confirmaSenha = confirmaSenha;
	}

	public void inicializar() {
		usuario = seguranca.getUsuarioLogado().getUsuario();
		if ("true".equals(request.getParameter("troca"))) {
			FacesUtil.addWarnMessage("Sua senha deve ser alterada!");
		}
	}

	public void trocarSenha() {
		usuario = usuarioRepository.porId(usuario.getId());

		String senhaCripto = new BCryptPasswordEncoder().encode(novaSenha);
		usuario.setPassword(senhaCripto);
		usuario.setTrocarSenha(false);
		usuario = usuarioService.salvar(usuario);
		if (usuario != null) {
			FacesUtil.redirecionarPagina("/DashBoard.xhtml?trocada=true");
		}

	}

}
