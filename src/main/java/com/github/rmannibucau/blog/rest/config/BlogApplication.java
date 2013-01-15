package com.github.rmannibucau.blog.rest.config;

import com.github.rmannibucau.blog.domain.Category;
import com.github.rmannibucau.blog.domain.Comment;
import com.github.rmannibucau.blog.domain.Post;
import com.github.rmannibucau.blog.domain.User;
import com.github.rmannibucau.blog.rest.service.comment.CommentService;
import com.github.rmannibucau.blog.rest.service.post.PostService;
import com.github.rmannibucau.blog.rest.service.user.UserService;
import org.apache.cxf.jaxrs.provider.json.JSONProvider;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/")
public class BlogApplication extends Application {
    private final Set<Class<?>> classes = new HashSet<Class<?>>();
    private final Set<Object> singletons = new HashSet<Object>();

    public BlogApplication() {
        // rest services
        classes.add(UserService.class);
        classes.add(PostService.class);
        classes.add(CommentService.class);

        // providers
        singletons.add(new NotAuthorizedExceptionMapper());

        final JSONProvider<Object> jsonProvider = new JSONProvider<Object>();
        jsonProvider.setDropRootElement(true);
        jsonProvider.setSupportUnwrapped(true);
        jsonProvider.setSingleJaxbContext(true);
        jsonProvider.setJaxbElementClassNames(Arrays.asList(Category.class.getName(), Comment.class.getName(), Post.class.getName(), User.class.getName()));
        singletons.add(jsonProvider);
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
