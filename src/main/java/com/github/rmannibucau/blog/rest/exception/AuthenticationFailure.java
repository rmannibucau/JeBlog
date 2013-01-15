package com.github.rmannibucau.blog.rest.exception;

public class AuthenticationFailure extends RuntimeException {
    public AuthenticationFailure(final String name) {
        super("Can't log in " + name);
    }
}
