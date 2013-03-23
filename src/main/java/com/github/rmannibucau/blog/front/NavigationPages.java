package com.github.rmannibucau.blog.front;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named("pages")
@ApplicationScoped
public class NavigationPages {
    public Class<? extends Navigation.PostsNavigation> getPostPage() {
        return Navigation.Post.class;
    }
}
