package br.com.sis.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import br.com.sis.entity.Acessorio;
import br.com.sis.repository.AcessorioRepository;

@FacesConverter(forClass = Acessorio.class)
public class AcessorioConverter implements Converter, Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private AcessorioRepository acessorioRepository;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Acessorio acessorio = null;
		if (StringUtils.isNotBlank(value)) {
			Long id = new Long(value);
			acessorio = acessorioRepository.porId(id);
		}
		return acessorio;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Acessorio acessorio = (Acessorio) value;
			return acessorio.getId() == null ? null : acessorio.getId().toString();
		}
		return "";
	}

}
