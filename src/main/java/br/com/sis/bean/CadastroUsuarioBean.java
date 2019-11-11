package br.com.sis.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.DualListModel;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.sis.entity.Configuracoes;
import br.com.sis.entity.Perfil;
import br.com.sis.entity.Pessoa;
import br.com.sis.entity.Usuario;
import br.com.sis.repository.ConfiguracoesRepository;
import br.com.sis.repository.PerfilRepository;
import br.com.sis.repository.PessoaRepository;
import br.com.sis.security.Seguranca;
import br.com.sis.service.CommonsEmailService;
import br.com.sis.service.UsuarioService;
import br.com.sis.util.Utils;
import br.com.sis.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroUsuarioBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private UsuarioService usuarioService;

	@Inject
	private PerfilRepository perfilRepository;

	@Inject
	private CommonsEmailService commonsEmailService;

	@Inject
	private PessoaRepository funcionarioRepositorio;

	@Inject
	private ConfiguracoesRepository configuracoesRepository;

	@Inject
	private Seguranca seguranca;

	private Usuario usuario;

	private Pessoa pessoa;

	private boolean desabilitarComboFunc;

	private List<String> listaPerfis = new ArrayList<>();
	private List<String> listaPerfisUsuario = new ArrayList<>();

	private List<Pessoa> listaFuncionarios;

	private DualListModel<String> perfisModel;

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public List<Pessoa> getListaFuncionarios() {
		return listaFuncionarios;
	}

	public void setListaFuncionarios(List<Pessoa> listaFuncionarios) {
		this.listaFuncionarios = listaFuncionarios;
	}

	public boolean isDesabilitarComboFunc() {
		return desabilitarComboFunc;
	}

	public List<String> getListaPerfis() {
		return listaPerfis;
	}

	public void setListaPerfis(List<String> listaPerfis) {
		this.listaPerfis = listaPerfis;
	}

	public List<String> getListaPerfisUsuario() {
		return listaPerfisUsuario;
	}

	public void setListaPerfisUsuario(List<String> listaPerfisUsuario) {
		this.listaPerfisUsuario = listaPerfisUsuario;
	}

	public DualListModel<String> getPerfisModel() {
		return perfisModel;
	}

	public void setPerfisModel(DualListModel<String> perfisModel) {
		this.perfisModel = perfisModel;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public void inicializar() {
		listaPerfisUsuario.clear();
		listaPerfis.clear();
		List<Perfil> listAllPerfis = perfilRepository.listAll();
		if (usuario == null) {
			listaFuncionarios = funcionarioRepositorio.listaPessoasSemUsuario();
			usuario = new Usuario();
			usuario.setAtivo(true);
			usuario.setTrocarSenha(true);
			if (pessoa != null) {
				usuario.setPessoa(pessoa);
			}
		} else {
			listaFuncionarios = funcionarioRepositorio.listAllFuncionarios();
			for (Perfil p : usuario.getPerfis()) {
				listaPerfisUsuario.add(p.getId().toString());
			}
		}
		for (Perfil p : listAllPerfis) {
			if (!listaPerfisUsuario.contains(p.getId().toString())) {
				listaPerfis.add(p.getId().toString());
			}
		}
		perfisModel = new DualListModel<>(listaPerfis, listaPerfisUsuario);

		desabilitarComboFunc = usuario.getPessoa() != null;

	}

	public void salvar() {
		Configuracoes configuracoes = configuracoesRepository.configuracoesGerais();
		boolean novo = false;
		String senhaGerada = null;
		usuario.getPerfis().clear();
		for (String id : perfisModel.getTarget()) {
			Perfil p = perfilRepository.porId(Long.parseLong(id));
			usuario.getPerfis().add(p);
		}
		if (usuario.getId() == null) {
			novo = true;
			if (configuracoes.isEnviarSenhaEmail()) {
				senhaGerada = Utils.geraSenha();
			} else {
				senhaGerada = usuario.getPessoa().getDocumentoReceita();
			}
			String senhaCripto = new BCryptPasswordEncoder().encode(senhaGerada);
			usuario.setPassword(senhaCripto);
			usuario.setTrocarSenha(true);
		}
		usuario = usuarioService.salvar(usuario);
		if (usuario != null) {
			if (novo == true) {
				if (configuracoes.isEnviarSenhaEmail()) {
					if (!commonsEmailService
							.enviarEmail(usuario.getPessoa().getEmail(), usuario.getPessoa().getEmail(),
									"Redefinição de senha", "Caro colaborador(a) " + usuario.getPessoa().getNome()
											+ ".\nNova senha gerada: " + senhaGerada + ".\nTrocar no próximo logon",
									false)) {
						FacesUtil
								.addErroMessage("Ocorreu um erro no envio o email. Anote a senha gerada: " + senhaGerada
										+ " e passe para o usuário. Será necessário a troca da senha no próximo login");
					} else {
						FacesUtil.addInfoMessage("Usuário salvo com sucesso!");
					}
				} else {
					FacesUtil.addInfoMessage("Usuário salvo com sucesso. Sua senha de acesso inicial é o CPF. Será solicitado trocar no próximo login");
				}
			} else {
				FacesUtil.addInfoMessage("Usuário salvo com sucesso!");
			}

		}

	}

	public String descricaoPerfil(String id) {
		Perfil p = perfilRepository.porId(Long.parseLong(id));
		return p.getDescricao();
	}

	public boolean isEditando() {
		return this.usuario.getId() != null;
	}

	public boolean isPodeSalvar() {
		return (isEditando() && seguranca.isUsuarioEditar()) || (!isEditando() && seguranca.isUsuarioInserir());
	}

}
