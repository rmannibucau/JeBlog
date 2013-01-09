package com.github.rmannibucau.test.blog.service;

import com.github.rmannibucau.blog.domain.User;
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
import java.net.URI;

import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class UserServiceTest extends RESTTest {
    @Deployment(testable = false)
    public static WebArchive war() {
        return ShrinkWraps.javaEEBlog();
    }

    @Test
    public void loginOk() throws IOException {
        final Response response = WebClient.create(URI.create("http://localhost:4204/je-blog/user/login"))
                .form(new Form().set("username", NAME).set("password", PWD));
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void loginKo() throws IOException {
        final Response response = WebClient.create(URI.create("http://localhost:4204/je-blog/user/login"))
                .form(new Form().set("username", NAME).set("password", "wrongpwd"));
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }

    @Test
    public void logout() {
        final WebClient webClient = sessionAwareWebClient();
        login(webClient);
        webClient.reset().path("user/logout").head();

    }

    @Test
    public void create() throws IOException {
        final Response response = WebClient.create(URI.create("http://localhost:4204/je-blog/user/create"))
                .form(new Form().set("username", "superuser").set("displayName", "display").set("password", "superpwd"));
        final User user = provider(User.class).readFrom(User.class, User.class,
                ANNOTATIONS, JSON, HEADERS, new ByteArrayInputStream(json(response).getBytes()));
        assertEquals("superuser", user.getLogin());
        assertEquals("superpwd", user.getPassword());
        assertEquals("display", user.getDisplayName());
    }

    private static void login(final WebClient webClient) {
        webClient.path("user/login").form(new Form().set("username", NAME).set("password", PWD));
    }
}
