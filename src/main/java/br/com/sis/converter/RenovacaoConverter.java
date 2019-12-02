package br.com.sis.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import br.com.sis.entity.Renovacao;
import br.com.sis.repository.RenovacaoRepository;

@FacesConverter(forClass = Renovacao.class)
public class RenovacaoConverter implements Converter, Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private RenovacaoRepository renovacaoRepository;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Renovacao renovacao = null;
		if (StringUtils.isNotBlank(value)) {
			Long id = new Long(value);
			renovacao = renovacaoRepository.porId(id);
		}
		return renovacao;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Renovacao renovacao = (Renovacao) value;
			return renovacao.getId() == null ? null : renovacao.getId().toString();
		}
		return "";
	}

}
