package br.com.sis.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import br.com.sis.entity.Configuracoes;
import br.com.sis.repository.ConfiguracoesRepository;

@FacesConverter(forClass = Configuracoes.class)
public class ConfiguracaoEmailConverter implements Converter {

	@Inject
	private ConfiguracoesRepository configuracoesEmail;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Configuracoes configuracaoEmail = null;
		if (StringUtils.isNotBlank(value)) {
			Long id = new Long(value);
			configuracaoEmail = configuracoesEmail.porId(id);
		}
		return configuracaoEmail;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Configuracoes configuracaoEmail = (Configuracoes) value;
			return configuracaoEmail.getId() == null ? null : configuracaoEmail.getId().toString();
		}
		return "";
	}

}
