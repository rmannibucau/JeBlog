package com.github.rmannibucau.blog.rest.config;

import com.github.rmannibucau.blog.rest.exception.NotAuthorizedException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotAuthorizedExceptionMapper implements ExceptionMapper<NotAuthorizedException> {
    @Override
    public Response toResponse(NotAuthorizedException throwable) {
        return Response.status(Response.Status.FORBIDDEN).build();
    }
}
