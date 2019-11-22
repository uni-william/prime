package br.com.sis.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.time.DateUtils;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.PieChartModel;

import br.com.sis.entity.Configuracoes;
import br.com.sis.entity.Usuario;
import br.com.sis.entity.vo.CarrosMaisVendidos;
import br.com.sis.entity.vo.TotalIntermediacao;
import br.com.sis.entity.vo.TotalPorData;
import br.com.sis.entity.vo.TotalPorOperacao;
import br.com.sis.enuns.TipoOperacao;
import br.com.sis.repository.ConfiguracoesRepository;
import br.com.sis.repository.IntermediacaoRepository;
import br.com.sis.repository.MovimentacaoRepository;
import br.com.sis.repository.UsuarioRepository;
import br.com.sis.security.Seguranca;
import br.com.sis.util.Utils;
import br.com.sis.util.jsf.FacesUtil;

@Named
@ViewScoped
public class DashBoardBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private MovimentacaoRepository movimentacaoRepository;

	@Inject
	private IntermediacaoRepository intermediacaoRepository;

	@Inject
	private ConfiguracoesRepository configuracoesRepository;

	@Inject
	private UsuarioRepository usuarioRepository;

	@Inject
	private HttpServletRequest request;

	@Inject
	private Seguranca seguranca;

	private Date dataInicial;
	private Date dataFinal;

	private List<TotalPorOperacao> operacoes;
	private PieChartModel pie;
	private LineChartModel lines;
	private BarChartModel barModel;
	private int dias;
	private int meses;
	private Configuracoes configuracoes = new Configuracoes();

	public BarChartModel getBarModel() {
		return barModel;
	}

	public void setBarModel(BarChartModel barModel) {
		this.barModel = barModel;
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public List<TotalPorOperacao> getOperacoes() {
		return operacoes;
	}

	public void setOperacoes(List<TotalPorOperacao> operacoes) {
		this.operacoes = operacoes;
	}

	public PieChartModel getPie() {
		return pie;
	}

	public LineChartModel getLines() {
		return lines;
	}

	public void setPie(PieChartModel pie) {
		this.pie = pie;
	}

	public void setLines(LineChartModel lines) {
		this.lines = lines;
	}

	public void inicializar() {
		if ("true".equals(request.getParameter("trocada"))) {

		} else {
			Usuario usuario = usuarioRepository.porId(seguranca.getUsuarioLogado().getUsuario().getId());
			if (usuario.isTrocarSenha()) {
				FacesUtil.redirecionarPagina("/TrocaSenha.xhtml?troca=true");
			}
		}

		configuracoes = configuracoesRepository.configuracoesGerais();
		dias = configuracoes.getDiasRetroativosGraficoPizzaELinhas();
		meses = configuracoes.getMesesRetroativosGrficoBarras();
		Calendar dt = Calendar.getInstance();
		dt = DateUtils.truncate(dt, Calendar.DAY_OF_MONTH);
		dt.add(Calendar.DAY_OF_MONTH, -dias);
		dataInicial = dt.getTime();
		dataFinal = new Date();
		pesquisar();
	}

	public void preencherGraficoPizza() {
		operacoes = movimentacaoRepository.totalPorOperacaoJPA(dataInicial, dataFinal);
		List<TotalIntermediacao> intermediacoes = intermediacaoRepository.totalIntermedicaoJPA(dataInicial, dataFinal);
		Map<TipoOperacao, BigDecimal> mapa = new HashMap<>();
		List<TipoOperacao> lista = Arrays.asList(TipoOperacao.values());
		for (TipoOperacao t : lista) {
			mapa.put(t, BigDecimal.ZERO);
		}
		for (TotalPorOperacao tpo : operacoes) {
			mapa.put(tpo.getTipoOperacao(), tpo.getValor());
		}
		
		for (TotalIntermediacao ti : intermediacoes) {
			mapa.put(TipoOperacao.INTERMEDIACAO,  ti.getValor() != null ? ti.getValor() : BigDecimal.ZERO);
		}
		pie = new PieChartModel();
		pie.setTitle("Total de Movimentações Realizadas nos últimos " + dias + " dias");
		pie.setLegendPosition("w");
		pie.setShowDataLabels(true);
		for (Map.Entry<TipoOperacao, BigDecimal> saida : mapa.entrySet()) {
			TipoOperacao tipoOperacao = saida.getKey();
			BigDecimal valor = saida.getValue();
			pie.set(tipoOperacao.getDescricao(), valor);

		}
	}

	public void pesquisar() {
		carregarGraficoLinhas();
		preencherGraficoPizza();
		carregarGraficoBarras();

	}

	public void carregarGraficoBarras() {
		Calendar dt = Calendar.getInstance();
		Calendar df = Calendar.getInstance();
		dt = DateUtils.truncate(dt, Calendar.DAY_OF_MONTH);
		dt.add(Calendar.MONTH, -meses);

		List<CarrosMaisVendidos> maisVendidos = movimentacaoRepository.carrosMaisVendidos(dt.getTime(), df.getTime());
		barModel = new BarChartModel();
		ChartSeries modelos = new ChartSeries();
		modelos.setLabel("Modelo");
		for (CarrosMaisVendidos c : maisVendidos) {
			modelos.set(c.getModelo().getModeloMotoriacao(), c.getQtd());
		}

		barModel.addSeries(modelos);

		barModel.setTitle("Veículos mais Vendidos nos últimos " + meses + " meses");
		barModel.setLegendPosition("ne");
		barModel.setAnimate(true);

		Axis xAxis = barModel.getAxis(AxisType.X);
		xAxis.setLabel("Modelos");

		Axis yAxis = barModel.getAxis(AxisType.Y);
		yAxis.setLabel("Quantidade");
		yAxis.setMin(0);
		yAxis.setMax(20);
	}

	public void carregarGraficoLinhas() {
		Calendar dtIni = Calendar.getInstance();
		Calendar dtFim = Calendar.getInstance();
		dtIni.setTime(dataInicial);
		dtFim.setTime(dataFinal);
		Map<String, BigDecimal> vendas = criarMapaVazio(dtIni, dtFim);
		Map<String, BigDecimal> compras = criarMapaVazio(dtIni, dtFim);
		Map<String, BigDecimal> intermediacoes = criarMapaVazio(dtIni, dtFim);

		List<TotalPorData> vendasDB = movimentacaoRepository.totalPorData(dataInicial, dataFinal, TipoOperacao.VENDA);
		List<TotalPorData> comprasDB = movimentacaoRepository.totalPorData(dataInicial, dataFinal, TipoOperacao.COMPRA);
		List<TotalPorData> intermediacoesDB = intermediacaoRepository.totalPorData(dataInicial, dataFinal);

		for (TotalPorData dataVenda : vendasDB) {
			vendas.put(Utils.dataFormatada(dataVenda.getData()), dataVenda.getValor());
		}

		for (TotalPorData dataCompra : comprasDB) {
			compras.put(Utils.dataFormatada(dataCompra.getData()), dataCompra.getValor());
		}

		for (TotalPorData dataIntermediacao : intermediacoesDB) {
			intermediacoes.put(Utils.dataFormatada(dataIntermediacao.getData()), dataIntermediacao.getValor());
		}

		lines = criarGraficoLinhas(vendas, compras, intermediacoes);

		lines.setTitle("Vendas, Compras e Intermediações dos últimos " + dias + " dias");
		lines.setLegendPosition("e");
		lines.setShowPointLabels(true);
		lines.setAnimate(true);
		Axis yAxis = lines.getAxis(AxisType.Y);
		lines.getAxes().put(AxisType.X, new CategoryAxis("Dias"));
		yAxis.setLabel("Valores");
		yAxis.setMin(0);
		yAxis.setMax(configuracoes.getLimiteGraficoVendas());
	}

	private static Map<String, BigDecimal> criarMapaVazio(Calendar dtIni, Calendar dtFim) {
		Map<String, BigDecimal> mapaInicial = new TreeMap<>();
		dtIni = (Calendar) dtIni.clone();
		while (!dtIni.after(dtFim)) {
			mapaInicial.put(Utils.dataFormatada(dtIni.getTime()), BigDecimal.ZERO);
			dtIni.add(Calendar.DAY_OF_MONTH, 1);
		}
		return mapaInicial;
	}

	private LineChartModel criarGraficoLinhas(Map<String, BigDecimal> vendas, Map<String, BigDecimal> compras,
			Map<String, BigDecimal> intermediacoes) {
		LineChartModel model = new LineChartModel();

		ChartSeries series1 = new ChartSeries();
		series1.setLabel("Vendas");

		for (String data : vendas.keySet()) {
			series1.set(data, vendas.get(data));
		}

		ChartSeries series2 = new ChartSeries();
		series2.setLabel("Compras");

		for (String data : compras.keySet()) {
			series2.set(data, compras.get(data));
		}
		
		ChartSeries series3 = new ChartSeries();
		series3.setLabel("Intermediações");

		for (String data : intermediacoes.keySet()) {
			series3.set(data, intermediacoes.get(data));
		}		

		model.addSeries(series1);
		model.addSeries(series2);
		model.addSeries(series3);

		return model;

	}

}
