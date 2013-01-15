package com.github.rmannibucau.test.blog.rest.service;

import com.github.rmannibucau.blog.domain.User;
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
public class UserServiceTest extends RESTTest {
    @Test
    public void loginOk() throws IOException {
        final Response response = newWebClient().path("user/login")
                .form(new Form().set("username", NAME).set("password", PWD));
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void loginKo() throws IOException {
        final Response response = newWebClient().path("user/login")
                .form(new Form().set("username", NAME).set("password", "wrongpwd"));
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }

    @Test
    public void logout() throws IOException {
        final WebClient webClient = newWebClient();
        login(webClient).path("user/logout").head();

        // quick way to check we are no more logged: a user can create a post only when logged
        final Response response = webClient.reset().path("post/create")
                .form(new Form().set("title", "fail").set("content", "fail").set("category", "fail"));
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }

    @Test
    public void create() throws IOException {
        final Response response = newWebClient().path("user/create")
                .form(new Form().set("username", "superuser").set("displayName", "display").set("password", "superpwd"));
        final User user = unserialize(User.class, response);
        assertEquals("superuser", user.getLogin());
        assertEquals("superpwd", user.getPassword());
        assertEquals("display", user.getDisplayName());
    }
}
