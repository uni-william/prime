package br.com.sis.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import br.com.sis.entity.Veiculo;
import br.com.sis.repository.VeiculoRepository;

@FacesConverter(forClass = Veiculo.class)
public class VeiculoConverter implements Converter {

	@Inject
	private VeiculoRepository veiculoRepository;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Veiculo veiculo = null;
		if (StringUtils.isNotBlank(value)) {
			Long id = new Long(value);
			veiculo = veiculoRepository.porId(id);
		}
		return veiculo;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Veiculo veiculo = (Veiculo) value;
			return veiculo.getId() == null ? null : veiculo.getId().toString();
		}
		return "";
	}

}
