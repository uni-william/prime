package br.com.sis.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import br.com.sis.entity.ItemCheckList;
import br.com.sis.repository.ItemCheckListRepository;

@FacesConverter(forClass = ItemCheckList.class)
public class ItemCheckListConverter implements Converter, Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private ItemCheckListRepository itemCheckListRepository;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		ItemCheckList itemCheckList = null;
		if (StringUtils.isNotBlank(value)) {
			Long id = new Long(value);
			itemCheckList = itemCheckListRepository.porId(id);
		}
		return itemCheckList;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			ItemCheckList itemCheckList = (ItemCheckList) value;
			return itemCheckList.getId() == null ? null : itemCheckList.getId().toString();
		}
		return "";
	}

}
