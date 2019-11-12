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
import br.com.sis.enuns.StatusVeiculo;
import br.com.sis.repository.VeiculoRepository;
import br.com.sis.repository.filter.VeiculoFilter;

@Named
@ViewScoped
public class SelecaoVeiculoAluguelBean implements Serializable {

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
		veiculosFiltrados = veiculoRepository.filtradosAluguel(filter);
	}

	public void selectVeiculoFromDialog(Veiculo cliente) {
		RequestContext.getCurrentInstance().closeDialog(cliente);
	}

	public void inicializar() {
		filter = new VeiculoFilter();
		filter.setStatusVeiculo(StatusVeiculo.PARA_ALUGUEL);
	}
	
	public void abrirSelecaoVeiculoAluguel() {
		Map<String, Object> options = new HashMap<String, Object>();
		options.put("resizable", false);
		options.put("contentHeight", 520);
		options.put("height", 520);
		options.put("modal", true);
		RequestContext.getCurrentInstance().openDialog("/dialogos/DialogVeiculoAluguel", options, null);
	}
	

}
