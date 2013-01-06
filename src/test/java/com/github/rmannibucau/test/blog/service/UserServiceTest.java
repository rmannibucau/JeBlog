package com.github.rmannibucau.test.blog.service;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.ext.form.Form;
import org.apache.ziplock.IO;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(Arquillian.class)
public class UserServiceTest {
    @Deployment(testable = false)
    public static WebArchive war() {
        return ShrinkWrap.create(WebArchive.class, "je-blog.war")
                .addPackages(true, "com.github.rmannibucau.blog")
                .addAsWebInfResource(new FileAsset(new File("src/main/webapp/WEB-INF/beans.xml")), "beans.xml")
                .addAsWebInfResource(new FileAsset(new File("src/main/webapp/WEB-INF/persistence.xml")), "persistence.xml");
    }

    @Test
    public void login() throws IOException {
        final Response response = WebClient.create(URI.create("http://localhost:4204/je-blog/user/create"))
                .form(new Form()
                        .set("username", "admin")
                        .set("password", "adminpwd"));

        final Object entity = response.getEntity();
        assertThat(entity, instanceOf(InputStream.class));

        final String user = IO.slurp(InputStream.class.cast(entity));
        assertEquals("{\"login\":\"admin\",\"password\":\"adminpwd\",\"displayName\":\"adminpwd\"}", user);
    }
}
