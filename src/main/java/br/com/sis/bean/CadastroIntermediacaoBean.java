package br.com.sis.bean;

import java.io.Serializable;
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
import org.hibernate.validator.constraints.NotEmpty;
import org.primefaces.event.SelectEvent;

import br.com.sis.converter.PessoaConverter;
import br.com.sis.entity.Banco;
import br.com.sis.entity.Intermediacao;
import br.com.sis.entity.Pessoa;
import br.com.sis.entity.Veiculo;
import br.com.sis.report.ExecutorRelatorio;
import br.com.sis.repository.BancoRepository;
import br.com.sis.repository.PessoaRepository;
import br.com.sis.repository.VeiculoRepository;
import br.com.sis.security.Seguranca;
import br.com.sis.service.IntermediacaoService;
import br.com.sis.util.Utils;
import br.com.sis.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroIntermediacaoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private PessoaRepository pessoaRepository;
	@Inject
	private VeiculoRepository veiculoRepository;
	@Inject
	private IntermediacaoService service;
	@Inject
	private Seguranca seguranca;
	@Inject
	private BancoRepository bancoRepository;

	@Inject
	private PessoaConverter clienteConverter;
	@Inject
	private PessoaConverter parceiroConverter;
	
	@Inject
	private FacesContext facesContext;

	@Inject
	private HttpServletResponse response;

	@Inject
	private EntityManager manager;
	

	private Intermediacao intermediacao;

	private List<Banco> bancos;

	@NotEmpty
	private String placa;

	public List<Banco> getBancos() {
		return bancos;
	}

	public void setBancos(List<Banco> bancos) {
		this.bancos = bancos;
	}

	public PessoaConverter getClienteConverter() {
		return clienteConverter;
	}

	public void setClienteConverter(PessoaConverter clienteConverter) {
		this.clienteConverter = clienteConverter;
	}

	public PessoaConverter getParceiroConverter() {
		return parceiroConverter;
	}

	public void setParceiroConverter(PessoaConverter parceiroConverter) {
		this.parceiroConverter = parceiroConverter;
	}

	public Intermediacao getIntermediacao() {
		return intermediacao;
	}

	public void setIntermediacao(Intermediacao intermediacao) {
		this.intermediacao = intermediacao;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public void inicializar() {
		bancos = bancoRepository.listAll();
		if (intermediacao == null) {
			intermediacao = new Intermediacao();
			intermediacao.setFuncionario(seguranca.getUsuarioLogado().getUsuario().getPessoa());
			intermediacao.setData(new Date());
		} else {
			this.placa = this.intermediacao.getVeiculo().getPlaca();
		}
	}

	public List<Pessoa> completePorNome(String nome) {
		return pessoaRepository.clientesAtivosPorNome(nome);
	}

	public List<Pessoa> completePorNomeParceiro(String nome) {
		return pessoaRepository.parceirosAtivosPorNome(nome);
	}

	public void efetuarIntermediacao() {
		intermediacao = service.efetuarIntermediacao(intermediacao);
		if (intermediacao != null) {
			FacesUtil.addInfoMessage("Operação realizada com sucesso");
		}
	}

	public void onClienteEscolhido(SelectEvent event) {
		Pessoa pes = (Pessoa) event.getObject();
		this.intermediacao.setCliente(pes);
	}

	public void onVeiculoEscolhido(SelectEvent event) {
		Veiculo carro = (Veiculo) event.getObject();
		carro = veiculoRepository.porId(carro.getId());
		this.intermediacao.setVeiculo(carro);
		this.intermediacao.setValorFinanciado(carro.getValorVenda());
		this.placa = carro.getPlaca();
	}

	public void pesquisarPorPlaca() {
		if (StringUtils.isNotBlank(placa)) {
			Veiculo v = veiculoRepository.porPlaca(placa);
			if (v != null && !v.isNoPatio()) {
				FacesUtil.addWarnMessage(
						"Veículo não disponível para venda. Somente veículos no pátio podem ser vendidos.");
				v = new Veiculo();
				intermediacao.setValorFinanciado(null);
			} else {
				if (v == null) {
					v = new Veiculo();
					intermediacao.setValorFinanciado(null);
				} else {
					intermediacao.setValorFinanciado(v.getValorVenda());
				}
			}
			intermediacao.setVeiculo(v);
		} else {
			intermediacao.setVeiculo(new Veiculo());
		}
	}

	public boolean isIntermediacaoEfetuada() {
		return this.intermediacao.isConcluida();
	}
	
	public boolean isIntermediacaoCancelada() {
		return this.intermediacao.isCancelada();
	}	
	
	public boolean isIntermediacaoEmAndamento() {
		return this.intermediacao.isEmAndamento();
	}	
	
	private String numeroFormatado(Long id) {
		return String.format("%06d", id);
	}	
	
	public void emitirRelatorio() {
		String dataExtenso = "Manaus, " + Utils.dataPorExtenso(new Date()) + ".";
		Long id = intermediacao.getId();
		String nomeRel = "Intemediacao_" + numeroFormatado(intermediacao.getId()) + ".pdf";
		String caminhoLogo = FacesUtil.localFotos() + "/logoprime.png";
		Map<String, Object> parametros = new HashMap<>();
		parametros.put("codigo", id);
		parametros.put("logo", caminhoLogo);
		parametros.put("dataextenso", dataExtenso);
		ExecutorRelatorio executor = new ExecutorRelatorio("/relatorios/rel_termo_parceiro.jasper", this.response, parametros,
				nomeRel);

		Session session = manager.unwrap(Session.class);
		session.doWork(executor);

		if (executor.isRelatorioGerado()) {
			facesContext.responseComplete();
		} else {
			FacesUtil.addErroMessage("A execução do relatório não retornou dados.");
		}
	}
	
	public boolean isPossuiEntrada() {
		return isIntermediacaoEfetuada() && this.intermediacao.getEntrada() != null
				&& this.intermediacao.getEntrada().doubleValue() > 0;
	}	
	
	public void emitirRecibo() {
		Long id = intermediacao.getId();
		String nomeRel = "Recibo_" + numeroFormatado(intermediacao.getId()) + ".pdf";
		String caminhoLogo = FacesUtil.localFotos() + "/logoprime.png";
		Map<String, Object> parametros = new HashMap<>();
		parametros.put("codigo", id);
		parametros.put("logo", caminhoLogo);
		ExecutorRelatorio executor = new ExecutorRelatorio("/relatorios/recibo_parceiro.jasper", this.response, parametros,
				nomeRel);

		Session session = manager.unwrap(Session.class);
		session.doWork(executor);

		if (executor.isRelatorioGerado()) {
			facesContext.responseComplete();
		} else {
			FacesUtil.addErroMessage("A execução do relatório não retornou dados.");
		}
	}	
	
}
