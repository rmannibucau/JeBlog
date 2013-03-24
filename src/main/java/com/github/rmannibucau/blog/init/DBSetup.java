package com.github.rmannibucau.blog.init;

import com.github.rmannibucau.blog.dao.PostRepository;
import com.github.rmannibucau.blog.dao.TagRepository;
import com.github.rmannibucau.blog.dao.UserRepository;
import com.github.rmannibucau.blog.domain.Post;
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
    private UserRepository users;

    @Inject
    private TagRepository categories;

    @Inject
    private PostRepository posts;

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

            for (int i = 0; i < 30; i++) {
                Post p = new Post();
                p.setTitle("#" + i);
                p.setContent("#" + i);
                p.setAuthor(users.findAll().iterator().next());
                p.setStatus(Post.Status.PUBLISHED);
                posts.save(p);
            }
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
