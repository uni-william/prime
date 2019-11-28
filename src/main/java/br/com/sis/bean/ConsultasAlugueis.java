package br.com.sis.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sis.entity.Aluguel;
import br.com.sis.repository.AluguelRepository;

@Named
@ViewScoped
public class ConsultasAlugueis implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private AluguelRepository aluguelRepository;

	private List<Aluguel> topAlugueis = new ArrayList<Aluguel>();

	private int dias = 7;
	private int top = 10;

	public int getDias() {
		return dias;
	}

	public void setDias(int dias) {
		this.dias = dias;
	}

	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
	}

	public List<Aluguel> getTopAlugueis() {
		return topAlugueis;
	}
	
	public void topAlugueisDevolucao() {
		topAlugueis = aluguelRepository.topContratosAVencerVencidos(top, dias);
	}
	
	public void topPagamentosSemanais() {
		topAlugueis = aluguelRepository.topPagamentosSemanalAVencerVencidos(top, dias);
	}

}
