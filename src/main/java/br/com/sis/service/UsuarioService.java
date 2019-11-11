package br.com.sis.service;

import java.io.Serializable;

import javax.inject.Inject;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.sis.entity.Configuracoes;
import br.com.sis.entity.Usuario;
import br.com.sis.repository.ConfiguracoesRepository;
import br.com.sis.repository.UsuarioRepository;
import br.com.sis.util.Utils;
import br.com.sis.util.jpa.Transactional;
import br.com.sis.util.jsf.FacesUtil;

public class UsuarioService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private UsuarioRepository usuarioRepository;

	@Inject
	private CommonsEmailService commonsEmailService;

	@Inject
	private ConfiguracoesRepository configuracoesRepository;

	@Transactional
	public Usuario salvar(Usuario usuario) {
		Usuario usuarioExistente = usuarioRepository.porLogin(usuario.getLogin());
		if (usuarioExistente != null && !usuarioExistente.equals(usuario)) {
			FacesUtil.addErroMessage("Já existe um usuario cadastrado com esse login.");
			return null;
		} else {
			return usuarioRepository.salvar(usuario);
		}
	}

	@Transactional
	public Usuario gerarSenhaESalvar(Usuario usuario) {
		Usuario user = null;
		Usuario usuarioExistente = usuarioRepository.porLogin(usuario.getLogin());
		if (usuarioExistente != null && !usuarioExistente.equals(usuario)) {
			FacesUtil.addErroMessage("Já existe um usuario cadastrado com esse login.");
		} else {
			Configuracoes configuracoes = configuracoesRepository.configuracoesGerais();
			String senhaGerada = null;
			String senhaCripto = null;
			if (configuracoes.isEnviarSenhaEmail()) {
				senhaGerada = Utils.geraSenha();
			} else {
				senhaGerada = usuario.getPessoa().getDocumentoReceita();
			}
			senhaCripto = new BCryptPasswordEncoder().encode(senhaGerada);
			usuario.setPassword(senhaCripto);
			usuario.setTrocarSenha(true);
			user = usuarioRepository.salvar(usuario);
			if (user != null) {
				if (configuracoes.isEnviarSenhaEmail()) {
					if (!commonsEmailService
							.enviarEmail(user.getPessoa().getEmail(), user.getPessoa().getEmail(),
									"Redefinição de senha", "Caro colaborador(a) " + user.getPessoa().getNome()
											+ ".\nNova senha gerada: " + senhaGerada + ".\nTrocar no próximo logon",
									false)) {
						FacesUtil
								.addErroMessage("Ocorreu um erro no envio o email. Anote a senha gerada: " + senhaGerada
										+ " e passe para o usuário. Será necessário a troca da senha no próximo login");
					} else {
						FacesUtil.addInfoMessage("Senha gerada e enviada com sucesso para o e-mail do funcionário");
					}
				} else {
					FacesUtil.addInfoMessage("Senha de acesso do usuário é o seu CPF. Será solicitado a troca no próximo login.");
				}
			}
		}
		return user;

	}

	@Transactional
	public Usuario esqueciSenha(String login) {
		Usuario user = null;
		Usuario usuarioExistente = usuarioRepository.porLogin(login);
		if (usuarioExistente != null) {
			String senhaGerada = Utils.geraSenha();
			String senhaCripto = new BCryptPasswordEncoder().encode(senhaGerada);
			usuarioExistente.setPassword(senhaCripto);
			usuarioExistente.setTrocarSenha(true);
			user = usuarioRepository.salvar(usuarioExistente);

		}
		return user;

	}

}
