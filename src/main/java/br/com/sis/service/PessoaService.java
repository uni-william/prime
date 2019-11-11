package br.com.sis.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.sis.entity.Pessoa;
import br.com.sis.repository.PessoaRepository;
import br.com.sis.util.Utils;
import br.com.sis.util.jpa.Transactional;
import br.com.sis.util.jsf.FacesUtil;

public class PessoaService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private PessoaRepository repository;

	@Transactional
	public Pessoa salvar(Pessoa pessoa) {
		String docReceita = Utils.removerCaracter(Utils.removerCaracter(Utils.removerCaracter(pessoa.getDocumentoReceita(), "."), "-"), "/");
		Pessoa pesExistente = repository.porCpf(docReceita);
		if (pesExistente != null && !pesExistente.equals(pessoa)) {
			FacesUtil.addErroMessage("Já existe uma pessoa cadastrada com esse CPF.");
			return null;
		} else {
			pessoa.setDocumentoReceita(docReceita);
			Pessoa funcSalvo = repository.salvar(pessoa);
			if (funcSalvo != null) {
				FacesUtil.addInfoMessage("Regitro salvo com sucesso");
				return funcSalvo;
			} else {
				return pessoa;
			}
		}
	}

}
