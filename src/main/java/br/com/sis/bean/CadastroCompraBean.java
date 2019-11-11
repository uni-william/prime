package br.com.sis.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;

import br.com.sis.converter.AcessorioConverter;
import br.com.sis.entity.Acessorio;
import br.com.sis.entity.Fabricante;
import br.com.sis.entity.Modelo;
import br.com.sis.entity.Movimentacao;
import br.com.sis.entity.Pessoa;
import br.com.sis.entity.Veiculo;
import br.com.sis.enuns.Estado;
import br.com.sis.enuns.Tipo;
import br.com.sis.enuns.TipoOperacao;
import br.com.sis.enuns.TipoPessoa;
import br.com.sis.enuns.TipoVeiculo;
import br.com.sis.repository.AcessorioRepository;
import br.com.sis.repository.FabricanteRepository;
import br.com.sis.repository.ModeloRepository;
import br.com.sis.repository.PessoaRepository;
import br.com.sis.repository.VeiculoRepository;
import br.com.sis.security.Seguranca;
import br.com.sis.service.FabricanteService;
import br.com.sis.service.ModeloService;
import br.com.sis.service.MovimentacaoService;
import br.com.sis.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroCompraBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Seguranca seguranca;

	@Inject
	private FabricanteRepository fabricanteRepository;

	@Inject
	private ModeloRepository modeloRepository;

	@Inject
	private PessoaRepository pessoaRepository;

	@Inject
	private AcessorioRepository acessorioRepository;

	@Inject
	private AcessorioConverter acessorioConverter;

	@Inject
	private MovimentacaoService service;

	@Inject
	private VeiculoRepository veiculoRepository;
	
	@Inject
	private FabricanteService fabricanteService;
	
	@Inject
	private ModeloService modeloService;
	
	private List<Fabricante> fabricantes;

	private Fabricante fabricanteSelecionado;

	private List<Modelo> modelos;

	private List<Acessorio> acessorios;

	private Movimentacao movimentacao;

	private String mascara;
	private int indicePessoa;

	private Modelo novoModelo;

	public Modelo getNovoModelo() {
		return novoModelo;
	}

	public void setNovoModelo(Modelo novoModelo) {
		this.novoModelo = novoModelo;
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

	public Movimentacao getMovimentacao() {
		return movimentacao;
	}

	public void setMovimentacao(Movimentacao movimentacao) {
		this.movimentacao = movimentacao;
	}

	public int getIndicePessoa() {
		return indicePessoa;
	}

	public void setIndicePessoa(int indicePessoa) {
		this.indicePessoa = indicePessoa;
	}

	public String getMascara() {
		if (this.indicePessoa == 0) {
			mascara = "999.999.999-99";
		} else if (this.indicePessoa == 1) {
			mascara = "99.999.999/9999-99";
		}
		return mascara;
	}

	public void setMascara(String mascara) {
		this.mascara = mascara;
	}

	public AcessorioConverter getAcessorioConverter() {
		return acessorioConverter;
	}

	public void setAcessorioConverter(AcessorioConverter acessorioConverter) {
		this.acessorioConverter = acessorioConverter;
	}

	public List<Modelo> getModelos() {
		return modelos;
	}

	public void setModelos(List<Modelo> modelos) {
		this.modelos = modelos;
	}

	public List<Acessorio> getAcessorios() {
		return acessorios;
	}

	public void setAcessorios(List<Acessorio> acessorios) {
		this.acessorios = acessorios;
	}

	public void atualizaTipoPessoa() {
		if (indicePessoa == 0) {
			movimentacao.getCliente().setTipoPessoa(TipoPessoa.FISICA);
		} else if (indicePessoa == 1) {
			movimentacao.getCliente().setTipoPessoa(TipoPessoa.JURIDICA);
		}
	}

	public boolean isExibirDocumento() {
		return (this.movimentacao.getCliente().getId() == null);
	}

	public Estado[] getEstados() {
		return Estado.values();
	}

	public TipoVeiculo[] getTipoVeiculos() {
		return TipoVeiculo.values();
	}

	public void inicializar() {
		fabricanteSelecionado = new Fabricante();
		fabricantes = fabricanteRepository.listAll();
		acessorios = acessorioRepository.listAll();
		if (movimentacao == null) {
			movimentacao = new Movimentacao();
			movimentacao.setData(new Date());
			movimentacao.setVeiculo(new Veiculo());
			movimentacao.setCliente(new Pessoa());
			movimentacao.setFuncionario(seguranca.getUsuarioLogado().getUsuario().getPessoa());
			movimentacao.setTipoOperacao(TipoOperacao.COMPRA);

			movimentacao.getCliente().setAtivo(true);
			movimentacao.getCliente().setTipo(Tipo.CLIENTE);
			movimentacao.getCliente().setTipoPessoa(TipoPessoa.FISICA);
			indicePessoa = 0;
		} else {
			fabricanteSelecionado = movimentacao.getVeiculo().getModelo().getFabricante();
			listarModelos();
			if (movimentacao.getCliente().isFisica()) {
				indicePessoa = 0;
			} else {
				indicePessoa = 1;
			}
		}
	}

	public void eftuarCompra() {
		movimentacao = service.efetuarCompra(movimentacao);
		if (movimentacao != null) {
			FacesUtil.addInfoMessage("Operação realizada com sucesso");
		}
	}

	public void onClienteEscolhido(SelectEvent event) {
		Pessoa pes = (Pessoa) event.getObject();
		this.movimentacao.setCliente(pes);
		if (movimentacao.getCliente().isFisica()) {
			indicePessoa = 0;
		} else {
			indicePessoa = 1;
		}
	}

	public void carregarClientePorDocumentoReceita() {
		if (this.movimentacao.getId() == null) {
			Pessoa p = pessoaRepository.porCpf(this.movimentacao.getCliente().getDocumentoReceita());
			if (p != null) {
				this.movimentacao.setCliente(p);
			}
		}
	}

	public void onVeiculoEscolhido(SelectEvent event) {
		Veiculo carro = (Veiculo) event.getObject();
		carro = veiculoRepository.porId(carro.getId());
		this.movimentacao.setVeiculo(carro);
	}

	public boolean isDesabilitaTabVeiculo() {
		return this.movimentacao.getVeiculo() != null && this.movimentacao.getVeiculo().getId() != null;
	}

	public boolean isDesabilitaTabCliente() {
		return this.movimentacao.getCliente() != null && this.movimentacao.getCliente().getId() != null;
	}

	public boolean isCompraEfetuada() {
		return this.movimentacao.getId() != null;
	}

	public void listarModelos() {
		modelos = modeloRepository.modelosPorFabricante(fabricanteSelecionado);
	}
	
	public void novoFabricante() {
		fabricanteSelecionado = new Fabricante();
		modelos = null;
	}
	
	public void novoModelo() {
		novoModelo = new Modelo();
		novoModelo.setFabricante(fabricanteSelecionado);
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
			movimentacao.getVeiculo().setModelo(novoModelo);
		}
	}	

}
