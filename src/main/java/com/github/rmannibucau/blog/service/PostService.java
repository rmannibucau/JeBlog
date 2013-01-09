package com.github.rmannibucau.blog.service;

import com.github.rmannibucau.blog.dao.CategoryDao;
import com.github.rmannibucau.blog.dao.PostDao;
import com.github.rmannibucau.blog.dao.UserDao;
import com.github.rmannibucau.blog.domain.Category;
import com.github.rmannibucau.blog.domain.Post;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.List;

@Singleton
@Lock(LockType.READ)
public class PostService {
    @Inject
    private PostDao posts;

    @Inject
    private UserDao users;

    @Inject
    private CategoryDao categories;

    @Inject
    private UserService userService;

    @POST
    @Path("post/create")
    public Response create(final @FormParam("") FormPost input, final @Context HttpServletRequest request) {
        final String currentUser;
        try {
            currentUser = currentUser(request);
        } catch (IllegalStateException ise) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        final Post post = new Post();
        updatePost(post, input);
        post.setAuthor(users.findByLogin(currentUser));
        return Response.ok(posts.saveAndFlush(post)).build(); // validation on flush
    }

    @GET
    @Path("post/{id}")
    public Post read(final @PathParam("id") long id) {
        return posts.findOne(id);
    }

    @POST
    @Path("post/{id}")
    public void update(final @PathParam("id") long id, final @FormParam("") FormPost input) {
        final Post post = posts.findOne(id);
        if (post == null) {
            throw new IllegalStateException("Post " + id + " not found");
        }

        updatePost(post, input);
    }

    @DELETE
    @Path("post/{id}")
    public void delete(final @PathParam("id") long id) {
        posts.delete(id);
    }

    @GET
    @Path("posts")
    public List<Post> findAll(final @QueryParam("page") @DefaultValue("1") int page,
                              final @QueryParam("size") @DefaultValue("20") int size) {
        final Pageable pageable = new PageRequest(page, size);
        return posts.findAll(pageable).getContent();
    }

    private String currentUser(final HttpServletRequest request) {
        final String currentUser = userService.currentUser(request.getSession());
        if (currentUser == null) {
            throw new IllegalStateException("Only logged user can post");
        }
        return currentUser;
    }

    private void updatePost(final Post post, final FormPost input) {
        if (input.getTitle() != null) {
            post.setTitle(input.getTitle());
        }
        if (input.getContent() != null) {
            post.setContent(input.getContent());
        }
        if (post.getStatus() == null) {
            post.setStatus(Post.Status.DRAFT);
        }
        if (input.getCategory() != null) {
            final Category category = categories.findByName(input.getCategory());
            if (category != null) {
                post.setCategory(category);
            }
        }
    }
}
