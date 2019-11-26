package br.com.sis.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sis.entity.Aluguel;
import br.com.sis.entity.CheckList;
import br.com.sis.entity.Configuracoes;
import br.com.sis.entity.Pessoa;
import br.com.sis.entity.Veiculo;
import br.com.sis.repository.AluguelRepository;
import br.com.sis.repository.ConfiguracoesRepository;
import br.com.sis.repository.PagamentoSemanalRepository;
import br.com.sis.repository.PessoaRepository;
import br.com.sis.repository.VeiculoRepository;
import br.com.sis.service.AluguelService;
import br.com.sis.util.jsf.FacesUtil;

@Named
@ViewScoped
public class DevolucaoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private AluguelService aluguelService;
	@Inject
	private PessoaRepository pessoaRepository;
	@Inject
	private AluguelRepository aluguelRepository;
	@Inject
	private VeiculoRepository veiculoRepository;
	@Inject
	private ConfiguracoesRepository configuracoesRepository;
	@Inject
	private PagamentoSemanalRepository pagamentoSemanalRepository;

	private List<CheckList> checkList = new ArrayList<CheckList>();

	private Date diaEntrega;
	private Integer kmEntrega;

	private Aluguel aluguel;
	private Veiculo veiculo;
	private Pessoa cliente;

	public List<CheckList> getCheckList() {
		return checkList;
	}

	public void setCheckList(List<CheckList> checkList) {
		this.checkList = checkList;
	}

	public Date getDiaEntrega() {
		return diaEntrega;
	}

	public void setDiaEntrega(Date diaEntrega) {
		this.diaEntrega = diaEntrega;
	}

	public Integer getKmEntrega() {
		return kmEntrega;
	}

	public void setKmEntrega(Integer kmEntrega) {
		this.kmEntrega = kmEntrega;
	}

	public Aluguel getAluguel() {
		return aluguel;
	}

	public void setAluguel(Aluguel aluguel) {
		this.aluguel = aluguel;
	}

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	public Pessoa getCliente() {
		return cliente;
	}

	public void setCliente(Pessoa cliente) {
		this.cliente = cliente;
	}

	public BigDecimal getTotalCheckList() {
		double total = 0;
		for (CheckList item : checkList) {
			if (item.isEntrega() && !item.isRecebimento()) {
			  total = total + item.getValorItemCheckList().doubleValue();
			}
		}
		return new BigDecimal(total);
	}
	
	public String getFormatoMoeda() {
		NumberFormat nf = NumberFormat.getCurrencyInstance();  
		String formatado = nf.format (this.getTotalCheckList());		
		return formatado; 
	}	

	public void inicializar() {
		if (this.aluguel == null) {
			FacesUtil.redirecionarPagina("/alugueis/ListaAlugueis.xhtml");
		} else {
			checkList = this.aluguel.getCheckList();
			prepararDados();
		}
	}
	
	public void prepararDados() {
		cliente = pessoaRepository.porId(aluguel.getCliente().getId());
		veiculo = veiculoRepository.porId(aluguel.getVeiculo().getId());
	}
	
	public Long getDiasAtraso() {
		Calendar dtPrevista = Calendar.getInstance();
		dtPrevista.setTime(aluguel.getDataPrevista());
		Calendar dtEntrega = Calendar.getInstance();
		if (diaEntrega == null) {	
			return 0l;
		} else {
			dtEntrega.setTime(diaEntrega);
			int diai = dtPrevista.get(Calendar.DAY_OF_MONTH);
			int mesi = dtPrevista.get(Calendar.MONTH);
			int anoi = dtPrevista.get(Calendar.YEAR);

			int diaf = dtEntrega.get(Calendar.DAY_OF_MONTH);
			int mesf = dtEntrega.get(Calendar.MONTH);
			int anof = dtEntrega.get(Calendar.YEAR);

			LocalDate di = LocalDate.of(anoi, mesi, diai);
			LocalDate df = LocalDate.of(anof, mesf, diaf);

			Long dias = ChronoUnit.DAYS.between(di, df);
			if (dias < 0l)
				dias = 0l;
			
			return dias;			
		}
	}
	
	public Integer getKmExcedente() {
		if (kmEntrega == null) {
			return 0;
		} else {
			int kmentrega = kmEntrega;
			int km = aluguel.getKmFinal();
			int retorno = kmentrega - km;
			if (retorno < 0) {
				retorno = 0;
			}
			return retorno;
		}
	}
	
	public BigDecimal getValorDiasAtraso() {
		BigDecimal diaria = veiculo.getValorAluguel();
		return diaria.multiply(new BigDecimal(this.getDiasAtraso()));
	}
	
	public BigDecimal getValorKmExcedente() {
		Configuracoes configuracoes = configuracoesRepository.configuracoesGerais();
		BigDecimal valorKmExcedente = configuracoes.getValorKmExcedente();
		return valorKmExcedente.multiply(new BigDecimal(this.getKmExcedente()));
	}

}
