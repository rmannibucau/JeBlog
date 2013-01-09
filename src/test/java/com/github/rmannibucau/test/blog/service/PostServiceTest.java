package com.github.rmannibucau.test.blog.service;

import com.github.rmannibucau.blog.domain.Post;
import com.github.rmannibucau.test.blog.ShrinkWraps;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.ext.form.Form;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class PostServiceTest extends RESTTest {
    @Deployment(testable = false)
    public static WebArchive war() {
        return ShrinkWraps.javaEEBlog();
    }

    @Test
    public void create() throws IOException {
        final WebClient webClient = sessionAwareWebClient();

        webClient.path("user/login").form(new Form().set("username", NAME).set("password", PWD));
        final Response response;
        try {
            response = webClient.reset().path("post/create")
                .form(new Form().set("title", "supertitle").set("content", "supercontent").set("category", "supercategory"));
        } finally {
            webClient.reset().path("user/logout").head();
        }

        final Post post = provider(Post.class).readFrom(Post.class, Post.class,
                ANNOTATIONS, JSON, HEADERS, new ByteArrayInputStream(json(response).getBytes()));

        assertEquals("supertitle", post.getTitle());
        assertEquals("supercontent", post.getContent());
    }
}
