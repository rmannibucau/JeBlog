package com.github.rmannibucau.blog.front;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named
@ApplicationScoped
public class MenuController {
    public Class<? extends Navigation> getHomePage() {
        return Navigation.Index.class;
    }
}
