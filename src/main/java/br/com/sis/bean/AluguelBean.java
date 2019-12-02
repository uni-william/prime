package br.com.sis.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
import br.com.sis.entity.CheckList;
import br.com.sis.entity.Configuracoes;
import br.com.sis.entity.ItemCheckList;
import br.com.sis.entity.Pessoa;
import br.com.sis.entity.Veiculo;
import br.com.sis.enuns.StatusAluguel;
import br.com.sis.repository.ConfiguracoesRepository;
import br.com.sis.repository.ItemCheckListRepository;
import br.com.sis.repository.PessoaRepository;
import br.com.sis.repository.VeiculoRepository;
import br.com.sis.security.Seguranca;
import br.com.sis.service.AluguelService;
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
	private ItemCheckListRepository itemCheckListRepository;

	@Inject
	private PessoaConverter pessoaConverter;
	@Inject
	private Seguranca seguranca;
	@Inject
	private AluguelService aluguelService;

	@Inject
	private ConfiguracoesRepository configuracoesRepository;

	private Aluguel aluguel;

	private String placa;

	private List<CheckList> checkLists = new ArrayList<CheckList>();

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

	public List<CheckList> getCheckLists() {
		return checkLists;
	}

	public void setCheckLists(List<CheckList> checkLists) {
		this.checkLists = checkLists;
	}

	public List<Pessoa> completePorNome(String nome) {
		return pessoaRepository.clientesAtivosPorNome(nome);
	}

	public void calcularTotal() {
		BigDecimal desconto = BigDecimal.ZERO;
		BigDecimal acrescimo = BigDecimal.ZERO;
		BigDecimal total = this.aluguel.getValorDiaria();

		if (this.getAluguel().getValorDesconto() != null) {
			desconto = this.getAluguel().getValorDesconto();
		}
		if (this.getAluguel().getValorAcrescimo() != null) {
			acrescimo = this.getAluguel().getValorAcrescimo();
		}

		if (this.getDias().longValue() > BigDecimal.ZERO.longValue()) {
			total = total.multiply(this.getDias());
			total = total.add(acrescimo);
			total = total.subtract(desconto);
			this.aluguel.setValorTotal(total);
		} else {
			this.aluguel.setValorTotal(null);
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
			mesi = mesi+1;
			mesf = mesf+1;
			if ((mesf == 4 || mesf == 6 || mesf == 9|| mesf == 11) && (diaf == 31)) {
				diaf = 30;
			}
			if (mesf == 2 && diaf == 31) {
				diaf = 28;
			}
			
			
			LocalDate di = LocalDate.of(anoi, mesi, diai);
			LocalDate df = LocalDate.of(anof, mesf, diaf);

			Long dias = ChronoUnit.DAYS.between(di, df);
			retorno = BigDecimal.valueOf(dias);

		}
		return retorno;
	}

	public void realizarAluguel() {
		if (this.aluguel.getVeiculo() == null) {
			FacesUtil.addErroMessage("Informe o veículo");
		} else if (this.aluguel.isPagamentoSemanal() && this.aluguel.getDataProximoPagamento() == null) {
			FacesUtil.addErroMessage(
					"Contratos com pagamento semanal é necessário informar o dia de pagamento da primeira semana");
		} else {
			this.aluguel.setCheckList(this.checkLists);
			this.aluguelService.realizarAluguel(this.aluguel);
			this.placa = "";
			FacesUtil.addInfoMessage("Aluguel realizado com sucesso!");
			aluguel = new Aluguel();
			aluguel.setFuncionario(seguranca.getUsuarioLogado().getUsuario().getPessoa());
			aluguel.setDataInicio(new Date());
			aluguel.setStatusAluguel(StatusAluguel.ABERTO);
			colocarDataPrevista();
			carregarLista();
		}
	}

	public void inicializar() {
		if (aluguel == null) {
			aluguel = new Aluguel();
			aluguel.setFuncionario(seguranca.getUsuarioLogado().getUsuario().getPessoa());
			aluguel.setDataInicio(new Date());
			colocarDataPrevista();
			carregarLista();
		} else {
			this.placa = this.aluguel.getVeiculo().getPlaca();
		}
	}

	private void colocarDataPrevista() {
		Calendar c = Calendar.getInstance();
		c.setTime(aluguel.getDataInicio());
		c.add(Calendar.DAY_OF_MONTH, 30);
		aluguel.setDataPrevista(c.getTime());
		aluguel.setStatusAluguel(StatusAluguel.ABERTO);
	}

	private void carregarLista() {
		checkLists.clear();
		List<ItemCheckList> lista = itemCheckListRepository.listAll();
		for (ItemCheckList item : lista) {
			CheckList checkList = new CheckList();
			checkList.setAluguel(aluguel);
			checkList.setItemCheckList(item);
			checkList.setQuantidade(BigDecimal.ONE);
			checkList.setEntrega(true);
			checkList.setRecebimento(false);
			checkLists.add(checkList);
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
		this.aluguel.setValorDiaria(carro.getValorAluguel());
		this.aluguel.setKmInicial(carro.getQuilometragem());
		this.aluguel.setKmFinal(carro.getQuilometragem() + this.config().getLimiteKmAluguel());
		this.placa = carro.getPlaca();
		calcularTotal();
	}

	public void pesquisarPorPlaca() {
		if (StringUtils.isNotBlank(placa)) {
			Veiculo v = veiculoRepository.porPlaca(placa);
			if (v != null && !v.isParaAluguel()) {
				FacesUtil.addWarnMessage(
						"Veículo não disponível para aluguel. Somente veículos para aluguel podem ser alugados.");
				v = new Veiculo();
				aluguel.setValorDiaria(null);
			} else {
				if (v == null) {
					v = new Veiculo();
					aluguel.setValorDiaria(null);
				} else {
					aluguel.setValorDiaria(v.getValorAluguel());
					aluguel.setKmInicial(v.getQuilometragem());
					aluguel.setKmFinal(v.getQuilometragem() + this.config().getLimiteKmAluguel());
					calcularTotal();
				}
			}
			aluguel.setVeiculo(v);
		} else {
			aluguel.setVeiculo(new Veiculo());
		}
	}

	private Configuracoes config() {
		return configuracoesRepository.configuracoesGerais();
	}
	
	public void atualizaKm() {
		aluguel.setKmFinal(aluguel.getKmInicial() + this.config().getLimiteKmAluguel());
	}

}
