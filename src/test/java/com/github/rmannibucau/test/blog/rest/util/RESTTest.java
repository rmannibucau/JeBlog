package com.github.rmannibucau.test.blog.rest.util;

import com.github.rmannibucau.blog.domain.Post;
import org.apache.cxf.jaxrs.client.JAXRSClientFactoryBean;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.ext.form.Form;
import org.apache.cxf.jaxrs.impl.MetadataMap;
import org.apache.cxf.jaxrs.provider.json.JSONProvider;
import org.apache.cxf.message.Message;
import org.apache.ziplock.IO;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

public final class RESTTest {
    protected static final Annotation[] ANNOTATIONS = new Annotation[0];
    protected static final MediaType JSON = MediaType.APPLICATION_JSON_TYPE;
    protected static final MetadataMap<String,String> HEADERS = new MetadataMap<>();

    public static final String NAME = "admin";
    public static final String PWD = "adminpwd";

    private static JSONProvider<Object> jsonProvider = newJSonProvider();

    private RESTTest() {
        // no-op
    }

    private static JSONProvider<Object> newJSonProvider() {
        final JSONProvider<Object> jsprovider = new JSONProvider<>();
        jsprovider.setSkipJaxbChecks(true);
        jsprovider.setDropRootElement(true);
        jsprovider.setSupportUnwrapped(true);
        jsprovider.setSingleJaxbContext(true);
        return jsprovider;
    }

    public static String json(final Response response) throws IOException {
        final Object entity = response.getEntity();
        assertThat(entity, instanceOf(InputStream.class));
        return IO.slurp(InputStream.class.cast(entity));
    }

    public static WebClient newWebClient() {
        final JAXRSClientFactoryBean bean = new JAXRSClientFactoryBean();
        bean.setAddress("http://localhost:" + System.getProperty("httpejbd.port", "4204") + "/je-blog/");
        bean.setProperties(Collections.<String, Object>singletonMap(Message.MAINTAIN_SESSION, Boolean.TRUE));
        return bean.createWebClient();
    }

    public static <T> T unserialize(final Class<T> clazz, final Response response) throws IOException {
        return (T) jsonProvider.readFrom((Class<Object>) clazz, clazz,
                                ANNOTATIONS, JSON, HEADERS, new ByteArrayInputStream(json(response).getBytes()));
    }

    public static WebClient logout(final WebClient client) {
        client.reset().path("user/logout").head();
        client.reset();
        return client;
    }

    public static WebClient login(final WebClient client) {
        client.reset().path("user/login").form(new Form().set("username", NAME).set("password", PWD));
        client.reset();
        return client;
    }

    public static long createPost(final WebClient webClient, final String name) throws IOException {
        return unserialize(Post.class, webClient.reset().path("post/create")
                    .form(new Form().set("title", name + " title").set("content", "read content").set("category", "read")))
                    .getId();
    }
}
