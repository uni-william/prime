package br.com.sis.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import br.com.sis.entity.Fabricante;
import br.com.sis.repository.FabricanteRepository;

@FacesConverter(forClass = Fabricante.class)
public class FabricanteConverter implements Converter {

	@Inject
	private FabricanteRepository fabricanteRepository;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Fabricante fabricante = null;
		if (StringUtils.isNotBlank(value)) {
			Long id = new Long(value);
			fabricante = fabricanteRepository.porId(id);
		}
		return fabricante;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Fabricante fabricante = (Fabricante) value;
			return fabricante.getId() == null ? null : fabricante.getId().toString();
		}
		return "";
	}

}
