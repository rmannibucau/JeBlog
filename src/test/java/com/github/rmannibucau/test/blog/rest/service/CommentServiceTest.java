package com.github.rmannibucau.test.blog.rest.service;

import com.github.rmannibucau.blog.domain.Comment;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.ext.form.Form;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.core.Response;
import java.io.IOException;

import static com.github.rmannibucau.test.blog.rest.util.RESTTest.createPost;
import static com.github.rmannibucau.test.blog.rest.util.RESTTest.login;
import static com.github.rmannibucau.test.blog.rest.util.RESTTest.logout;
import static com.github.rmannibucau.test.blog.rest.util.RESTTest.newWebClient;
import static com.github.rmannibucau.test.blog.rest.util.RESTTest.unserialize;
import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class CommentServiceTest {
    @Test
    public void create() throws IOException {
        final WebClient client = newWebClient();

        login(client);
        final long postId = createPost(client, "create comment");
        logout(client);

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
