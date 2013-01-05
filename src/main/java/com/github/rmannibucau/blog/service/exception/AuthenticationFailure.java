package com.github.rmannibucau.blog.service.exception;

public class AuthenticationFailure extends RuntimeException {
    public AuthenticationFailure(final String name) {
        super("Can't log in " + name);
    }
}
