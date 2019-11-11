package br.com.sis.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.context.RequestContext;

import br.com.sis.entity.Veiculo;
import br.com.sis.repository.VeiculoRepository;
import br.com.sis.repository.filter.VeiculoFilter;

@Named
@ViewScoped
public class SelecaoVeiculoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private VeiculoRepository veiculoRepository;

	private List<Veiculo> veiculosFiltrados = new ArrayList<Veiculo>();
	private VeiculoFilter filter;

	public VeiculoFilter getFilter() {
		return filter;
	}

	public void setFilter(VeiculoFilter filter) {
		this.filter = filter;
	}

	public List<Veiculo> getVeiculosFiltrados() {
		return veiculosFiltrados;
	}

	public void pesquisar() {
		veiculosFiltrados = veiculoRepository.filtrados(filter);
	}

	public void selectVeiculoFromDialog(Veiculo cliente) {
		RequestContext.getCurrentInstance().closeDialog(cliente);
	}

	public void inicializar() {
		filter = new VeiculoFilter();
	}

	public void abrirSelecaoVeiculoCompra() {
		Map<String, Object> options = new HashMap<String, Object>();
		options.put("resizable", false);
		options.put("contentHeight", 520);
		options.put("height", 520);
		options.put("modal", true);
		RequestContext.getCurrentInstance().openDialog("/dialogos/DialogVeiculo", options, null);
	}
	
	public void abrirSelecaoVeiculoVenda() {
		Map<String, Object> options = new HashMap<String, Object>();
		options.put("resizable", false);
		options.put("contentHeight", 520);
		options.put("height", 520);
		options.put("modal", true);
		RequestContext.getCurrentInstance().openDialog("/dialogos/DialogVeiculo", options, null);
	}
	

}
