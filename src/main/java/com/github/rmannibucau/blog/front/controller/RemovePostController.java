package com.github.rmannibucau.blog.front.controller;

import com.github.rmannibucau.blog.front.service.PostService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named("removePost")
@RequestScoped
public class RemovePostController implements Serializable {
    @Inject
    private PostService posts;

    public Class<? extends Navigation> remove(final long id) {
        posts.delete(id);
        return Navigation.Index.class;
    }
}
