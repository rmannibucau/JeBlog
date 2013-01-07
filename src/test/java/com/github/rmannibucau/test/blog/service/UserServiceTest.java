package com.github.rmannibucau.test.blog.service;

import org.apache.cxf.jaxrs.client.JAXRSClientFactoryBean;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.ext.form.Form;
import org.apache.cxf.message.Message;
import org.apache.ziplock.IO;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
public class UserServiceTest {
    @Deployment(testable = false)
    public static WebArchive war() {
        return ShrinkWrap.create(WebArchive.class, "je-blog.war")
                .addPackages(true, "com.github.rmannibucau.blog")
                .addAsWebInfResource(new FileAsset(new File("src/main/webapp/WEB-INF/beans.xml")), "beans.xml")
                .addAsWebInfResource(new FileAsset(new File("src/main/webapp/WEB-INF/persistence.xml")), "persistence.xml");
    }

    @BeforeClass
    public static void createAdmin() throws IOException {
        final Response response = WebClient.create(URI.create("http://localhost:4204/je-blog/user/create"))
                .form(new Form()
                        .set("username", "admin")
                        .set("displayName", "Admin")
                        .set("password", "adminpwd"));

        final Object entity = response.getEntity();
        assertThat(entity, instanceOf(InputStream.class));

        final String user = IO.slurp(InputStream.class.cast(entity));
        assertEquals("{\"login\":\"admin\",\"password\":\"adminpwd\",\"displayName\":\"Admin\"}", user);
    }

    @Test
    public void loginOk() throws IOException {
        final Response response = WebClient.create(URI.create("http://localhost:4204/je-blog/user/login"))
                .form(new Form()
                        .set("username", "admin")
                        .set("password", "adminpwd"));
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }

    @Test
    public void loginKo() throws IOException {
        final Response response = WebClient.create(URI.create("http://localhost:4204/je-blog/user/login"))
                .form(new Form()
                        .set("username", "admin")
                        .set("password", "wrongpwd"));
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }

    @Test
    public void isLoggedKo() { // different session
        final boolean logged = WebClient.create(URI.create("http://localhost:4204/je-blog/user/is-logged")).get(Boolean.class);
        assertFalse(logged);
    }

    @Test
    public void isLoggedOk() {
        final JAXRSClientFactoryBean bean = new JAXRSClientFactoryBean();
        bean.setAddress("http://localhost:4204/je-blog/");
        bean.setProperties(Collections.<String, Object>singletonMap(Message.MAINTAIN_SESSION, Boolean.TRUE));
        final WebClient webClient = bean.createWebClient();
        login(webClient);
        assertTrue(webClient.reset().path("user/is-logged").get(Boolean.class));
    }

    @Test
    public void logout() {
        final JAXRSClientFactoryBean bean = new JAXRSClientFactoryBean();
        bean.setAddress("http://localhost:4204/je-blog/");
        bean.setProperties(Collections.<String, Object>singletonMap(Message.MAINTAIN_SESSION, Boolean.TRUE));
        final WebClient webClient = bean.createWebClient();

        login(webClient);
        webClient.reset().path("user/logout").head();
        assertFalse(webClient.reset().path("user/is-logged").get(Boolean.class));

    }

    private static void login(final WebClient webClient) {
        webClient.path("user/login").form(new Form().set("username", "admin").set("password", "adminpwd"));
    }
}
