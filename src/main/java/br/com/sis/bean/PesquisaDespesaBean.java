package br.com.sis.bean;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import br.com.sis.entity.Despesa;
import br.com.sis.entity.TipoDespesa;
import br.com.sis.repository.DespesaRepository;
import br.com.sis.repository.TipoDespesaRepository;
import br.com.sis.repository.filter.DespesaFilter;
import br.com.sis.util.jsf.FacesUtil;

@Named
@ViewScoped
public class PesquisaDespesaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private DespesaRepository repository;

	@Inject
	private TipoDespesaRepository tipoDespesaRepository;

	private List<TipoDespesa> tipoDespesas;

	private List<Despesa> listaDespesas;
	private Despesa despesaSelecionado;

	private boolean desabilitarBotoes;

	private DespesaFilter filter;

	public List<TipoDespesa> getTipoDespesas() {
		return tipoDespesas;
	}

	public void setTipoDespesas(List<TipoDespesa> tipoDespesas) {
		this.tipoDespesas = tipoDespesas;
	}

	public boolean isDesabilitarBotoes() {
		return desabilitarBotoes;
	}

	public void setDesabilitarBotoes(boolean desabilitarBotoes) {
		this.desabilitarBotoes = desabilitarBotoes;
	}

	public Despesa getDespesaSelecionado() {
		return despesaSelecionado;
	}

	public void setDespesaSelecionado(Despesa despesaSelecionado) {
		this.despesaSelecionado = despesaSelecionado;
	}

	public List<Despesa> getlistaDespesas() {
		return listaDespesas;
	}

	public DespesaFilter getFilter() {
		return filter;
	}

	public void setFilter(DespesaFilter filter) {
		this.filter = filter;
	}

	public void inicializar() {
		tipoDespesas = tipoDespesaRepository.listAll();
		filter = new DespesaFilter();
		LocalDate dateIni = LocalDate.now();
		LocalDate dateFim = LocalDate.now();
		dateIni = dateIni.with(TemporalAdjusters.firstDayOfMonth());
		dateFim = dateFim.with(TemporalAdjusters.lastDayOfMonth());
		filter.setDtIni(Date.from(dateIni.atStartOfDay(ZoneId.systemDefault()).toInstant()));
		filter.setDtFim(Date.from(dateFim.atStartOfDay(ZoneId.systemDefault()).toInstant()));
		consultar();
	}

	public void consultar() {
		listaDespesas = repository.listAll(filter);
		despesaSelecionado = new Despesa();
		this.desabilitarBotoes = true;
	}

	public void excluir() {
		if (repository.remover(despesaSelecionado) == true) {
			despesaSelecionado = new Despesa();
			listaDespesas = repository.listAll(filter);
			FacesUtil.addInfoMessage("Registro excluído com sucesso!");
			setDesabilitarBotoes(true);
		}
	}

	public void onRowSelect(SelectEvent event) {
		desabilitarBotoes = false;
	}

	public void onRowUnSelect(UnselectEvent event) {
		desabilitarBotoes = true;
	}
}
