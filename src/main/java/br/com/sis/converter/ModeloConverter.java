package br.com.sis.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import br.com.sis.entity.Modelo;
import br.com.sis.repository.ModeloRepository;

@FacesConverter(forClass = Modelo.class)
public class ModeloConverter implements Converter {

	@Inject
	private ModeloRepository ModeloRepository;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Modelo Modelo = null;
		if (StringUtils.isNotBlank(value)) {
			Long id = new Long(value);
			Modelo = ModeloRepository.porId(id);
		}
		return Modelo;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Modelo Modelo = (Modelo) value;
			return Modelo.getId() == null ? null : Modelo.getId().toString();
		}
		return "";
	}

}
