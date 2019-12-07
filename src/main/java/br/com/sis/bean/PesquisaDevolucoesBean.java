package br.com.sis.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import br.com.sis.entity.Aluguel;
import br.com.sis.enuns.StatusAluguel;
import br.com.sis.report.ExecutorRelatorio;
import br.com.sis.repository.AluguelRepository;
import br.com.sis.repository.filter.AluguelFilter;
import br.com.sis.service.AluguelService;
import br.com.sis.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaDevolucoesBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private AluguelRepository aluguelRepository;

	@Inject
	private AluguelService aluguelService;
	
	@Inject
	private FacesContext facesContext;	
	
	@Inject
	private HttpServletResponse response;

	@Inject
	private EntityManager manager;
	
	

	private AluguelFilter filter;
	private int indicePessoa = 0;
	private List<Aluguel> alugueis = new ArrayList<>();

	private Aluguel aluguelSelecionado;
	private String value = "0";

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

	public Aluguel getAluguelSelecionado() {
		return aluguelSelecionado;
	}

	public void setAluguelSelecionado(Aluguel aluguelSelecionado) {
		this.aluguelSelecionado = aluguelSelecionado;
	}

	public StatusAluguel[] getStatusAluguel() {
		return StatusAluguel.values();
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void iniciliazar() {
		filter = new AluguelFilter();
	}

	public void pesquisar() {
		filter.setPagtoSemanal(null);
		if ("1".equals(value)) {
			filter.setPagtoSemanal(true);
		} else if ("2".equals(value)) {
			filter.setPagtoSemanal(false);
		}
		this.alugueis = aluguelRepository.filtrados(filter);
	}

	public void cancelarAluguel() {
		aluguelService.cancelarAluguel(aluguelSelecionado);
		FacesUtil.addInfoMessage("Contrato de aluguel cancelado com sucesso!");
		pesquisar();
	}
	
	
	public void emitirContrato() {
		Long id = aluguelSelecionado.getId();
		String nomeRel = "Contrato_" + numeroFormatado(id) + ".pdf";

		Map<String, Object> parametros = new HashMap<>();
		parametros.put("contrato", id);
		parametros.put("subreport", "/relatorios/check_list_contrato.jasper");
		ExecutorRelatorio executor = new ExecutorRelatorio("/relatorios/contrato_aluguel.jasper", this.response, parametros,
				nomeRel);

		Session session = manager.unwrap(Session.class);
		session.doWork(executor);

		if (executor.isRelatorioGerado()) {
			facesContext.responseComplete();
		} else {	
			FacesUtil.addErroMessage("A execução do relatório não retornou dados.");
		}
	}

	private String numeroFormatado(Long id) {
		return String.format("%06d", id);
	}		

}
