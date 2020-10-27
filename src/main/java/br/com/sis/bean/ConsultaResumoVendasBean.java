package br.com.sis.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sis.entity.vo.ResumoVendasVO;
import br.com.sis.repository.ManutencaoRepository;
import br.com.sis.repository.MovimentacaoRepository;

@Named
@ViewScoped
public class ConsultaResumoVendasBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private MovimentacaoRepository movimentacaoRepository;
	
	@Inject
	private ManutencaoRepository manutencaoRepository;
	
	private List<ResumoVendasVO> listresumoVendas = new ArrayList<ResumoVendasVO>();
	
	private Date dataIni;
	private Date dataFim;
	public Date getDataIni() {
		return dataIni;
	}
	public void setDataIni(Date dataIni) {
		this.dataIni = dataIni;
	}
	public Date getDataFim() {
		return dataFim;
	}
	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}
	public List<ResumoVendasVO> getListresumoVendas() {
		return listresumoVendas;
	}
	
	public void inicializar() {
		LocalDate dateIni = LocalDate.now();
		LocalDate dateFim = LocalDate.now();
		dateIni = dateIni.with(TemporalAdjusters.firstDayOfMonth());
		dateFim = dateFim.with(TemporalAdjusters.lastDayOfMonth());
		dataIni = Date.from(dateIni.atStartOfDay(ZoneId.systemDefault()).toInstant());
		dataFim = Date.from(dateFim.atStartOfDay(ZoneId.systemDefault()).toInstant());		
	}
	
	public void consultar() {
		List<ResumoVendasVO> lista = movimentacaoRepository.listResumoVendas(dataIni, dataFim);
		listresumoVendas.clear();
		for (ResumoVendasVO vendasVO : lista) {
			vendasVO.setValorManutencao(this.totalManutencoes(vendasVO.getVeiculoId()));
			listresumoVendas.add(vendasVO);
			
		}
		
		
	}
	
	private BigDecimal totalManutencoes(Long id) {
		return manutencaoRepository.totalManutencoesPorVeiculoId(id);
	}
	
	public BigDecimal getTotalSaldo() {
		BigDecimal totalSaldo = BigDecimal.ZERO;
		for (ResumoVendasVO resumoVendasVO : listresumoVendas) {
			totalSaldo = totalSaldo.add(resumoVendasVO.getSaldo());
		}
		return totalSaldo;
	}

}
