package br.com.sis.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sis.entity.Aluguel;
import br.com.sis.enuns.StatusAluguel;
import br.com.sis.repository.AluguelRepository;
import br.com.sis.repository.filter.AluguelFilter;

@Named
@ViewScoped
public class PesquisaDevolucoesBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private AluguelRepository aluguelRepository;

	private AluguelFilter filter;
	private int indicePessoa = 0;
	private List<Aluguel> alugueis = new ArrayList<>();

	public AluguelFilter getFilter() {
		return filter;
	}

	public void setFilter(AluguelFilter filter) {
		this.filter = filter;
	}

	public int getIndicePessoa() {
		return indicePessoa;
	}

	public void setIndicePessoa(int indicePessoa) {
		this.indicePessoa = indicePessoa;
	}

	public String getMascara() {
		if (this.indicePessoa == 0)
			return "999.999.999-99";
		else
			return "99.999.999/9999-99";
	}

	public List<Aluguel> getAlugueis() {
		return alugueis;
	}

	public void iniciliazar() {
		filter = new AluguelFilter();
		filter.setStatusAluguel(StatusAluguel.ABERTO);
	}
	
	public void pesquisar() {
		this.alugueis = aluguelRepository.filtrados(filter);
	}

}
