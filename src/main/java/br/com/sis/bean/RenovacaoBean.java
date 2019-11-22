package br.com.sis.bean;

import java.io.Serializable;
import java.util.Calendar;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import br.com.sis.entity.Aluguel;
import br.com.sis.entity.Configuracoes;
import br.com.sis.entity.Pessoa;
import br.com.sis.entity.Veiculo;
import br.com.sis.enuns.StatusAluguel;
import br.com.sis.repository.AluguelRepository;
import br.com.sis.repository.ConfiguracoesRepository;
import br.com.sis.repository.PessoaRepository;
import br.com.sis.repository.VeiculoRepository;
import br.com.sis.service.AluguelService;
import br.com.sis.util.jsf.FacesUtil;

@Named
@ViewScoped
public class RenovacaoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private AluguelService aluguelService;
	@Inject
	private PessoaRepository pessoaRepository;
	@Inject
	private AluguelRepository aluguelRepository;
	@Inject
	private VeiculoRepository veiculoRepository;
	
	@Inject
	private ConfiguracoesRepository configuracoesRepository;	

	private Aluguel aluguel;
	private Veiculo veiculo;
	private Pessoa cliente;
	private Integer diasRenovacao;

	public Aluguel getAluguel() {
		return aluguel;
	}

	public void setAluguel(Aluguel aluguel) {
		this.aluguel = aluguel;
	}

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public Pessoa getCliente() {
		return cliente;
	}

	@NotNull(message = "Informe a quantidade de dias para renovação")
	public Integer getDiasRenovacao() {
		return diasRenovacao;
	}

	public void setDiasRenovacao(Integer diasRenovacao) {
		this.diasRenovacao = diasRenovacao;
	}

	public void prepararDados() {
		cliente = pessoaRepository.porId(aluguel.getCliente().getId());
		veiculo = veiculoRepository.porId(aluguel.getVeiculo().getId());
	}

	public void inicializar() {
		if (this.aluguel == null) {
			FacesUtil.redirecionarPagina("/alugueis/ListaAlugueis.xhtml");
		} else {
			prepararDados();
		}
	}
	
	public void realizarRenovacao() {
		veiculo = veiculoRepository.porId(veiculo.getId());
		veiculo.setQuilometragem(aluguel.getKmFinal());
		aluguel = aluguelRepository.porId(aluguel.getId());
		aluguel.setKmInicial(aluguel.getKmFinal());
		aluguel.setKmFinal(aluguel.getKmInicial() + config().getLimiteKmAluguel());
		aluguel.setDataInicio(aluguel.getDataPrevista());
		colocarDataPrevista();
		aluguel = aluguelService.realizarAluguel(aluguel);
		FacesUtil.addInfoMessage("Contrato renovado com sucesso!");
		diasRenovacao = null;
	}
	
	private Configuracoes config() {
		return configuracoesRepository.configuracoesGerais();
	}
	
	private void colocarDataPrevista() {
		Calendar c = Calendar.getInstance();
		c.setTime(aluguel.getDataInicio());
		c.add(Calendar.DAY_OF_MONTH, diasRenovacao);
		aluguel.setDataPrevista(c.getTime());
	}	

}
