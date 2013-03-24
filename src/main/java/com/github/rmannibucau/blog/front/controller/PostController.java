package com.github.rmannibucau.blog.front.controller;

import com.github.rmannibucau.blog.dao.PostRepository;
import com.github.rmannibucau.blog.domain.Post;
import com.github.rmannibucau.blog.front.dto.PostDto;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named("post")
@RequestScoped
public class PostController {
    @Inject
    private PostRepository posts;

    private PostDto post;

    private long id;

    public void init() {
        if (id >= 0) {
            final Post p = posts.findById(id);
            if (p != null) {
                post = new PostDto(p.getId(), p.getTitle(), p.getHtml(), p.getFormat(),
                                p.getCreated(), p.getModified(),
                                p.getAuthor().getDisplayName(),
                                p.getTagsAsString(), p.getStatus());
            }
        }

        if (post == null) { // not found
            post = new PostDto(-1, "Not found", "No post id specified", null, null, null, null, null, null);
        }
    }

    public PostDto getCurrent() {
        return post;
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }
}
