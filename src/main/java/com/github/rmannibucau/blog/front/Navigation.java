package com.github.rmannibucau.blog.front;

import org.apache.deltaspike.core.api.config.view.ViewConfig;
import org.apache.deltaspike.jsf.api.config.view.View;

@View(basePath = "/", extension = "xhtml", navigation = View.NavigationMode.REDIRECT)
public interface Navigation extends ViewConfig {
    @View
    public static class Index implements Navigation {}

    @View
    public static class Login implements Navigation {}

    @View
    public static class Post implements Navigation {}

    @View
    public static interface SecuredNavigation extends Navigation {}
}
