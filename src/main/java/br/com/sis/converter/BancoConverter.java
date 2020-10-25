package br.com.sis.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import br.com.sis.entity.Banco;
import br.com.sis.repository.BancoRepository;

@FacesConverter(forClass = Banco.class)
public class BancoConverter implements Converter, Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private BancoRepository bancoRepository;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Banco banco = null;
		if (StringUtils.isNotBlank(value)) {
			Long id = new Long(value);
			banco = bancoRepository.porId(id);
		}
		return banco;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Banco banco = (Banco) value;
			return banco.getId() == null ? null : banco.getId().toString();
		}
		return "";
	}

}
