package com.github.rmannibucau.test.blog.service;

import com.github.rmannibucau.blog.domain.User;
import org.apache.cxf.jaxrs.client.JAXRSClientFactoryBean;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.ext.form.Form;
import org.apache.cxf.jaxrs.impl.MetadataMap;
import org.apache.cxf.jaxrs.provider.json.JSONProvider;
import org.apache.cxf.message.Message;
import org.apache.ziplock.IO;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

public abstract class RESTTest {
    protected static final Annotation[] ANNOTATIONS = new Annotation[0];
    protected static final MediaType JSON = MediaType.APPLICATION_JSON_TYPE;
    protected static final MetadataMap<String,String> HEADERS = new MetadataMap<String, String>();
    protected static final String NAME = "admin";
    protected static final String PWD = "adminpwd";

    private static long adminId;

    @BeforeClass
    public static void createAdmin() throws Exception {
        final WebClient client = WebClient.create(URI.create("http://localhost:4204/je-blog/"));
        client.path("user/create").form(new Form().set("username", NAME).set("displayName", "Admin").set("password", PWD));
        adminId = client.reset().path("user/login/admin").get(User.class).getId();
    }

    @AfterClass
    public static void deleteAdmin() {
        WebClient.create(URI.create("http://localhost:4204/je-blog/")).path("user/" + adminId).delete();
    }

    protected static <T> JSONProvider<T> provider(final Class<T> clazz) {
        final JSONProvider<T> jsonProvider = new JSONProvider<T>();
        jsonProvider.setDropRootElement(true);
        jsonProvider.setSupportUnwrapped(true);
        return jsonProvider;
    }

    protected static String json(final Response response) throws IOException {
        final Object entity = response.getEntity();
        assertThat(entity, instanceOf(InputStream.class));
        return IO.slurp(InputStream.class.cast(entity));
    }

    protected static WebClient sessionAwareWebClient() {
        final JAXRSClientFactoryBean bean = new JAXRSClientFactoryBean();
        bean.setAddress("http://localhost:4204/je-blog/");
        bean.setProperties(Collections.<String, Object>singletonMap(Message.MAINTAIN_SESSION, Boolean.TRUE));
        return bean.createWebClient();
    }
}
