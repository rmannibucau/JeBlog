package com.github.rmannibucau.blog.domain.xml;

import com.github.rmannibucau.blog.dao.CategoryDao;
import com.github.rmannibucau.blog.domain.Category;
import org.apache.deltaspike.core.api.provider.BeanProvider;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class CategoryAdaptor extends XmlAdapter<String, Category> {
    @Override
    public Category unmarshal(String name) throws Exception {
        if (name == null) {
            return null;
        }
        return BeanProvider.getContextualReference(CategoryDao.class).findByName(String.class.cast(name));
    }

    @Override
    public String marshal(final Category category) throws Exception {
        if (category == null) {
            return null;
        }
        return Category.class.cast(category).getName();
    }
}
