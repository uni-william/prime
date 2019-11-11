package br.com.sis.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.DualListModel;

import br.com.sis.entity.Perfil;
import br.com.sis.enuns.Funcionalidade;
import br.com.sis.security.Seguranca;
import br.com.sis.service.PerfilService;
import br.com.sis.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroPerfilBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private PerfilService service;
	
	@Inject
	private Seguranca seguranca;

	private Perfil perfil;

	private List<String> listAllPages = new ArrayList<String>();
	private List<String> paginasPerfil = new ArrayList<String>();

	private DualListModel<String> paginas;

	public DualListModel<String> getPaginas() {
		return paginas;
	}

	public void setPaginas(DualListModel<String> paginas) {
		this.paginas = paginas;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}
	

	public void inicializar() {
		if (perfil == null) {
			perfil = new Perfil();
		}
		paginasPerfil.clear();
		perfil.getPaginas().sort(null);
		for (Funcionalidade p : perfil.getPaginas()) {
			paginasPerfil.add(p.toString());
		}
		for (Funcionalidade p : Funcionalidade.values()) {
			if (!paginasPerfil.contains(p.toString())) {
				listAllPages.add(p.toString());
			}
		}
		
		paginas = new DualListModel<String>(listAllPages, paginasPerfil);
	}

	public void salvar() {
		perfil.setDescricao(perfil.getDescricao().trim());
		paginasPerfil = paginas.getTarget();
		perfil.getPaginas().clear();
		for (String s : paginasPerfil) {
			Funcionalidade p = Funcionalidade.valueOf(s);
			perfil.getPaginas().add(p);
		}

		if (service.salvar(perfil) != null) {
			FacesUtil.addInfoMessage("Perfil salvo com sucesso!");
			perfil = new Perfil();
			inicializar();
		}
	}

	public String mostrarDescricao(String s) {
		Funcionalidade pagina = Funcionalidade.valueOf(s);
		return pagina.getDescricao();
	}

	public boolean isEditando() {
		return this.perfil.getId() != null;
	}
	
	public boolean isPodeSalvar() {
		return (isEditando() && seguranca.isPerfilEditar()) || (!isEditando() && seguranca.isPerfilInserir());
	}
	
}
