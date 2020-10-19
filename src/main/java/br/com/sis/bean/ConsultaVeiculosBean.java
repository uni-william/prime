package br.com.sis.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sis.entity.Veiculo;
import br.com.sis.repository.VeiculoRepository;
import br.com.sis.service.VeiculoService;
import br.com.sis.util.jsf.FacesUtil;

@Named
@ViewScoped
public class ConsultaVeiculosBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private VeiculoRepository veiculoRepository;
	
	@Inject
	private VeiculoService veiculoService;

	private List<Veiculo> listaVeiculos;

	public List<Veiculo> getListaVeiculos() {
		return listaVeiculos;
	}

	private Veiculo veiculoSelecionado;

	public Veiculo getVeiculoSelecionado() {
		return veiculoSelecionado;
	}

	public void setVeiculoSelecionado(Veiculo veiculoSelecionado) {
		this.veiculoSelecionado = veiculoSelecionado;
	}

	public void inicializar() {
		listaVeiculos = veiculoRepository.listaDeCarrosNoPatio();
	}
	
	public void excluir() {
		if (veiculoService.removerVeiculo(veiculoSelecionado)) {
			FacesUtil.addInfoMessage("Veículo excluído com sucesso!");
			listaVeiculos = veiculoRepository.listaDeCarrosNoPatio();
		}
	}

}
