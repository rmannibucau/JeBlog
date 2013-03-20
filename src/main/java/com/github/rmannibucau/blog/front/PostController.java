package com.github.rmannibucau.blog.front;

import com.github.rmannibucau.blog.dao.PostDao;
import com.github.rmannibucau.blog.dao.Repository;
import com.github.rmannibucau.blog.domain.Post;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;

@Named("post")
@RequestScoped
public class PostController {
    @Inject
    @Repository
    private PostDao posts;

    private Post post;

    @PostConstruct
    public void findPost() {
        final Map<String, String> map = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if (map.containsKey("id")) {
            post = posts.findOne(Long.parseLong(map.get("id")));
        } else {
            post = new Post();
            post.setContent("Post not found");
        }
    }

    public Post getCurrent() {
        return post;
    }
}
