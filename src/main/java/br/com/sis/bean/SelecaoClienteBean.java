package br.com.sis.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.context.RequestContext;

import br.com.sis.entity.Pessoa;
import br.com.sis.repository.PessoaRepository;
import br.com.sis.repository.filter.PessoaFilter;

@Named
@ViewScoped
public class SelecaoClienteBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private PessoaRepository pessoaRepository;

	private List<Pessoa> clientesFiltrados = new ArrayList<Pessoa>();
	private PessoaFilter filter;
	private int indicePessoa;
	private String mascara;

	public PessoaFilter getFilter() {
		return filter;
	}

	public void setFilter(PessoaFilter filter) {
		this.filter = filter;
	}

	public int getIndicePessoa() {
		return indicePessoa;
	}

	public void setIndicePessoa(int indicePessoa) {
		this.indicePessoa = indicePessoa;
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

	public List<Pessoa> getClientesFiltrados() {
		return clientesFiltrados;
	}

	public void pesquisar() {
		clientesFiltrados = pessoaRepository.clientesFiltrados(filter);
	}

	public void selectClienteFromDialog(Pessoa cliente) {
		RequestContext.getCurrentInstance().closeDialog(cliente);
	}

	public void inicializar() {
		filter = new PessoaFilter();
		filter.setAtivo(true);
	}

	public void abrirSelecaoCliente() {
		Map<String, Object> options = new HashMap<String, Object>();
		options.put("resizable", false);
		options.put("contentHeight", 520);
		options.put("height", 520);
		options.put("modal", true);
		RequestContext.getCurrentInstance().openDialog("/dialogos/DialogCliente", options, null);
	}

}
