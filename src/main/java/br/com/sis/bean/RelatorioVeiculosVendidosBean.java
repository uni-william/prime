package br.com.sis.bean;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.Session;

import br.com.sis.entity.Configuracoes;
import br.com.sis.report.ExecutorRelatorio;
import br.com.sis.repository.ConfiguracoesRepository;
import br.com.sis.util.jsf.FacesUtil;

@Named
@ViewScoped
public class RelatorioVeiculosVendidosBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private FacesContext facesContext;

	@Inject
	private HttpServletResponse response;

	@Inject
	private EntityManager manager;
	
	@Inject
	private ConfiguracoesRepository configuracoesRepository;

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
	
	public void emitirRelatorio() {
		String caminhoLogo = FacesUtil.localFotos() + "/logoprime.png";
		Map<String, Object> parametros = new HashMap<>();
		parametros.put("logo", caminhoLogo);
		if (dtInicial != null) {
			parametros.put("dti", dtInicial);
		}
		if (dtFinal != null) {
			parametros.put("dtf", dtFinal);
		}		
		ExecutorRelatorio executor = new ExecutorRelatorio("/relatorios/rel_veiculos_vendidos.jasper", this.response, parametros,
				"Relatorio Veiculos Vendidos.pdf");

		Session session = manager.unwrap(Session.class);
		session.doWork(executor);

		if (executor.isRelatorioGerado()) {
			facesContext.responseComplete();
		} else {
			FacesUtil.addErroMessage("A execução do relatório não retornou dados.");
		}		
	}
	
	public void inicializar() {
		Configuracoes configuracoes = configuracoesRepository.configuracoesGerais();
		Calendar dt = Calendar.getInstance();
		dt = DateUtils.truncate(dt, Calendar.DAY_OF_MONTH);
		dt.add(Calendar.MONTH, - configuracoes.getMesesRetroativosGrficoBarras());
		dtInicial = dt.getTime();
		dtFinal = new Date();		
	}

}
