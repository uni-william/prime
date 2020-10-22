package br.com.sis.bean;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.sis.converter.PessoaConverter;
import br.com.sis.entity.Banco;
import br.com.sis.entity.ComissaoVenda;
import br.com.sis.entity.Movimentacao;
import br.com.sis.entity.Pessoa;
import br.com.sis.entity.Veiculo;
import br.com.sis.entity.Vendedor;
import br.com.sis.enuns.TipoOperacao;
import br.com.sis.report.ExecutorRelatorio;
import br.com.sis.repository.BancoRepository;
import br.com.sis.repository.MovimentacaoRepository;
import br.com.sis.repository.PessoaRepository;
import br.com.sis.repository.VeiculoRepository;
import br.com.sis.repository.VendedorRepository;
import br.com.sis.security.Seguranca;
import br.com.sis.service.MovimentacaoService;
import br.com.sis.util.Utils;
import br.com.sis.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroVendaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private PessoaRepository pessoaRepository;
	@Inject
	private VeiculoRepository veiculoRepository;
	@Inject
	private MovimentacaoService service;
	@Inject
	private Seguranca seguranca;
	@Inject
	private MovimentacaoRepository movimentacaoRepository;
	@Inject
	private BancoRepository bancoRepository;
	@Inject
	private VendedorRepository vendedorRepository;

	@Inject
	private PessoaConverter pessoaConverter;
	private Movimentacao movimentacao;

	@Inject
	private FacesContext facesContext;

	@Inject
	private HttpServletResponse response;

	@Inject
	private EntityManager manager;

	private String placa;

	private StreamedContent file;

	private List<Banco> bancos;

	private String labelBotao;
	private List<ComissaoVenda> comissoes = new ArrayList<ComissaoVenda>();
	private Vendedor vendedor = new Vendedor();
	private List<Vendedor> vendedores;

	public List<Banco> getBancos() {
		return bancos;
	}

	public void setBancos(List<Banco> bancos) {
		this.bancos = bancos;
	}

	public StreamedContent getFile() {
		return file;
	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}

	public PessoaConverter getPessoaConverter() {
		return pessoaConverter;
	}

	public void setPessoaConverter(PessoaConverter pessoaConverter) {
		this.pessoaConverter = pessoaConverter;
	}

	public Movimentacao getMovimentacao() {
		return movimentacao;
	}

	public void setMovimentacao(Movimentacao movimentacao) {
		this.movimentacao = movimentacao;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public boolean isExibirComponenteUpload() {
		return movimentacao.getId() != null && movimentacao.getTermoAssinado() == null;
	}

	public boolean isExibirBotaoDownload() {
		return movimentacao.getId() != null && movimentacao.getTermoAssinado() != null;
	}

	public String getLabelBotao() {
		return labelBotao;
	}

	public List<ComissaoVenda> getComissoes() {
		return comissoes;
	}

	public void setComissoes(List<ComissaoVenda> comissoes) {
		this.comissoes = comissoes;
	}

	public Vendedor getVendedor() {
		return vendedor;
	}

	public void setVendedor(Vendedor vendedor) {
		this.vendedor = vendedor;
	}

	public List<Vendedor> getVendedores() {
		return vendedores;
	}

	public void setVendedores(List<Vendedor> vendedores) {
		this.vendedores = vendedores;
	}

	public void inicializar() {
		bancos = bancoRepository.listAll();
		vendedores = vendedorRepository.listAllAtivos();
		if (movimentacao == null) {
			movimentacao = new Movimentacao();
			movimentacao.setFuncionario(seguranca.getUsuarioLogado().getUsuario().getPessoa());
			movimentacao.setData(new Date());
			movimentacao.setTipoOperacao(TipoOperacao.VENDA);
			this.labelBotao = "Efetuar Venda";
		} else {
			this.placa = this.movimentacao.getVeiculo().getPlaca();
			this.comissoes = this.movimentacao.getComissoes();
			this.labelBotao = "Atualizar Venda";
		}
	}

	public List<Pessoa> completePorNome(String nome) {
		return pessoaRepository.clientesAtivosPorNome(nome);
	}

	public void efetuarVenda() {
		movimentacao.setComissoes(this.comissoes);
		movimentacao = service.efetuarVenda(movimentacao);
		if (movimentacao != null) {
			FacesUtil.addInfoMessage("Operação realizada com sucesso");
		}
	}

	public void cancelarVenda() {
		movimentacao = service.cancelarVenda(movimentacao);
		if (movimentacao != null) {
			FacesUtil.addInfoMessage("Operação realizada com sucesso");
		}

	}

	public void onClienteEscolhido(SelectEvent event) {
		Pessoa pes = (Pessoa) event.getObject();
		this.movimentacao.setCliente(pes);
	}

	public void onVeiculoEscolhido(SelectEvent event) {
		Veiculo carro = (Veiculo) event.getObject();
		carro = veiculoRepository.porId(carro.getId());
		this.movimentacao.setVeiculo(carro);
		this.movimentacao.setValor(carro.getValorVenda());
		this.placa = carro.getPlaca();
	}

	public void pesquisarPorPlaca() {
		if (StringUtils.isNotBlank(placa)) {
			Veiculo v = veiculoRepository.porPlaca(placa);
			if (v != null && !v.isNoPatio()) {
				FacesUtil.addWarnMessage(
						"Veículo não disponível para venda. Somente veículos no pátio podem ser vendidos.");
				v = new Veiculo();
				movimentacao.setValor(null);
				movimentacao.setEntrada(null);
			} else {
				if (v == null) {
					v = new Veiculo();
					movimentacao.setValor(null);
					movimentacao.setEntrada(null);
				} else {
					movimentacao.setValor(v.getValorVenda());
				}
			}
			movimentacao.setVeiculo(v);
		} else {
			movimentacao.setVeiculo(new Veiculo());
		}
	}

	public boolean isVendaEfetuada() {
		return this.movimentacao.isConcluida();
	}

	public boolean isVendaCancelada() {
		return this.movimentacao.isCancelada();
	}

	public boolean isVendaEmAndamento() {
		return this.movimentacao.isEmAndamento();
	}

	public boolean isPossuiEntrada() {
		return isVendaEfetuada() && this.movimentacao.getEntrada() != null
				&& this.movimentacao.getEntrada().doubleValue() > 0;
	}

	public void emitirRelatorio() {
		String dataExtenso = "Manaus, " + Utils.dataPorExtenso(new Date()) + ".";
		Long id = movimentacao.getId();
		String nomeRel = "Contrato_" + numeroFormatado(movimentacao.getId()) + ".pdf";
		String caminhoLogo = FacesUtil.localFotos() + "/logo_prime_short.png";
		Map<String, Object> parametros = new HashMap<>();
		parametros.put("codigo", id);
		parametros.put("logo", caminhoLogo);
		parametros.put("dataextenso", dataExtenso);
		ExecutorRelatorio executor = new ExecutorRelatorio("/relatorios/rel_termo.jasper", this.response, parametros,
				nomeRel);

		Session session = manager.unwrap(Session.class);
		session.doWork(executor);

		if (executor.isRelatorioGerado()) {
			facesContext.responseComplete();
		} else {
			FacesUtil.addErroMessage("A execução do relatório não retornou dados.");
		}
	}

	private String numeroFormatado(Long id) {
		return String.format("%06d", id);
	}

	public void carregarArquivo(FileUploadEvent event) {
		if (event.getFile().getContents() != null && event.getFile().getSize() > 0) {
			movimentacao.setTermoAssinado(event.getFile().getContents());
			service.salvar(movimentacao);
			FacesUtil.addInfoMessage("Termo anexado com sucesso");
		}
	}

	public void limparTermoGravado() {
		movimentacao.setTermoAssinado(null);
		service.salvar(movimentacao);
		movimentacao = movimentacaoRepository.porId(movimentacao.getId());
		FacesUtil.addInfoMessage("Termo excluído com sucesso");
	}

	public void baixarComprovante() {
		String fotoDisco = gerarImagemDisco();
		String nomeDownload = "termo_" + movimentacao.getId().toString() + ".jpg";
		try {
			InputStream stream = new BufferedInputStream(new FileInputStream(fotoDisco));
			file = new DefaultStreamedContent(stream, "image/jpg", nomeDownload);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public String gerarImagemDisco() {
		movimentacao = movimentacaoRepository.porId(movimentacao.getId());
		String local = FacesUtil.localFotos();
		String nome = "termo.jpg";
		Path localFoto = FileSystems.getDefault().getPath(local, "temp");
		local = local + File.separator + "temp" + File.separator + nome;
		try {
			Files.createDirectories(localFoto);
			Path fotoTemp = localFoto.resolve(nome);
			Files.write(fotoTemp, movimentacao.getTermoAssinado());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return local;
	}

	public void emitirRecibo() {
		Long id = movimentacao.getId();
		String nomeRel = "Recibo_" + numeroFormatado(movimentacao.getId()) + ".pdf";
		String caminhoLogo = FacesUtil.localFotos() + "/logo_prime_short.png";
		Map<String, Object> parametros = new HashMap<>();
		parametros.put("codigo", id);
		parametros.put("logo", caminhoLogo);
		ExecutorRelatorio executor = new ExecutorRelatorio("/relatorios/recibo.jasper", this.response, parametros,
				nomeRel);

		Session session = manager.unwrap(Session.class);
		session.doWork(executor);

		if (executor.isRelatorioGerado()) {
			facesContext.responseComplete();
		} else {
			FacesUtil.addErroMessage("A execução do relatório não retornou dados.");
		}
	}

	public void adicionarVendedor() {
		boolean encontrou = false;
		for (ComissaoVenda comissaoVenda : comissoes) {
			if (comissaoVenda.getVendedor().equals(this.vendedor))
				encontrou = true;
		}
		if (encontrou == false) {
			ComissaoVenda comissaoVenda = new ComissaoVenda();
			comissaoVenda.setVendedor(this.vendedor);
			comissaoVenda.setComissao(this.vendedor.getValorComissao());
			comissaoVenda.setMovimentacao(this.movimentacao);
			this.comissoes.add(comissaoVenda);
			this.vendedor = new Vendedor();
		}
	}

	public void removerVendedor(ComissaoVenda comissaoVenda) {
		this.comissoes.remove(comissaoVenda);
	}

}
