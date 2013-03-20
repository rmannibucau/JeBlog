package com.github.rmannibucau.blog.importer;

public class ParsingException extends RuntimeException {
    public ParsingException(final Exception e) {
        super(e);
    }

    public ParsingException(final String s, final Exception e) {
        super(s, e);
    }
}
