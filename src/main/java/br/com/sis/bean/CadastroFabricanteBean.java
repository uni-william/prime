package br.com.sis.bean;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sis.entity.Fabricante;
import br.com.sis.entity.Modelo;
import br.com.sis.security.Seguranca;
import br.com.sis.service.FabricanteService;
import br.com.sis.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroFabricanteBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private FabricanteService service;

	@Inject
	private Seguranca seguranca;

	private Fabricante fabricante;
	private Modelo modelo;
	private int indice;

	public Fabricante getFabricante() {
		return fabricante;
	}

	public void setFabricante(Fabricante fabricante) {
		this.fabricante = fabricante;
	}

	public Modelo getModelo() {
		return modelo;
	}

	public void setModelo(Modelo modelo) {
		this.modelo = modelo;
	}

	public int getIndice() {
		return indice;
	}

	public void setIndice(int indice) {
		this.indice = indice;
	}

	public void inicializar() {
		modelo = new Modelo();
		if (fabricante == null) {
			fabricante = new Fabricante();
		}
		indice = -1;
	}

	public void salvar() {
		fabricante.setDescricao(fabricante.getDescricao().trim());
		if (service.salvar(fabricante) != null) {
			FacesUtil.addInfoMessage("Fabricante salvo com sucesso!");
		}
	}

	public boolean isEditando() {
		return this.fabricante.getId() != null;
	}

	public boolean isPodeSalvar() {
		return (isEditando() && seguranca.isFabricanteEditar()) || (!isEditando() && seguranca.isFabricanteInserir());
	}

	public void adicionarModelo() {
		modelo.setFabricante(fabricante);
		if (indice > -1) {
			fabricante.getModelos().set(indice, modelo);
		} else {
			fabricante.getModelos().add(modelo);
		}
		modelo = new Modelo();
		indice = -1;
	}
	
	public void editarModelo(int linha) {
		indice = linha;
		modelo = fabricante.getModelos().get(linha);
	}
	
	public void removerModelo(int linha) {
		fabricante.getModelos().remove(linha);
	}

}
