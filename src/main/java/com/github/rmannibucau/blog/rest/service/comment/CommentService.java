package com.github.rmannibucau.blog.rest.service.comment;

import com.github.rmannibucau.blog.dao.CommentDao;
import com.github.rmannibucau.blog.dao.PostDao;
import com.github.rmannibucau.blog.domain.Comment;
import com.github.rmannibucau.blog.domain.Post;
import com.github.rmannibucau.blog.rest.service.comment.form.FormComment;
import com.github.rmannibucau.blog.rest.service.comment.form.FormUpdateComment;
import org.springframework.data.domain.PageRequest;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.util.List;

@Path("comment")
@Singleton
@Lock(LockType.READ)
public class CommentService {
    @Inject
    private CommentDao comments;

    @Inject
    private PostDao posts;

    @POST
    @Path("create")
    public Comment create(final @FormParam("") FormComment input) {
        final Comment comment = new Comment();
        comment.setStatus(Comment.Status.PENDING);
        updateComment(input, comment);
        comments.save(comment);
        return comment;
    }

    @POST
    @Path("update")
    public void update(final @FormParam("") FormUpdateComment input) {
        final Comment comment = comments.findOne(input.getId());
        if (comment == null) {
            throw new IllegalArgumentException("Comment " + input.getId() + " not found");
        }

        updateComment(input, comment);

        if (input.getStatus() != null) {
            final Comment.Status status = Comment.Status.valueOf(input.getStatus());
            comment.setStatus(status);
        }
    }

    @GET
    @Path("{id}")
    public Comment read(final @PathParam("id") long id) {
        return comments.findOne(id);
    }

    @DELETE
    @Path("{id}")
    public void delete(final @PathParam("id") long id) {
        comments.delete(id);
    }

    @GET
    @Path("list")
    public List<Comment> findAll(final @QueryParam("postId") long postId,
                              final @QueryParam("page") @DefaultValue("0") int page,
                              final @QueryParam("size") @DefaultValue("20") int size) {
        final Post post = posts.findOne(postId);
        if (post == null) {
            throw new IllegalArgumentException("Post " + post + " not found");
        }

        return comments.findByPost(post, new PageRequest(page, size)).getContent();
    }

    @GET
    @Path("pending")
    public List<Comment> findAll(final @QueryParam("page") @DefaultValue("0") int page,
                                 final @QueryParam("size") @DefaultValue("20") int size) {
        return comments.findByStatus(Comment.Status.PENDING, new PageRequest(page, size));
    }

    private void updateComment(final FormComment input, final Comment comment) {
        if (input.getAuthor() != null) {
            comment.setAuthor(input.getAuthor());
        }
        if (input.getEmail() != null) {
            comment.setEmail(input.getEmail());
        }
        if (input.getContent() != null) {
            comment.setContent(input.getContent());
        }
        if (input.getPostId() > 0) {
            comment.setPost(posts.findOne(input.getPostId()));
        }
    }
}
