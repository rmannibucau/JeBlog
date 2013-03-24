package com.github.rmannibucau.blog.front.converter;

import com.github.rmannibucau.blog.dao.TagRepository;
import com.github.rmannibucau.blog.domain.Tag;
import org.apache.commons.lang3.StringUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@FacesConverter(forClass = Tag.class)
public class CategoryConverter implements Converter {
    @Inject
    private TagRepository categories;

    @Override
    public String getAsString(final FacesContext facesContext, final UIComponent uiComponent, final Object value)
            throws ConverterException {
        if (value == null) {
            return null;
        }

        return StringUtils.capitalize(Tag.class.cast(value).getName());
    }

    @Override
    public Object getAsObject(final FacesContext facesContext, final UIComponent uiComponent, final String value)
            throws ConverterException {
        if (value == null) {
            return null;
        }

        return categories.findByName(value);
    }
}
