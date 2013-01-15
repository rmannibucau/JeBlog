package com.github.rmannibucau.blog.domain.xml;

import com.github.rmannibucau.blog.domain.Category;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class CategoryAdaptor extends XmlAdapter<String, Category> {
    @Override
    public Category unmarshal(final String name) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public String marshal(final Category category) throws Exception {
        if (category == null) {
            return null;
        }
        return Category.class.cast(category).getName();
    }
}
