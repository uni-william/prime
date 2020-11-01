package br.com.sis.security;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import br.com.sis.enuns.Funcionalidade;

@Named
@RequestScoped
public class Seguranca {

	@Inject
	private ExternalContext externalContext;

	public UsuarioSistema getUsuarioLogado() {
		UsuarioSistema usuario = null;
		UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) FacesContext
				.getCurrentInstance().getExternalContext().getUserPrincipal();
		if (auth != null && auth.getPrincipal() != null) {
			usuario = (UsuarioSistema) auth.getPrincipal();

		}
		return usuario;
	}

	public String getNomeUsuario() {
		String nome = null;
		UsuarioSistema user = getUsuarioLogado();
		if (user != null) {
			nome = user.getUsuario().getPessoa().getNome();
		}
		return nome;
	}

	public boolean isPerfilInserir() {
		return externalContext.isUserInRole(Funcionalidade.PERFIL_INSERT.toString());
	}

	public boolean isPerfilExcluir() {
		return externalContext.isUserInRole(Funcionalidade.PERFIL_DEL.toString());
	}

	public boolean isPerfilEditar() {
		return externalContext.isUserInRole(Funcionalidade.PERFIL_EDIT.toString());
	}

	public boolean isUsuarioInserir() {
		return externalContext.isUserInRole(Funcionalidade.USUARIO_INSERT.toString());
	}

	public boolean isUsuarioExcluir() {
		return externalContext.isUserInRole(Funcionalidade.USUARIO_DEL.toString());
	}

	public boolean isUsuarioEditar() {
		return externalContext.isUserInRole(Funcionalidade.USUARIO_EDIT.toString());
	}

	public boolean isClienteInserir() {
		return externalContext.isUserInRole(Funcionalidade.CLIENTE_INSERT.toString());
	}

	public boolean isClienteExcluir() {
		return externalContext.isUserInRole(Funcionalidade.CLIENTE_DEL.toString());
	}

	public boolean isClienteEditar() {
		return externalContext.isUserInRole(Funcionalidade.CLIENTE_EDIT.toString());
	}

	public boolean isFuncionarioInserir() {
		return externalContext.isUserInRole(Funcionalidade.FUNCIONARIO_INSERT.toString());
	}

	public boolean isFuncionarioExcluir() {
		return externalContext.isUserInRole(Funcionalidade.FUNCIONARIO_DEL.toString());
	}

	public boolean isFuncionarioEditar() {
		return externalContext.isUserInRole(Funcionalidade.FUNCIONARIO_EDIT.toString());
	}

	public boolean isFabricanteInserir() {
		return externalContext.isUserInRole(Funcionalidade.FABRICANTE_INSERT.toString());
	}

	public boolean isFabricanteExcluir() {
		return externalContext.isUserInRole(Funcionalidade.FABRICANTE_DEL.toString());
	}

	public boolean isFabricanteEditar() {
		return externalContext.isUserInRole(Funcionalidade.FABRICANTE_EDIT.toString());
	}

	public boolean isAcessorioInserir() {
		return externalContext.isUserInRole(Funcionalidade.ACESSORIO_INSERT.toString());
	}

	public boolean isAcessorioExcluir() {
		return externalContext.isUserInRole(Funcionalidade.ACESSORIO_DEL.toString());
	}

	public boolean isAcessorioEditar() {
		return externalContext.isUserInRole(Funcionalidade.ACESSORIO_EDIT.toString());
	}

	public boolean isVeiculoInserir() {
		return externalContext.isUserInRole(Funcionalidade.VEICULO_INSERT.toString());

	}

	public boolean isVeiculoExcluir() {
		return externalContext.isUserInRole(Funcionalidade.VEICULO_DEL.toString());
	}

	public boolean isVeiculoEditar() {
		return externalContext.isUserInRole(Funcionalidade.VEICULO_EDIT.toString());
	}

	public boolean isBancoInserir() {
		return externalContext.isUserInRole(Funcionalidade.BANCO_INSERT.toString());

	}

	public boolean isBancoExcluir() {
		return externalContext.isUserInRole(Funcionalidade.BANCO_DEL.toString());
	}

	public boolean isBancoEditar() {
		return externalContext.isUserInRole(Funcionalidade.BANCO_EDIT.toString());
	}
	
	public boolean isCanalInserir() {
		return externalContext.isUserInRole(Funcionalidade.CANAL_INSERT.toString());

	}

	public boolean isCanalExcluir() {
		return externalContext.isUserInRole(Funcionalidade.CANAL_DEL.toString());
	}

	public boolean isCanalEditar() {
		return externalContext.isUserInRole(Funcionalidade.CANAL_EDIT.toString());
	}
	
	public boolean isFormaPagamentoInserir() {
		return externalContext.isUserInRole(Funcionalidade.FORMA_INSERT.toString());

	}

	public boolean isFormaPagamentoExcluir() {
		return externalContext.isUserInRole(Funcionalidade.FORMA_DEL.toString());
	}

	public boolean isFormaPagamentoEditar() {
		return externalContext.isUserInRole(Funcionalidade.FORMA_EDIT.toString());
	}	

	
	public boolean isTipoDespesaInserir() {
		return externalContext.isUserInRole(Funcionalidade.TIPO_DESPESA_INSERT.toString());
	}

	public boolean isTipoDespesaExcluir() {
		return externalContext.isUserInRole(Funcionalidade.TIPO_DESPESA_DEL.toString());
	}

	public boolean isTipoDespesaEditar() {
		return externalContext.isUserInRole(Funcionalidade.TIPO_DESPESA_EDIT.toString());
	}
	
	public boolean isDespesaInserir() {
		return externalContext.isUserInRole(Funcionalidade.DESPESA_INSERT.toString());
	}

	public boolean isDespesaExcluir() {
		return externalContext.isUserInRole(Funcionalidade.DESPESA_DEL.toString());
	}

	public boolean isDespesaEditar() {
		return externalContext.isUserInRole(Funcionalidade.DESPESA_EDIT.toString());
	}
	
	public boolean isVendedorInserir() {
		return externalContext.isUserInRole(Funcionalidade.VENDEDOR_INSERT.toString());
	}

	public boolean isVendedorExcluir() {
		return externalContext.isUserInRole(Funcionalidade.VENDEDOR_DEL.toString());
	}

	public boolean isVendedorEditar() {
		return externalContext.isUserInRole(Funcionalidade.VENDEDOR_EDIT.toString());
	}

}
