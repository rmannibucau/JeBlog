package com.github.rmannibucau.blog.init;

import com.github.rmannibucau.blog.dao.TagDao;
import com.github.rmannibucau.blog.dao.UserDao;
import com.github.rmannibucau.blog.domain.User;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.SessionContext;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.concurrent.Future;

@Singleton
@Startup
public class DBSetup {
    public static final String DEFAULT_CATEGORY = "Default";

    @Inject
    private UserDao users;

    @Inject
    private TagDao categories;

    @Resource
    private SessionContext sc;

    private Future<Boolean> done;

    @PostConstruct
    public void init() {
        done = sc.getBusinessObject(DBSetup.class).doInit();
    }

    @Asynchronous
    public Future<Boolean> doInit() {
        if (users.count() == 0) {
            addDefaultUser();

        }
        return new AsyncResult<>(true);
    }

    @PreDestroy
    public void waitStartup() {
        try {
            done.get();
        } catch (final Exception e) {
            //no-op
        }
    }

    private void addDefaultUser() {
        final User user = new User();
        user.setLogin("jeblog");
        user.setDisplayName("Admin");
        user.setPassword("p@ssword");
        users.saveAndFlush(user);
    }
}
