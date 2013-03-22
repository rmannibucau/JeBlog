package com.github.rmannibucau.blog.front.controller;

import com.github.rmannibucau.blog.dao.CategoryDao;
import com.github.rmannibucau.blog.dao.Repository;
import com.github.rmannibucau.blog.dao.UserDao;
import com.github.rmannibucau.blog.domain.Category;
import com.github.rmannibucau.blog.domain.Post;
import com.github.rmannibucau.blog.domain.User;
import com.github.rmannibucau.blog.front.Navigation;
import com.github.rmannibucau.blog.front.dto.PostDto;
import com.github.rmannibucau.blog.front.service.PostService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.springframework.data.domain.Sort;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Collection;

@Named("createPost")
@ViewScoped
public class CreatePostController implements Serializable {
    @Inject
    @Repository
    private UserDao users;

    @Inject
    @Repository
    private CategoryDao categories;

    @Inject
    private PostService postService;

    private PostDto post = new PostDto();

    public PostDto getPost() {
        return post;
    }

    public Class<? extends Navigation> save() {
        postService.save(post);

        // reset before returning on home page
        post = new PostDto();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Post created", null));
        return Navigation.Index.class;
    }

    public Post.Status[] getAvailableStatus() {
        return Post.Status.values();
    }

    public Collection<User> getAvailableUsers() {
        return users.findAll(new Sort(Sort.Direction.ASC, "displayName"));
    }

    public Collection<Category> getAvailableCategories() {
        return categories.findAll(new Sort(Sort.Direction.ASC, "name"));
    }
}
