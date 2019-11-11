package br.com.sis.bean;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sis.entity.Pessoa;
import br.com.sis.enuns.Estado;
import br.com.sis.enuns.Tipo;
import br.com.sis.enuns.TipoPessoa;
import br.com.sis.service.PessoaService;

@Named
@ViewScoped
public class CadastroClienteBean implements Serializable {

	private static final long serialVersionUID = 1L;
	@Inject
	private PessoaService service;

	private Pessoa pessoa;

	private int idex;

	private String mascara;

	private int indicePessoa;

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

	public int getIdex() {
		return idex;
	}

	public void setIdex(int idex) {
		this.idex = idex;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public void inicializar() {
		if (pessoa == null) {
			pessoa = new Pessoa();
			pessoa.setAtivo(true);
			pessoa.setTipo(Tipo.CLIENTE);
			pessoa.setTipoPessoa(TipoPessoa.FISICA);
			indicePessoa = 0;
		} else {
			if (this.pessoa.isFisica()) {
				indicePessoa = 0;
			} else {
				indicePessoa = 1;
			}
		}
	}

	public boolean isEditando() {
		return pessoa != null && pessoa.getId() != null;
	}

	public Estado[] getEstados() {
		return Estado.values();
	}

	public TipoPessoa[] getTipoPessoas() {
		return TipoPessoa.values();
	}

	public void salvar() {
		pessoa = service.salvar(pessoa);
		this.idex = 0;
		inicializar();

	}

	public boolean isExibirDocumento() {
		return (this.pessoa.getTipoPessoa() != null) && (this.pessoa.isFisica() || this.pessoa.isJuridica());
	}

	public void atualizaTipoPessoa() {
		if (indicePessoa == 0  ) {
			this.pessoa.setTipoPessoa(TipoPessoa.FISICA);
		}  else if (indicePessoa == 1) {
			this.pessoa.setTipoPessoa(TipoPessoa.JURIDICA);
		}
	}

}
