package br.com.sis.bean;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sis.entity.Pessoa;
import br.com.sis.entity.Usuario;
import br.com.sis.entity.vo.CepVO;
import br.com.sis.enuns.Estado;
import br.com.sis.enuns.Tipo;
import br.com.sis.enuns.TipoPessoa;
import br.com.sis.repository.UsuarioRepository;
import br.com.sis.service.PessoaService;
import br.com.sis.util.Utils;

@Named
@ViewScoped
public class CadastroFuncionarioBean implements Serializable {

	private static final long serialVersionUID = 1L;
	@Inject
	private PessoaService service;

	@Inject
	private UsuarioRepository usuarioRepository;
	
	private Pessoa pessoa;

	private boolean podeTransformar;

	public boolean isPodeTransformar() {
		return podeTransformar;
	}

	public void setPodeTransformar(boolean podeTransformar) {
		this.podeTransformar = podeTransformar;
	}

	private int idex;

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
			podeTransformar = false;
			pessoa.setTipo(Tipo.FUNCIONARIO);
			pessoa.setTipoPessoa(TipoPessoa.FISICA);
		} else {
			Usuario user = usuarioRepository.porFuncionario(pessoa);
			if (user == null) {
				podeTransformar = true;
			} else {
				podeTransformar = false;
			}
		}
	}

	public boolean isEditando() {
		return pessoa != null && pessoa.getId() != null;
	}

	public Estado[] getEstados() {
		return Estado.values();
	}

	public void salvar() {
		pessoa = service.salvar(pessoa);
		this.idex = 0;
		inicializar();

	}
	
	public void carregarDadosCep() {
		CepVO cepVO = Utils.retonaDadosEndereco(this.pessoa.getCep());
		if (cepVO != null) {
			this.pessoa.setLogradouro(cepVO.getLogradouro());
			this.pessoa.setBairro(cepVO.getBairro());
			this.pessoa.setCidade(cepVO.getLocalidade());
			this.pessoa.setEstado(Estado.valueOf(cepVO.getUf()));
		}
	}

}
