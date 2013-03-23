package com.github.rmannibucau.blog.front.controller;

import com.github.rmannibucau.blog.dao.PostDao;
import com.github.rmannibucau.blog.dao.Repository;
import com.github.rmannibucau.blog.domain.Post;
import com.github.rmannibucau.blog.front.dto.PostDto;

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

    private PostDto post;

    @PostConstruct
    public void findPost() {
        final Map<String, String> map = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if (map.containsKey("id")) {
            final Post p = posts.findOne(Long.parseLong(map.get("id")));
            if (p != null) {
                post = new PostDto(p.getId(), p.getTitle(), p.getHtml(), p.getFormat(),
                                p.getCreated(), p.getModified(),
                                p.getAuthor().getDisplayName(),
                                p.getTagsAsString(), p.getStatus());
            }
        }

        if (post == null) { // not found
            post = new PostDto(-1, "Not found", "No post id specified", null, null, null, null, null, null);
        }
    }

    public PostDto getCurrent() {
        return post;
    }
}
