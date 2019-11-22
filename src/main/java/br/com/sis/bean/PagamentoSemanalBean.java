package br.com.sis.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sis.entity.Aluguel;
import br.com.sis.entity.PagamentoSemanal;
import br.com.sis.entity.Pessoa;
import br.com.sis.entity.Veiculo;
import br.com.sis.repository.AluguelRepository;
import br.com.sis.repository.PagamentoSemanalRepository;
import br.com.sis.repository.PessoaRepository;
import br.com.sis.repository.VeiculoRepository;
import br.com.sis.service.PagamentoSemanalService;
import br.com.sis.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PagamentoSemanalBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private PagamentoSemanalRepository pagamentoSemanalRepository;

	@Inject
	private PagamentoSemanalService pagamentoSemanalService;

	@Inject
	private PessoaRepository pessoaRepository;

	@Inject
	private AluguelRepository aluguelRepository;

	@Inject
	private VeiculoRepository veiculoRepository;

	private Veiculo veiculo;

	private Pessoa cliente;

	private List<PagamentoSemanal> pagtosRealizados = new ArrayList<PagamentoSemanal>();
	private Aluguel contratoAluguel;
	private PagamentoSemanal pagamentoSemanal;
	private Date dataProximoPagamento;
	private PagamentoSemanal pagamentoSelecionado;

	public Pessoa getCliente() {
		return cliente;
	}

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public Aluguel getContratoAluguel() {
		return contratoAluguel;
	}

	public void setContratoAluguel(Aluguel contratoAluguel) {
		this.contratoAluguel = contratoAluguel;
	}

	public PagamentoSemanal getPagamentoSemanal() {
		return pagamentoSemanal;
	}

	public void setPagamentoSemanal(PagamentoSemanal pagamentoSemanal) {
		this.pagamentoSemanal = pagamentoSemanal;
	}

	public Date getDataProximoPagamento() {
		return dataProximoPagamento;
	}

	public void setDataProximoPagamento(Date dataProximoPagamento) {
		this.dataProximoPagamento = dataProximoPagamento;
	}

	public List<PagamentoSemanal> getPagtosRealizados() {
		return pagtosRealizados;
	}

	public PagamentoSemanal getPagamentoSelecionado() {
		return pagamentoSelecionado;
	}

	public void setPagamentoSelecionado(PagamentoSemanal pagamentoSelecionado) {
		this.pagamentoSelecionado = pagamentoSelecionado;
	}

	public void inicializar() {
		if (contratoAluguel != null) {
			prepararPagto();
		} else {
			FacesUtil.redirecionarPaginaComMensagem("/alugueis/ListaAlugueis.xhtml", "Não existe contrato selecionado");
		}
	}

	private void prepararPagto() {
		cliente = pessoaRepository.porId(contratoAluguel.getCliente().getId());
		veiculo = veiculoRepository.porId(contratoAluguel.getVeiculo().getId());
		pagtosRealizados = pagamentoSemanalRepository.pagamentosPorContrato(contratoAluguel);
		pagamentoSemanal = new PagamentoSemanal();
		pagamentoSemanal.setAluguel(contratoAluguel);
		dataProximoPagamento = null;
	}

	public void excluirPagto() {
		pagamentoSemanalRepository.remover(pagamentoSelecionado);
		prepararPagto();
		FacesUtil.addInfoMessage("Pagamnto excluído com sucesso!");
	}

	public void imprimirRecibo() {
		FacesUtil.redirecionarPagina("alugueis/PagamentoSemanal.xhtml?aluguel=" + contratoAluguel.getId().toString());
		System.out.println("Recibo impresso " + pagamentoSelecionado.getId().toString());
	}

	public void salvarPagamento() {
		contratoAluguel = aluguelRepository.porId(pagamentoSemanal.getAluguel().getId());
		if (pagamentoSemanal.getDataPagto() == null || pagamentoSemanal.getValorPago() == null) {
			FacesUtil.addErroMessage("Valor pago e Data de pagto devem ser informados");
		} else {
			if (dataProximoPagamento != null) {
				Calendar cProximo = Calendar.getInstance();
				cProximo.setTime(dataProximoPagamento);
				Calendar cLimite = Calendar.getInstance();
				cLimite.setTime(contratoAluguel.getDataPrevista());
				if (cProximo.after(cLimite)) {
					FacesUtil.addErroMessage(
							"A data do próximo pagamento não pode ser superior a data de entrega. Você deve efeutar somente o pagamento e renovar o contrato!");
				} else {
					pagamentoSemanal.setAluguel(contratoAluguel);
					pagamentoSemanal.getAluguel().setDataProximoPagamento(this.dataProximoPagamento);
					pagamentoSemanalService.salvar(pagamentoSemanal);
					FacesUtil.addInfoMessage("Pagamento realizado com sucesso!");
					prepararPagto();
				}

			} else {
				pagamentoSemanalService.salvar(pagamentoSemanal);
				FacesUtil.addInfoMessage("Pagamento realizado com sucesso!");
				prepararPagto();
			}
		}
	}

}
