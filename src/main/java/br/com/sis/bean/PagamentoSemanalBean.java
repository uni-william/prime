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
import br.com.sis.repository.PagamentoSemanalRepository;
import br.com.sis.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PagamentoSemanalBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private PagamentoSemanalRepository pagamentoSemanalRepository;

	private List<PagamentoSemanal> pagtosRealizados = new ArrayList<PagamentoSemanal>();
	private Aluguel contratoAluguel;
	private PagamentoSemanal pagamentoSemanal;
	private Date dataProximoPagamento;

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

	public void inicializar() {
		if (contratoAluguel != null) {
			prepararPagto();
		} else {
			FacesUtil.redirecionarPaginaComMensagem("/alugueis/ListaAlugueis.xhtml", "Não existe contrato selecionado");
		}
	}

	private void prepararPagto() {
		pagtosRealizados = pagamentoSemanalRepository.pagamentosPorContrato(contratoAluguel);
		pagamentoSemanal = new PagamentoSemanal();
		pagamentoSemanal.setAluguel(contratoAluguel);
		dataProximoPagamento = null;
	}

	public void salvarPagamento() {
		if (dataProximoPagamento != null) {
			Calendar cProximo = Calendar.getInstance();
			cProximo.setTime(dataProximoPagamento);
			Calendar cLimite = Calendar.getInstance();
			cLimite.setTime(contratoAluguel.getDataPrevista());
			if (cProximo.after(cLimite)) {
				FacesUtil.addErroMessage("A data do próximo pagamento não pode ser superior a data de entrega. Você deve efeutar somente o pagamento e renovar o contrato!");
			} else {
				pagamentoSemanal.getAluguel().setDataProximoPagamento(this.dataProximoPagamento);
				pagamentoSemanalRepository.salvar(pagamentoSemanal);
				FacesUtil.addInfoMessage("Pagamento realizado com sucesso!");
				prepararPagto();
			}
			
		} else {
			pagamentoSemanalRepository.salvar(pagamentoSemanal);
			FacesUtil.addInfoMessage("Pagamento realizado com sucesso!");
			prepararPagto();			
		}
	}

}
