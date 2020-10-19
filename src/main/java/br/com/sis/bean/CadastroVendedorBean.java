package br.com.sis.bean;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sis.entity.Vendedor;
import br.com.sis.security.Seguranca;
import br.com.sis.service.VendedorService;
import br.com.sis.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroVendedorBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private VendedorService service;

	@Inject
	private Seguranca seguranca;

	private Vendedor vendedor;

	public Vendedor getVendedor() {
		return vendedor;
	}

	public void setVendedor(Vendedor vendedor) {
		this.vendedor = vendedor;
	}

	public void inicializar() {
		if (vendedor == null) {
			vendedor = new Vendedor();
		}
	}

	public void salvar() {
		vendedor.setNome(vendedor.getNome().trim());
		if (service.salvar(vendedor) != null) {
			FacesUtil.addInfoMessage("Vendedor salvo com sucesso!");
			vendedor = new Vendedor();
		}
	}

	public boolean isEditando() {
		return this.vendedor.getId() != null;
	}

	public boolean isPodeSalvar() {
		return (isEditando() && seguranca.isVendedorEditar()) || (!isEditando() && seguranca.isVendedorInserir());
	}

}
