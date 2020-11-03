package br.com.sis.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sis.entity.vo.TotalCanaisVO;
import br.com.sis.repository.MovimentacaoRepository;

@Named
@ViewScoped
public class ConsultaCanaisDivulgacaoBean implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Inject
	private MovimentacaoRepository movimentacaoRepository;
	
	List<TotalCanaisVO> lista = new ArrayList<TotalCanaisVO>();

	public List<TotalCanaisVO> getLista() {
		return lista;
	}
	
	public void inicializar() {
		lista = movimentacaoRepository.canais();
		Long totalSemCanal = movimentacaoRepository.totalCanaisNulo();
		TotalCanaisVO semCanal = new TotalCanaisVO();
		semCanal.setDescricao("Não informado");
		semCanal.setTotal(totalSemCanal);
		lista.add(semCanal);
	}

}
