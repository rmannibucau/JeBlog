package com.github.rmannibucau.blog.front.service;

import com.github.rmannibucau.blog.dao.PostRepository;
import com.github.rmannibucau.blog.dao.TagRepository;
import com.github.rmannibucau.blog.dao.UserRepository;
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
    private PostRepository posts;

    @Inject
    private UserRepository users;

    @Inject
    private TagRepository categories;

    @Inject
    private TagRepository tags;

    @Inject
    private UserController user;

    public void save(final PostDto post) {
        final Post toSave;
        if (post.getCreated() == null) {
            toSave = new Post();
        } else {
            toSave = posts.findById(post.getId());
            if (toSave == null) {
                throw new IllegalArgumentException("Post " + post.getId() + " not found");
            }
        }

        toSave.setTitle(post.getTitle());
        toSave.setContent(post.getContent());
        toSave.setStatus(post.getStatus());
        toSave.setAuthor(users.findByLogin(user.getLogin()));
        toSave.setFormat(post.getFormat());

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

    public void delete(final long id) {
        posts.deleteById(id);
    }
}
