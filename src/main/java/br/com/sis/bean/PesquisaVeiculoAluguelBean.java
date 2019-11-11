package br.com.sis.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import br.com.sis.entity.Modelo;
import br.com.sis.entity.Veiculo;
import br.com.sis.repository.ModeloRepository;
import br.com.sis.repository.VeiculoRepository;
import br.com.sis.repository.filter.VeiculoFilter;
import br.com.sis.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaVeiculoAluguelBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private VeiculoRepository repository;

	@Inject
	private ModeloRepository modeloRepository;

	private List<Veiculo> listaVeiculos;
	private Veiculo veiculoSelecionado;
	private VeiculoFilter filter;
	private List<Modelo> modelos;

	private boolean desabilitarBotoes;

	public boolean isDesabilitarBotoes() {
		return desabilitarBotoes;
	}

	public void setDesabilitarBotoes(boolean desabilitarBotoes) {
		this.desabilitarBotoes = desabilitarBotoes;
	}

	public List<Modelo> getModelos() {
		return modelos;
	}

	public VeiculoFilter getFilter() {
		return filter;
	}

	public void setFilter(VeiculoFilter filter) {
		this.filter = filter;
	}

	public Veiculo getVeiculoSelecionado() {
		return veiculoSelecionado;
	}

	public void setVeiculoSelecionado(Veiculo veiculoSelecionado) {
		this.veiculoSelecionado = veiculoSelecionado;
	}

	public List<Veiculo> getlistaVeiculos() {
		return listaVeiculos;
	}

	public void inicializar() {
		modelos = modeloRepository.listAll();
		filter = new VeiculoFilter();
		veiculoSelecionado = new Veiculo();
		this.desabilitarBotoes = true;
	}

	public void excluir() {
		if (repository.remover(veiculoSelecionado) == true) {
			veiculoSelecionado = new Veiculo();
			listaVeiculos = repository.filtradosAluguel(filter);
			FacesUtil.addInfoMessage("Registro excluído com sucesso!");
			setDesabilitarBotoes(true);
		}
	}

	public void onRowSelect(SelectEvent event) {
		desabilitarBotoes = false;
	}

	public void onRowUnSelect(UnselectEvent event) {
		desabilitarBotoes = true;
	}

	public void pesquisar() {
		listaVeiculos = repository.filtradosAluguel(filter);
	}
	

}
