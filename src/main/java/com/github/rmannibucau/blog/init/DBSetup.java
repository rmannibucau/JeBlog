package com.github.rmannibucau.blog.init;

import com.github.rmannibucau.blog.dao.CategoryDao;
import com.github.rmannibucau.blog.dao.PostDao;
import com.github.rmannibucau.blog.dao.UserDao;
import com.github.rmannibucau.blog.domain.Category;
import com.github.rmannibucau.blog.domain.Post;
import com.github.rmannibucau.blog.domain.User;
import org.apache.commons.lang3.RandomStringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.SessionContext;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.Random;
import java.util.concurrent.Future;

@Singleton
@Startup
public class DBSetup {
    public static final String DEFAULT_CATEGORY = "Default";

    @Inject
    private UserDao users;

    @Inject
    private CategoryDao categories;

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
            final User user = addDefaultUser();
            addSomeData(user);

        }
        if (categories.count() == 0) {
            addDefaultCategory();
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

    private void addDefaultCategory() {
        final Category category = new Category();
        category.setName(DEFAULT_CATEGORY);
        category.setDescription("The default category");
        categories.saveAndFlush(category);
    }

    private User addDefaultUser() {
        final User user = new User();
        user.setLogin("jeblog");
        user.setDisplayName("Admin");
        user.setPassword("p@ssword");
        users.saveAndFlush(user);
        return user;
    }

    @Inject
    private PostDao posts;
    private void addSomeData(final User u) {
        // to clean up once done
        final Random random = new Random();
        for (int i = 0; i < 100; i++) {
            final Post post = new Post();
            post.setAuthor(u);
            for (int j = 0; j < 100; j++) {
                post.setContent(post.getContent() + " " + RandomStringUtils.randomAlphanumeric(random.nextInt(50)));
            }
            post.setStatus(Post.Status.PUBLISHED);
            post.setTitle("Title #" + i);
            posts.save(post);
        }
    }
}
