package com.github.rmannibucau.blog.domain.xml;

import com.github.rmannibucau.blog.domain.User;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class AuthorAdaptor extends XmlAdapter<String, User> {
    @Override
    public User unmarshal(final String name) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public String marshal(final User user) throws Exception {
        if (user == null) {
            return null;
        }
        return User.class.cast(user).getDisplayName();
    }
}
