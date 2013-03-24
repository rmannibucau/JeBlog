package com.github.rmannibucau.blog.front.controller;

import com.github.rmannibucau.blog.dao.PostRepository;
import com.github.rmannibucau.blog.domain.Post;
import com.github.rmannibucau.blog.front.dto.PostDto;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named("editPost")
@ViewScoped
public class EditPostController extends PostEditContoller {
    @Inject
    private PostRepository posts;

    private long id;

    public void init() {
        if (post == null || post.getId() != id) {
            final Post record = posts.findById(id);
            post = new PostDto(record.getId(), record.getTitle(), record.getContent(),
                    record.getFormat(), record.getCreated(), record.getModified(),
                    record.getAuthor().getLogin(), record.getTagsAsString(), record.getStatus());
        } // else just a tag submit or ajax update
    }

    protected String getSuccessSummary() {
        return "Post updated";
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }
}
