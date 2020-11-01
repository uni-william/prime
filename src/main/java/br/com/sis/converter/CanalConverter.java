package br.com.sis.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import br.com.sis.entity.Canal;
import br.com.sis.repository.CanalRepository;

@FacesConverter(forClass = Canal.class)
public class CanalConverter implements Converter, Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private CanalRepository canalRepository;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Canal canal = null;
		if (StringUtils.isNotBlank(value)) {
			Long id = new Long(value);
			canal = canalRepository.porId(id);
		}
		return canal;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Canal canal = (Canal) value;
			return canal.getId() == null ? null : canal.getId().toString();
		}
		return "";
	}

}
