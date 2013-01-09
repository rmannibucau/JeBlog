package com.github.rmannibucau.blog.domain.xml;

import com.github.rmannibucau.blog.dao.UserDao;
import com.github.rmannibucau.blog.domain.User;
import org.apache.deltaspike.core.api.provider.BeanProvider;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class AuthorAdaptor extends XmlAdapter<String, User> {
    @Override
    public User unmarshal(final String name) throws Exception {
        if (name == null) {
            return null;
        }
        return BeanProvider.getContextualReference(UserDao.class).findByLogin(String.class.cast(name));
    }

    @Override
    public String marshal(final User user) throws Exception {
        if (user == null) {
            return null;
        }
        return User.class.cast(user).getDisplayName();
    }
}
