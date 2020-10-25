package br.com.sis.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import br.com.sis.entity.PagamentoSemanal;
import br.com.sis.repository.PagamentoSemanalRepository;

@FacesConverter(forClass = PagamentoSemanal.class)
public class PagamentoSemanalConverter implements Converter, Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private PagamentoSemanalRepository pagamentoSemanalRepository;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		PagamentoSemanal pagamentoSemanal = null;
		if (StringUtils.isNotBlank(value)) {
			Long id = new Long(value);
			pagamentoSemanal = pagamentoSemanalRepository.porId(id);
		}
		return pagamentoSemanal;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			PagamentoSemanal pagamentoSemanal = (PagamentoSemanal) value;
			return pagamentoSemanal.getId() == null ? null : pagamentoSemanal.getId().toString();
		}
		return "";
	}

}
