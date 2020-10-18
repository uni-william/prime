package br.com.sis.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;

import br.com.sis.entity.TipoDespesa;
import br.com.sis.repository.TipoDespesaRepository;

@FacesConverter(forClass = TipoDespesa.class)
public class TipoDespesaConverter implements Converter, Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private TipoDespesaRepository tipoDespesaRepository;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		TipoDespesa tipoDespesa = null;
		if (StringUtils.isNotBlank(value)) {
			Long id = new Long(value);
			tipoDespesa = tipoDespesaRepository.porId(id);
		}
		return tipoDespesa;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			TipoDespesa tipoDespesa = (TipoDespesa) value;
			return tipoDespesa.getId() == null ? null : tipoDespesa.getId().toString();
		}
		return "";
	}

}
