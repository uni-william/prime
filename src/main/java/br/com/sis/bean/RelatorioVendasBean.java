package br.com.sis.bean;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import br.com.sis.report.ExecutorRelatorio;
import br.com.sis.util.jsf.FacesUtil;

@Named
@ViewScoped
public class RelatorioVendasBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private FacesContext facesContext;

	@Inject
	private HttpServletResponse response;

	@Inject
	private EntityManager manager;

	private Date dtInicial;
	private Date dtFinal;

	public Date getDtInicial() {
		return dtInicial;
	}

	public void setDtInicial(Date dtInicial) {
		this.dtInicial = dtInicial;
	}

	public Date getDtFinal() {
		return dtFinal;
	}

	public void setDtFinal(Date dtFinal) {
		this.dtFinal = dtFinal;
	}
	
	public void inicializar() {
		LocalDate dateIni = LocalDate.now();
		LocalDate dateFim = LocalDate.now();
		dateIni = dateIni.with(TemporalAdjusters.firstDayOfMonth());
		dateFim = dateFim.with(TemporalAdjusters.lastDayOfMonth());
		dtInicial = Date.from(dateIni.atStartOfDay(ZoneId.systemDefault()).toInstant());
		dtFinal = Date.from(dateFim.atStartOfDay(ZoneId.systemDefault()).toInstant());	
	}
	
	public void emitirRelatorio() {
		String caminhoLogo = FacesUtil.localFotos() + "/logo_prime_short.png";
		Map<String, Object> parametros = new HashMap<>();
		parametros.put("logo", caminhoLogo);
		if (dtInicial != null) {
			parametros.put("data_ini", dtInicial);
		}
		if (dtFinal != null) {
			parametros.put("data_fim", dtFinal);
		}		
		ExecutorRelatorio executor = new ExecutorRelatorio("/relatorios/rel_vendas_periodo.jasper", this.response, parametros,
				"Relatorio Vendas Periodo.pdf");

		Session session = manager.unwrap(Session.class);
		session.doWork(executor);

		if (executor.isRelatorioGerado()) {
			facesContext.responseComplete();
		} else {
			FacesUtil.addErroMessage("A execução do relatório não retornou dados.");
		}		
	}

}
