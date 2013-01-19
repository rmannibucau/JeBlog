package com.github.rmannibucau.blog.init;

import com.github.rmannibucau.blog.dao.UserDao;
import com.github.rmannibucau.blog.domain.User;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

@Singleton
@Startup
public class FirstStartup {
    @Inject
    private UserDao users;

    @PostConstruct
    public void init() {
        if (users.count() == 0) {
            final User user = new User();
            user.setLogin("admin");
            user.setDisplayName("Admin");
            user.setPassword("adminpwd");
            users.saveAndFlush(user);
        }
    }
}
