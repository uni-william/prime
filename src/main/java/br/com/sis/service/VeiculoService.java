package br.com.sis.service;

import java.io.Serializable;

import javax.inject.Inject;

import br.com.sis.entity.Veiculo;
import br.com.sis.enuns.StatusVeiculo;
import br.com.sis.repository.VeiculoRepository;
import br.com.sis.util.jpa.Transactional;
import br.com.sis.util.jsf.FacesUtil;

public class VeiculoService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private VeiculoRepository veiculoRepository;

	@Transactional
	public Veiculo salvar(Veiculo veiculo) {
		Veiculo veiculoExistente = veiculoRepository.porChassi(veiculo.getChassi());
		if (veiculoExistente != null && !veiculoExistente.equals(veiculo)) {
			FacesUtil.addErroMessage("Já existe um veiculo cadastrado com esse chassi.");
			return null;
		} else {
			if (veiculo.getStatusVeiculo().equals(StatusVeiculo.PATIO) && veiculo.getValorVenda() == null) {
				FacesUtil.addErroMessage("Veículo no pátio necessita de preço de venda.");
				return null;
			} else {
				return veiculoRepository.salvar(veiculo);
			}
		}

	}

}
