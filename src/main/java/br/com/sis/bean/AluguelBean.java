package br.com.sis.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.SelectEvent;

import br.com.sis.converter.PessoaConverter;
import br.com.sis.entity.Aluguel;
import br.com.sis.entity.Pessoa;
import br.com.sis.entity.Veiculo;
import br.com.sis.enuns.StatusAluguel;
import br.com.sis.repository.PessoaRepository;
import br.com.sis.repository.VeiculoRepository;
import br.com.sis.security.Seguranca;
import br.com.sis.util.jsf.FacesUtil;

@Named
@ViewScoped
public class AluguelBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private PessoaRepository pessoaRepository;
	@Inject
	private VeiculoRepository veiculoRepository;
	@Inject
	private PessoaConverter pessoaConverter;
	@Inject
	private Seguranca seguranca;

	private Aluguel aluguel;

	private String placa;

	public PessoaConverter getPessoaConverter() {
		return pessoaConverter;
	}

	public void setPessoaConverter(PessoaConverter pessoaConverter) {
		this.pessoaConverter = pessoaConverter;
	}

	public Aluguel getAluguel() {
		return aluguel;
	}

	public void setAluguel(Aluguel aluguel) {
		this.aluguel = aluguel;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}
	
	public List<Pessoa> completePorNome(String nome) {
		return pessoaRepository.clientesAtivosPorNome(nome);
	}
	
	public void calcularTotal() {
		if (this.getDias().longValue() > BigDecimal.ZERO.longValue() ) {
			this.aluguel.setTotal(this.aluguel.getDiaria().multiply(this.getDias()));
		}
	}
	
	public BigDecimal getDias() {
		BigDecimal retorno = BigDecimal.ZERO;
		if (this.aluguel.getDataInicio() != null && this.aluguel.getDataPrevista() != null) {
			Calendar ci = Calendar.getInstance();
			ci.setTime(this.aluguel.getDataInicio());
			Calendar cf = Calendar.getInstance();
			cf.setTime(this.aluguel.getDataPrevista());
			int diai = ci.get(Calendar.DAY_OF_MONTH);
			int mesi = ci.get(Calendar.MONTH);
			int anoi = ci.get(Calendar.YEAR);
			
			int diaf = cf.get(Calendar.DAY_OF_MONTH);
			int mesf = cf.get(Calendar.MONTH);
			int anof = cf.get(Calendar.YEAR);
			
			LocalDate di = LocalDate.of(anoi, mesi, diai);
			LocalDate df = LocalDate.of(anof, mesf, diaf);
			
			Long dias = ChronoUnit.DAYS.between(di, df);
			retorno = BigDecimal.valueOf(dias);
			
		} 
		return retorno;
	}
	
	public void inicializar() {
		if (aluguel == null) {
			aluguel = new Aluguel();
			aluguel.setFuncionario(seguranca.getUsuarioLogado().getUsuario().getPessoa());
			aluguel.setDataInicio(new Date());
			aluguel.setStatusAluguel(StatusAluguel.ABERTO);
		} else {
			this.placa = this.aluguel.getVeiculo().getPlaca();
		}
	}
	
	public void onClienteEscolhido(SelectEvent event) {
		Pessoa pes = (Pessoa) event.getObject();
		this.aluguel.setCliente(pes);
	}

	public void onVeiculoEscolhido(SelectEvent event) {
		Veiculo carro = (Veiculo) event.getObject();
		carro = veiculoRepository.porId(carro.getId());
		this.aluguel.setVeiculo(carro);
		this.aluguel.setDiaria(carro.getValorAluguel());
		this.placa = carro.getPlaca();
	}
	
	public void pesquisarPorPlaca() {
		if (StringUtils.isNotBlank(placa)) {
			Veiculo v = veiculoRepository.porPlaca(placa);
			if (v != null && !v.isParaAluguel()) {
				FacesUtil.addWarnMessage(
						"Veículo não disponível para aluguel. Somente veículos para aluguel podem ser alugados.");
				v = new Veiculo();
				aluguel.setDiaria(null);
			} else {
				if (v == null) {
					v = new Veiculo();
					aluguel.setDiaria(null);
				} else {
					aluguel.setDiaria(v.getValorAluguel());
				}
			}
			aluguel.setVeiculo(v);
		} else {
			aluguel.setVeiculo(new Veiculo());
		}
	}	

}
