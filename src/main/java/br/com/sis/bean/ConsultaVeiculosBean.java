package br.com.sis.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sis.entity.Veiculo;
import br.com.sis.repository.VeiculoRepository;

@Named
@ViewScoped
public class ConsultaVeiculosBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private VeiculoRepository veiculoRepository;

	private List<Veiculo> listaVeiculos;

	public List<Veiculo> getListaVeiculos() {
		return listaVeiculos;
	}
	
	public void inicializar() {
		listaVeiculos = veiculoRepository.listaDeCarrosNoPatio();
	}

}
