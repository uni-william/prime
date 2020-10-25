package br.com.sis.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import br.com.sis.entity.Perfil;
import br.com.sis.repository.PerfilRepository;

@FacesConverter(forClass = Perfil.class)
public class PerfilConverter implements Converter {

	@Inject
	private PerfilRepository perfilRepository;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Perfil perfil = null;
		if (StringUtils.isNotBlank(value)) {
			Long id = new Long(value);
			perfil = perfilRepository.porId(id);
		}
		return perfil;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Perfil perfil = (Perfil) value;
			return perfil.getId() == null ? null : perfil.getId().toString();
		}
		return "";
	}

}
