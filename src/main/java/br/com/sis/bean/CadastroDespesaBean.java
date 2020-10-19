package br.com.sis.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sis.entity.Despesa;
import br.com.sis.entity.TipoDespesa;
import br.com.sis.repository.TipoDespesaRepository;
import br.com.sis.security.Seguranca;
import br.com.sis.service.DespesaService;
import br.com.sis.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroDespesaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private DespesaService service;

	@Inject
	private Seguranca seguranca;

	@Inject
	private TipoDespesaRepository tipoDespesaRepository;

	private List<TipoDespesa> tipoDespesas;

	private Despesa despesa;

	public Despesa getDespesa() {
		return despesa;
	}

	public void setDespesa(Despesa despesa) {
		this.despesa = despesa;
	}

	public List<TipoDespesa> getTipoDespesas() {
		return tipoDespesas;
	}

	public void inicializar() {
		tipoDespesas = tipoDespesaRepository.listAll();
		if (despesa == null) {
			despesa = new Despesa();
		}
	}

	public void salvar() {
		despesa.setDescricao(despesa.getDescricao().trim());
		if (service.salvar(despesa) != null) {
			FacesUtil.addInfoMessage("Despesa salvo com sucesso!");
			despesa = new Despesa();
		}
	}

	public boolean isEditando() {
		return this.despesa.getId() != null;
	}

	public boolean isPodeSalvar() {
		return (isEditando() && seguranca.isDespesaEditar()) || (!isEditando() && seguranca.isDespesaInserir());
	}

}
