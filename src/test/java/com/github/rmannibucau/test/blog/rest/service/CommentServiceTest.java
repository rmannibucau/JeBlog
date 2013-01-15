package com.github.rmannibucau.test.blog.rest.service;

import com.github.rmannibucau.blog.domain.Comment;
import com.github.rmannibucau.test.blog.rest.util.RESTTest;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.ext.form.Form;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.core.Response;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class CommentServiceTest extends RESTTest {
    @Test
    public void create() throws IOException {
        final WebClient client = newWebClient();
        final long postId = createPost(client, "create comment");

        final Response response = client.reset().path("comment/create")
            .form(new Form()
                    .set("author", "anonymous")
                    .set("email", "foo@bar.com")
                    .set("content", "a comment")
                    .set("postId", postId));

        final Comment comment = unserialize(Comment.class, response);
        assertEquals("anonymous", comment.getAuthor());
        assertEquals("a comment", comment.getContent());
        assertEquals(Comment.Status.PENDING, comment.getStatus());
    }
}
