package com.github.rmannibucau.blog.security;

import com.github.rmannibucau.blog.rest.exception.NotAuthorizedException;
import com.github.rmannibucau.blog.rest.service.user.UserService;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

@Authenticated
@Interceptor
public class AuthenticatedInterceptor {
    @Inject
    private UserService users;

    @Context
    private HttpServletRequest request;

    @AroundInvoke
    public Object checkUserIsAuthenticated(final InvocationContext ic) throws Exception {
        if (users.currentUser(request.getSession()) == null) {
            throw new NotAuthorizedException();
        }
        return ic.proceed();
    }
}
