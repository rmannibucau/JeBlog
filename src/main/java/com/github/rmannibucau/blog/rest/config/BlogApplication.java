package com.github.rmannibucau.blog.rest.config;

import com.github.rmannibucau.blog.domain.Category;
import com.github.rmannibucau.blog.rest.service.comment.CommentService;
import com.github.rmannibucau.blog.rest.service.post.PostService;
import com.github.rmannibucau.blog.rest.service.user.UserService;
import org.apache.cxf.jaxrs.provider.json.JSONProvider;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/")
public class BlogApplication extends Application {
    private final Set<Class<?>> classes = new HashSet<>();
    private final Set<Object> singletons = new HashSet<>();

    public BlogApplication() {
        // rest services
        classes.add(UserService.class);
        classes.add(PostService.class);
        classes.add(CommentService.class);

        // providers
        singletons.add(new NotAuthorizedExceptionMapper());
        singletons.add(jsonProvider());
    }

    public JSONProvider<Object> jsonProvider() {
        final JSONProvider<Object> jsprovider = new JSONProvider<>();
        jsprovider.setSkipJaxbChecks(true);
        jsprovider.setDropRootElement(true);
        jsprovider.setSupportUnwrapped(true);
        jsprovider.setSingleJaxbContext(true);
        jsprovider.setExtraClass(new Class<?>[]{ Category.class });
        return jsprovider;
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
