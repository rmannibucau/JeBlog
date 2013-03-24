package com.github.rmannibucau.blog.front.controller;

import com.github.rmannibucau.blog.domain.Post;
import com.github.rmannibucau.blog.front.dto.PostDto;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named("createPost")
@ViewScoped
public class CreatePostController extends PostEditContoller {
    @PostConstruct
    public void initPost() {
        post = new PostDto();
        post.setFormat(processor.getDefaultFormat());
        post.setStatus(Post.Status.PUBLISHED);
    }

    protected String getSuccessSummary() {
        return "Post saved";
    }
}
