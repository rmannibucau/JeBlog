package com.github.rmannibucau.blog.service.config;

import com.github.rmannibucau.blog.service.UserService;
import org.apache.cxf.jaxrs.provider.json.JSONProvider;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/")
public class BlogApplication extends Application {
    private final Set<Class<?>> classes = new HashSet<Class<?>>();
    private final Set<Object> singletons = new HashSet<Object>();

    public BlogApplication() {
        // rest services
        classes.add(UserService.class);

        // providers
        final JSONProvider<Object> jsonProvider = new JSONProvider<Object>();
        jsonProvider.setSerializeAsArray(true);
        jsonProvider.setDropRootElement(true);
        singletons.add(jsonProvider);

        classes.add(AuthenticationFailureMapper.class);
    }

    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
}
