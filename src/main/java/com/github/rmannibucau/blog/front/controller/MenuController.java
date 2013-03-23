package com.github.rmannibucau.blog.front.controller;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named("menu")
@ApplicationScoped
public class MenuController {
    public Class<? extends Navigation> getHomePage() {
        return Navigation.Index.class;
    }
}
