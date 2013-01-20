package com.github.rmannibucau.test.blog.rest.service;

import com.github.rmannibucau.blog.domain.Post;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.ext.form.Form;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static com.github.rmannibucau.test.blog.rest.util.RESTTest.createPost;
import static com.github.rmannibucau.test.blog.rest.util.RESTTest.login;
import static com.github.rmannibucau.test.blog.rest.util.RESTTest.logout;
import static com.github.rmannibucau.test.blog.rest.util.RESTTest.newWebClient;
import static com.github.rmannibucau.test.blog.rest.util.RESTTest.unserialize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(Arquillian.class)
public class PostServiceTest {
    private WebClient webClient;

    @Before
    public void doLogin() {
        webClient = newWebClient();
        login(webClient);
    }

    @After
    public void doLogout() {
        logout(webClient);
    }

    @Test
    public void create() throws IOException {
        final Response response = webClient.path("post/create")
            .form(new Form().set("title", "supertitle").set("content", "supercontent").set("category", "supercategory"));

        final Post post = unserialize(Post.class, response);
        assertEquals("supertitle", post.getTitle());
        assertEquals("supercontent", post.getContent());
    }

    @Test
    public void read() throws IOException {
        long id = createPost(webClient, "read");

        final Post get = webClient.reset().path("post/{id}", id).get(Post.class);
        assertNotNull(get);
        assertEquals("read title", get.getTitle());
        assertEquals("read content", get.getContent());

    }

    @Test
    public void update() throws IOException {
        long id = createPost(webClient, "update");

        final Response response = webClient.reset().path("post/update")
            .form(new Form().set("id", id).set("title", "updated").set("content", "changed"));
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());

        final Post updated = webClient.reset().path("post/{id}", id).get(Post.class);
        assertEquals("updated", updated.getTitle());
        assertEquals("changed", updated.getContent());
    }

    @Test
    public void delete() throws IOException {
        long id = createPost(webClient, "delete");

        final Response response = webClient.reset().path("post/{id}", id).delete();
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }

    @Test
    public void findAll() throws IOException {
        final Collection<Long> ids = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ids.add(createPost(webClient, "delete #" + i));
        }

        final Collection<? extends Post> posts = webClient.reset().path("post/list")
                                                    .query("status", Post.Status.DRAFT)
                                                    .accept(MediaType.APPLICATION_JSON_TYPE)
                                                    .getCollection(Post.class);
        assertTrue(posts.size() >= ids.size());

        for (Long id : ids) {
            boolean found = false;
            for (Post post : posts) {
                final Long postId = post.getId();
                if (postId != null && postId.equals(id)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                fail("post #" + id + " not found");
            }
        }
    }
}
