package com.github.rmannibucau.blog.front.controller;

import com.github.rmannibucau.blog.front.security.LoggedInUserVoter;
import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.jsf.api.config.view.View;
import org.apache.deltaspike.security.api.authorization.annotation.Secured;

@View(basePath = "/", extension = "xhtml", navigation = View.NavigationMode.REDIRECT)
public interface Navigation extends ViewConfig {
    @View
    class Index implements Navigation {}

    @View
    class Login implements Navigation {}

    @View(basePath = "/post/")
    interface PostsNavigation extends Navigation {}

    @View
    class Post implements PostsNavigation {}

    @Secured(LoggedInUserVoter.class)
    interface SecuredPostsNavigation extends PostsNavigation {}

    @View(name = "create-post")
    class CreatePost implements SecuredPostsNavigation {}

    @View(name = "edit-post")
    class EditPost implements SecuredPostsNavigation {}
}
