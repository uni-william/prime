package br.com.sis.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import br.com.sis.entity.FormaPagamento;
import br.com.sis.repository.FormaPagamentoRepository;

@FacesConverter(forClass = FormaPagamento.class)
public class FormaPagamentoConverter implements Converter, Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private FormaPagamentoRepository formaPagamentoRepository;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		FormaPagamento formaPagamento = null;
		if (StringUtils.isNotBlank(value)) {
			Long id = new Long(value);
			formaPagamento = formaPagamentoRepository.porId(id);
		}
		return formaPagamento;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			FormaPagamento formaPagamento = (FormaPagamento) value;
			return formaPagamento.getId() == null ? null : formaPagamento.getId().toString();
		}
		return "";
	}

}
