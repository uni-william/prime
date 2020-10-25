package br.com.sis.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import br.com.sis.entity.Aluguel;
import br.com.sis.repository.AluguelRepository;

@FacesConverter(forClass = Aluguel.class)
public class AluguelConverter implements Converter, Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private AluguelRepository aluguelRepository;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Aluguel aluguel = null;
		if (StringUtils.isNotBlank(value)) {
			Long id = new Long(value);
			aluguel = aluguelRepository.porId(id);
		}
		return aluguel;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Aluguel aluguel = (Aluguel) value;
			return aluguel.getId() == null ? null : aluguel.getId().toString();
		}
		return "";
	}

}
