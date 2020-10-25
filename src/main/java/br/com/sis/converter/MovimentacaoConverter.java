package br.com.sis.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import br.com.sis.entity.Movimentacao;
import br.com.sis.repository.MovimentacaoRepository;

@FacesConverter(forClass = Movimentacao.class)
public class MovimentacaoConverter implements Converter, Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private MovimentacaoRepository MovimentacaoRepository;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Movimentacao Movimentacao = null;
		if (StringUtils.isNotBlank(value)) {
			Long id = new Long(value);
			Movimentacao = MovimentacaoRepository.porId(id);
		}
		return Movimentacao;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Movimentacao Movimentacao = (Movimentacao) value;
			return Movimentacao.getId() == null ? null : Movimentacao.getId().toString();
		}
		return "";
	}

}
