package com.github.rmannibucau.blog.front.controller;

import com.github.rmannibucau.blog.dao.TagRepository;
import com.github.rmannibucau.blog.dao.UserRepository;
import com.github.rmannibucau.blog.domain.Post;
import com.github.rmannibucau.blog.front.dto.PostDto;
import com.github.rmannibucau.blog.front.service.PostService;
import com.github.rmannibucau.blog.processor.ContentProcessor;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.io.Serializable;

public abstract class PostEditContoller implements Serializable {
    @Inject
    protected UserRepository users;

    @Inject
    protected TagRepository categories;

    @Inject
    protected PostService postService;

    @Inject
    protected ContentProcessor processor;

    protected PostDto post;

    protected String newTag;

    public PostDto getPost() {
        return post;
    }

    public Class<? extends Navigation> save() {
        postService.save(post);

        // reset before returning on home page
        post = new PostDto();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Post created", null));
        return Navigation.Index.class;
    }

    public Post.Status[] getAvailableStatus() {
        return Post.Status.values();
    }

    public String[] getAvailableFormats() {
        return processor.getFormats();
    }

    public String getNewTag() {
        return newTag;
    }

    public void setNewTag(final String newTag) {
        this.newTag = newTag;
    }

    public void addTag(final String tag) {
        post.addTag(tag);
        newTag = null; // reset input entry
    }

    protected String getSuccessSummary() {
        return "Post updated";
    }
}
