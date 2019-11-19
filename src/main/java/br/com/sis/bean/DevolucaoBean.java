package br.com.sis.bean;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sis.entity.Aluguel;
import br.com.sis.service.AluguelService;
import br.com.sis.util.jsf.FacesUtil;

@Named
@ViewScoped
public class DevolucaoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private AluguelService aluguelService;

	private Aluguel aluguel;

	public Aluguel getAluguel() {
		return aluguel;
	}

	public void setAluguel(Aluguel aluguel) {
		this.aluguel = aluguel;
	}
	
	public void inicializar() {
		if (this.aluguel == null) {
			FacesUtil.redirecionarPagina("/alugueis/PesquisaDevolucoes.xhtml");
		}
	}

}
