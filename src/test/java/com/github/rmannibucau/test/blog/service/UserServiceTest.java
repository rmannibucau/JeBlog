package com.github.rmannibucau.test.blog.service;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.ext.form.Form;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.net.URI;

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
    public void login() {
        WebClient.create(URI.create("http://localhost:4204/je-blog/user/create"))
                .form(new Form()
                        .set("username", "admin")
                        .set("password", "adminpwd"));
    }
}
