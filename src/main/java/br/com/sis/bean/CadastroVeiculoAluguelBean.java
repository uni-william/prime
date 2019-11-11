package br.com.sis.bean;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sis.converter.AcessorioConverter;
import br.com.sis.entity.Acessorio;
import br.com.sis.entity.Fabricante;
import br.com.sis.entity.Modelo;
import br.com.sis.entity.Veiculo;
import br.com.sis.enuns.StatusVeiculo;
import br.com.sis.enuns.TipoVeiculo;
import br.com.sis.repository.AcessorioRepository;
import br.com.sis.repository.FabricanteRepository;
import br.com.sis.repository.ModeloRepository;
import br.com.sis.security.Seguranca;
import br.com.sis.service.FabricanteService;
import br.com.sis.service.ModeloService;
import br.com.sis.service.VeiculoService;
import br.com.sis.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroVeiculoAluguelBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private VeiculoService service;

	@Inject
	private Seguranca seguranca;

	@Inject
	private FabricanteRepository fabricanteRepository;

	@Inject
	private ModeloRepository modeloRepository;

	@Inject
	private AcessorioRepository acessorioRepository;

	@Inject
	private AcessorioConverter acessorioConverter;

	@Inject
	private FabricanteService fabricanteService;
	
	@Inject
	private ModeloService modeloService;

	private List<Modelo> modelos;

	private List<Fabricante> fabricantes;

	private Fabricante fabricanteSelecionado;

	private Veiculo veiculo;

	private List<Acessorio> acessorios;

	private Modelo novoModelo;

	public Modelo getNovoModelo() {
		return novoModelo;
	}

	public void setNovoModelo(Modelo novoModelo) {
		this.novoModelo = novoModelo;
	}

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public AcessorioConverter getAcessorioConverter() {
		return acessorioConverter;
	}

	public void setAcessorioConverter(AcessorioConverter acessorioConverter) {
		this.acessorioConverter = acessorioConverter;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	public List<Modelo> getModelos() {
		return modelos;
	}

	public List<Acessorio> getAcessorios() {
		return acessorios;
	}

	public TipoVeiculo[] getTipoVeiculos() {
		return TipoVeiculo.values();
	}

	public Fabricante getFabricanteSelecionado() {
		return fabricanteSelecionado;
	}

	public void setFabricanteSelecionado(Fabricante fabricanteSelecionado) {
		this.fabricanteSelecionado = fabricanteSelecionado;
	}

	public List<Fabricante> getFabricantes() {
		return fabricantes;
	}

	public void listarModelos() {
		modelos = modeloRepository.modelosPorFabricante(fabricanteSelecionado);
	}

	public void inicializar() {
		fabricanteSelecionado = new Fabricante();
		fabricantes = fabricanteRepository.listAll();
		acessorios = acessorioRepository.listAll();
		if (veiculo == null) {
			veiculo = new Veiculo();
			veiculo.setVeiculoAluguel(true);
			veiculo.setStatusVeiculo(StatusVeiculo.PARA_ALUGUEL);
		} else {
			fabricanteSelecionado = veiculo.getModelo().getFabricante();
			listarModelos();
		}
	}

	public void novoFabricante() {
		fabricanteSelecionado = new Fabricante();
		modelos = null;
	}
	
	public void novoModelo() {
		novoModelo = new Modelo();
		novoModelo.setFabricante(fabricanteSelecionado);
	}

	public void salvar() {
		veiculo.setChassi(veiculo.getChassi().trim());
		if (service.salvar(veiculo) != null) {
			FacesUtil.addInfoMessage("Veículo salvo com sucesso!");
			veiculo = new Veiculo();
			veiculo.setVeiculoDeParceiro(true);
			veiculo.setStatusVeiculo(StatusVeiculo.PATIO);
		}
	}

	public void salvarNovoFabricante() {
		fabricanteSelecionado = fabricanteService.salvar(fabricanteSelecionado);
		if (fabricanteSelecionado == null) {
			fabricanteSelecionado = new Fabricante();
		} else {
			fabricantes = fabricanteRepository.listAll();
			listarModelos();
		}
	}
	
	public void salvarNovoModelo() {
		novoModelo = modeloService.salvar(novoModelo);
		if (novoModelo != null) {
			listarModelos();
			veiculo.setModelo(novoModelo);
		}
	}

	public boolean isEditando() {
		return this.veiculo.getId() != null;
	}

	public boolean isPodeSalvar() {
		return (isEditando() && seguranca.isVeiculoEditar()) || (!isEditando() && seguranca.isVeiculoInserir());
	}
	
	

}
