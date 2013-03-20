package com.github.rmannibucau.blog.init;

import com.github.rmannibucau.blog.dao.PostDao;
import com.github.rmannibucau.blog.dao.UserDao;
import com.github.rmannibucau.blog.domain.Post;
import com.github.rmannibucau.blog.domain.User;
import org.apache.commons.lang3.RandomStringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.SessionContext;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.Random;

@Singleton
@Startup
public class FirstStartup {
    @Inject
    private UserDao users;

    @Resource
    private SessionContext sc;

    @PostConstruct
    public void init() {
        sc.getBusinessObject(FirstStartup.class).doInit();
    }

    @Asynchronous
    public void doInit() {
        if (users.count() == 0) {
            addDefaultUser();
            addSomeData();


        }
    }

    private void addDefaultUser() {
        final User user = new User();
        user.setLogin("admin");
        user.setDisplayName("Admin");
        user.setPassword("adminpwd");
        users.saveAndFlush(user);
    }

    @Inject
    private PostDao posts;
    private void addSomeData() {
        // to clean up once done
        final Random random = new Random();
        for (int i = 0; i < 100; i++) {
            final Post post = new Post();
            post.setAuthor(users.findAll().iterator().next());
            for (int j = 0; j < 100; j++) {
                post.setContent(post.getContent() + " " + RandomStringUtils.randomAlphanumeric(random.nextInt(50)));
            }
            post.setStatus(Post.Status.PUBLISHED);
            post.setTitle("Title #" + i);
            posts.save(post);
        }
    }
}
