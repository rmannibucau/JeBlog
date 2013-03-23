package com.github.rmannibucau.blog.front.service;

import com.github.rmannibucau.blog.dao.TagDao;
import com.github.rmannibucau.blog.dao.PostDao;
import com.github.rmannibucau.blog.dao.Repository;
import com.github.rmannibucau.blog.dao.UserDao;
import com.github.rmannibucau.blog.domain.Post;
import com.github.rmannibucau.blog.domain.Tag;
import com.github.rmannibucau.blog.front.controller.UserController;
import com.github.rmannibucau.blog.front.dto.PostDto;

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
    private TagDao categories;

    @Inject
    @Repository
    private TagDao tags;

    @Inject
    private UserController user;

    public void save(final PostDto post) {
        final Post toSave = new Post();
        toSave.setTitle(post.getTitle());
        toSave.setContent(post.getContent());
        toSave.setStatus(post.getStatus());
        toSave.setAuthor(users.findByLogin(user.getLogin()));

        for (final String name : post.getTags()) {
            Tag tag = tags.findByName(name);
            if (tag == null) {
                tag = new Tag();
                tag.setName(name);
            }
            tags.save(tag);
            toSave.getTags().add(tag);
        }

        posts.saveAndFlush(toSave);
    }
}
