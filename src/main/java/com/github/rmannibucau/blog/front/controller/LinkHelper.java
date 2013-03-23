package com.github.rmannibucau.blog.front.controller;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named("links")
@ApplicationScoped
public class LinkHelper {
    public Class<? extends Navigation> getLoginPage() {
        return Navigation.Login.class;
    }

    public Class<? extends Navigation> getIndexPage() {
        return Navigation.Index.class;
    }

    public Class<? extends Navigation.PostsNavigation> getPostPage() {
        return Navigation.Post.class;
    }

    public Class<? extends Navigation.PostsNavigation> getCreatePost() {
        return Navigation.CreatePost.class;
    }
}
