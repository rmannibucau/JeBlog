package com.github.rmannibucau.blog.front.controller;

import com.github.rmannibucau.blog.dao.Repository;
import com.github.rmannibucau.blog.dao.TagDao;
import com.github.rmannibucau.blog.dao.UserDao;
import com.github.rmannibucau.blog.domain.Post;
import com.github.rmannibucau.blog.front.dto.PostDto;
import com.github.rmannibucau.blog.front.service.PostService;
import com.github.rmannibucau.blog.processor.ContentProcessor;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named("createPost")
@ViewScoped
public class CreatePostController implements Serializable {
    @Inject
    @Repository
    private UserDao users;

    @Inject
    @Repository
    private TagDao categories;

    @Inject
    private PostService postService;

    @Inject
    private ContentProcessor processor;

    private PostDto post;

    private String newTag;

    @PostConstruct
    public void initPost() {
        post = new PostDto();
        post.setFormat(processor.getDefaultFormat());
        post.setStatus(Post.Status.PUBLISHED);
    }

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
}
