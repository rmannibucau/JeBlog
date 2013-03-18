package com.github.rmannibucau.blog.init;

import com.github.rmannibucau.blog.dao.PostDao;
import com.github.rmannibucau.blog.dao.UserDao;
import com.github.rmannibucau.blog.domain.Post;
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

            tmp();
        }
    }

    @Inject
    private PostDao posts;
    private void tmp() {
        for (int i = 0; i < 100; i++) {
            final Post post = new Post();
            post.setAuthor(users.findAll().iterator().next());
            post.setContent("bla " + i);
            post.setStatus(Post.Status.PUBLISHED);
            post.setTitle("p_" + i);
            posts.save(post);
        }
    }
}
