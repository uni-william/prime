package br.com.sis.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import br.com.sis.entity.Despesa;
import br.com.sis.repository.DespesaRepository;

@FacesConverter(forClass = Despesa.class)
public class DespesaConverter implements Converter, Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private DespesaRepository despesaRepository;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Despesa despesa = null;
		if (StringUtils.isNotBlank(value)) {
			Long id = new Long(value);
			despesa = despesaRepository.porId(id);
		}
		return despesa;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Despesa despesa = (Despesa) value;
			return despesa.getId() == null ? null : despesa.getId().toString();
		}
		return "";
	}

}
