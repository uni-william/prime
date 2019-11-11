package br.com.sis.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import br.com.sis.entity.Perfil;
import br.com.sis.entity.Usuario;
import br.com.sis.enuns.Funcionalidade;
import br.com.sis.repository.UsuarioRepository;
import br.com.sis.util.cdi.CDIServiceLocator;

public class AppUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		UsuarioRepository repository = CDIServiceLocator.getBean(UsuarioRepository.class);
		Usuario usuario = repository.porLogin(login);
		UsuarioSistema user = null;

		if (usuario != null) {
			user = new UsuarioSistema(usuario, getAuthorities(usuario));
		}

		return user;
	}

	private Collection<? extends GrantedAuthority> getAuthorities(Usuario usuario) {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		UsuarioRepository repository = CDIServiceLocator.getBean(UsuarioRepository.class);
		usuario = repository.usuarioComPaginas(usuario);
		for (Perfil perfil : usuario.getPerfis()) {
			for (Funcionalidade pagina : perfil.getPaginas()) {
				authorities.add(new SimpleGrantedAuthority("ROLE_" + pagina.toString()));
			}
		}
		
		return authorities;
	}

}
