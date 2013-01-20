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

import static com.github.rmannibucau.test.blog.rest.util.RESTTest.login;
import static com.github.rmannibucau.test.blog.rest.util.RESTTest.logout;
import static com.github.rmannibucau.test.blog.rest.util.RESTTest.newWebClient;
import static com.github.rmannibucau.test.blog.rest.util.RESTTest.unserialize;
import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class UserServiceTest {
    @Test
    public void loginOk() throws IOException {
        final Response response = newWebClient().path("user/login")
                .form(new Form().set("username", RESTTest.NAME).set("password", RESTTest.PWD));
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void loginKo() throws IOException {
        final Response response = newWebClient().path("user/login")
                .form(new Form().set("username", RESTTest.NAME).set("password", "wrongpwd"));
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }

    @Test
    public void checkLogout() throws IOException {
        final WebClient webClient = newWebClient();
        login(webClient).path("user/logout").head();

        // quick way to check we are no more logged: a user can create a post only when logged
        final Response response = webClient.reset().path("post/create")
                .form(new Form().set("title", "fail").set("content", "fail").set("category", "fail"));
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }

    @Test
    public void create() throws IOException {
        final WebClient client = newWebClient();
        final Response response = login(client).path("user/create")
                .form(new Form().set("username", "superuser").set("displayName", "display").set("password", "superpwd"));
        logout(client);

        final User user = unserialize(User.class, response);
        assertEquals("superuser", user.getLogin());
        assertEquals("********", user.getPassword());
        assertEquals("display", user.getDisplayName());
    }
}
