package com.github.rmannibucau.blog.front.converter;

import com.github.rmannibucau.blog.domain.Post;
import org.apache.commons.lang3.StringUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import java.util.Locale;

@FacesConverter(forClass = Post.Status.class)
public class PostStatusConverter implements Converter {
    @Override
    public String getAsString(final FacesContext facesContext, final UIComponent uiComponent, final Object value)
            throws ConverterException {
        if (value == null) {
            return null;
        }

        return StringUtils.capitalize(Post.Status.class.cast(value).name().toLowerCase(Locale.ENGLISH));
    }

    @Override
    public Object getAsObject(final FacesContext facesContext, final UIComponent uiComponent, final String value)
            throws ConverterException {
        if (value == null) {
            return null;
        }

        return Post.Status.valueOf(value.toUpperCase(Locale.ENGLISH));
    }
}
