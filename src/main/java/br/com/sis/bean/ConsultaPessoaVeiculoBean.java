package br.com.sis.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sis.entity.Intermediacao;
import br.com.sis.entity.Movimentacao;
import br.com.sis.entity.Pessoa;
import br.com.sis.entity.Veiculo;
import br.com.sis.entity.vo.Transacao;
import br.com.sis.repository.IntermediacaoRepository;
import br.com.sis.repository.MovimentacaoRepository;
import br.com.sis.repository.PessoaRepository;
import br.com.sis.repository.VeiculoRepository;

@Named
@ViewScoped
public class ConsultaPessoaVeiculoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String documentoReceita;
	private String mascara;
	private int indicePessoa;
	private Pessoa pessoa;
	private String labelDocto;
	private int index;
	private String placa;
	private Veiculo veiculo;

	private List<Transacao> transacoesCliente = new ArrayList<Transacao>();
	private List<Transacao> transacoesVeiculo = new ArrayList<Transacao>();

	@Inject
	private PessoaRepository pessoaRepository;

	@Inject
	private MovimentacaoRepository movimentacaoRepository;

	@Inject
	private IntermediacaoRepository intermediacaoRepository;

	@Inject
	private VeiculoRepository veiculoRepository;

	public String getDocumentoReceita() {
		return documentoReceita;
	}

	public void setDocumentoReceita(String documentoReceita) {
		this.documentoReceita = documentoReceita;
	}

	public String getMascara() {
		if (this.indicePessoa == 0) {
			mascara = "999.999.999-99";
		} else if (this.indicePessoa == 1) {
			mascara = "99.999.999/9999-99";
		}
		return mascara;
	}

	public void setMascara(String mascara) {
		this.mascara = mascara;
	}

	public int getIndicePessoa() {
		return indicePessoa;
	}

	public void setIndicePessoa(int indicePessoa) {
		this.indicePessoa = indicePessoa;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public List<Transacao> getTransacoesCliente() {
		return transacoesCliente;
	}

	public List<Transacao> getTransacoesVeiculo() {
		return transacoesVeiculo;
	}

	public String getLabelDocto() {
		if (this.indicePessoa == 0) {
			labelDocto = "CPF";
		} else if (this.indicePessoa == 1) {
			labelDocto = "CNPJ";
		}
		return labelDocto;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	public void inicializar() {
		transacoesCliente.clear();
		transacoesVeiculo.clear();
		indicePessoa = 0;
		index = 0;
		documentoReceita = "";
		placa = "";
		pessoa = new Pessoa();
		veiculo = new Veiculo();
	}

	public void consultar() {
		if (this.index == 0) {
			consultarPessoa();
		} else {
			consultarVeiculo();
		}
	}
	
	public void limpar() {
		inicializar();
	}

	private void consultarVeiculo() {
		this.transacoesVeiculo.clear();
		this.veiculo = veiculoRepository.porPlaca(this.placa);
		if (veiculo != null) {
			List<Movimentacao> movimentacoes = movimentacaoRepository.movimentacoesPorVeic(veiculo);
			List<Intermediacao> intermediacoes = intermediacaoRepository.intermedicoesPorVeiculo(veiculo);
			for (Intermediacao intermediacao : intermediacoes) {
				Transacao transacao = new Transacao();
				transacao.setData(intermediacao.getData());
				transacao.setCliente(intermediacao.getCliente());
				transacao.setEntrada(intermediacao.getEntrada());
				transacao.setOperacao("Intermediação");
				transacao.setParceiro(intermediacao.getParceiro().getNome());
				transacao.setValor(intermediacao.getValorFinanciado());
				transacao.setVeiculo(intermediacao.getVeiculo());
				transacoesVeiculo.add(transacao);
			}
			for (Movimentacao movimentacao : movimentacoes) {
				Transacao transacao = new Transacao();
				transacao.setData(movimentacao.getData());
				transacao.setCliente(movimentacao.getCliente());
				transacao.setEntrada(movimentacao.getEntrada());
				transacao.setOperacao(movimentacao.getTipoOperacao().getDescricao());
				transacao.setValor(movimentacao.getValorFinanciamento());
				transacao.setVeiculo(movimentacao.getVeiculo());
				transacoesVeiculo.add(transacao);
			}
			
		}
	}

	private void consultarPessoa() {
		transacoesCliente.clear();
		this.pessoa = this.pessoaRepository.porCpf(this.documentoReceita);
		if (pessoa != null) {
			List<Movimentacao> movimentacoes = movimentacaoRepository.movimentacoesPorCliente(pessoa);
			List<Intermediacao> intermediacoes = intermediacaoRepository.intermedicoesPorCliente(pessoa);
			for (Intermediacao intermediacao : intermediacoes) {
				Transacao transacao = new Transacao();
				transacao.setData(intermediacao.getData());
				transacao.setCliente(intermediacao.getCliente());
				transacao.setEntrada(intermediacao.getEntrada());
				transacao.setOperacao("Intermediação");
				transacao.setParceiro(intermediacao.getParceiro().getNome());
				transacao.setValor(intermediacao.getValorFinanciado());
				transacao.setVeiculo(intermediacao.getVeiculo());
				transacoesCliente.add(transacao);
			}
			for (Movimentacao movimentacao : movimentacoes) {
				Transacao transacao = new Transacao();
				transacao.setData(movimentacao.getData());
				transacao.setCliente(movimentacao.getCliente());
				transacao.setEntrada(movimentacao.getEntrada());
				transacao.setOperacao(movimentacao.getTipoOperacao().getDescricao());
				transacao.setValor(movimentacao.getValorFinanciamento());
				transacao.setVeiculo(movimentacao.getVeiculo());
				transacoesCliente.add(transacao);
			}
		}
	}
}