package com.github.rmannibucau.blog.front.converter;

import com.github.rmannibucau.blog.dao.CategoryDao;
import com.github.rmannibucau.blog.dao.Repository;
import com.github.rmannibucau.blog.domain.Category;
import com.github.rmannibucau.blog.domain.Post;
import org.apache.commons.lang3.StringUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.EnumConverter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Locale;

@Named
@FacesConverter(forClass = Category.class)
public class CategoryConverter implements Converter {
    @Inject
    @Repository
    private CategoryDao categories;

    @Override
    public String getAsString(final FacesContext facesContext, final UIComponent uiComponent, final Object value)
            throws ConverterException {
        if (value == null) {
            return null;
        }

        return StringUtils.capitalize(Category.class.cast(value).getName());
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
