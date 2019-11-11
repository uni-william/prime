package br.com.sis.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import br.com.sis.entity.Intermediacao;
import br.com.sis.repository.IntermediacaoRepository;

@FacesConverter(forClass = Intermediacao.class)
public class IntermediacaoConverter implements Converter, Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private IntermediacaoRepository IntermediacaoRepository;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Intermediacao Intermediacao = null;
		if (StringUtils.isNotBlank(value)) {
			Long id = new Long(value);
			Intermediacao = IntermediacaoRepository.porId(id);
		}
		return Intermediacao;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Intermediacao Intermediacao = (Intermediacao) value;
			return Intermediacao.getId() == null ? null : Intermediacao.getId().toString();
		}
		return "";
	}

}
