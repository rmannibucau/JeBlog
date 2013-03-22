package com.github.rmannibucau.blog.front.service;

import com.github.rmannibucau.blog.dao.CategoryDao;
import com.github.rmannibucau.blog.dao.PostDao;
import com.github.rmannibucau.blog.dao.Repository;
import com.github.rmannibucau.blog.dao.UserDao;
import com.github.rmannibucau.blog.domain.Post;
import com.github.rmannibucau.blog.front.controller.UserController;
import com.github.rmannibucau.blog.front.dto.PostDto;
import com.github.rmannibucau.blog.init.DBSetup;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.inject.Inject;

@Singleton
@Lock(LockType.READ)
public class PostService {
    @Inject
    @Repository
    private PostDao posts;

    @Inject
    @Repository
    private UserDao users;

    @Inject
    @Repository
    private CategoryDao categories;

    @Inject
    private UserController user;

    public void save(final PostDto post) {
        final Post toSave = new Post();
        toSave.setTitle(post.getTitle());
        toSave.setContent(post.getContent());
        toSave.setStatus(post.getStatus());
        toSave.setAuthor(users.findByLogin(user.getLogin()));
        if (post.getCategory() != null) {
            toSave.setCategory(categories.findByName(post.getCategory()));
        } else {
            toSave.setCategory(categories.findByName(DBSetup.DEFAULT_CATEGORY));
        }

        posts.saveAndFlush(toSave);
    }
}
