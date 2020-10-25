package br.com.sis.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import br.com.sis.entity.Vendedor;
import br.com.sis.repository.VendedorRepository;

@FacesConverter(forClass = Vendedor.class)
public class VendedorConverter implements Converter, Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private VendedorRepository vendedorRepository;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Vendedor vendedor = null;
		if (StringUtils.isNotBlank(value)) {
			Long id = new Long(value);
			vendedor = vendedorRepository.porId(id);
		}
		return vendedor;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Vendedor vendedor = (Vendedor) value;
			return vendedor.getId() == null ? null : vendedor.getId().toString();
		}
		return "";
	}

}
