package com.github.rmannibucau.blog.service.config;

import com.github.rmannibucau.blog.service.exception.AuthenticationFailure;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AuthenticationFailureMapper implements ExceptionMapper<AuthenticationFailure> {
    @Override
    public Response toResponse(final AuthenticationFailure throwable) {
        return Response.status(Response.Status.FORBIDDEN).build();
    }
}
