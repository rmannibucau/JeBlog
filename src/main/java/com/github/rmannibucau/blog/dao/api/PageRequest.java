package com.github.rmannibucau.blog.dao.api;

import java.io.Serializable;

public class PageRequest implements Serializable {
    private final int page;
    private final int size;

    public PageRequest(final int page, final int size) {
        if (0 > page) {
            throw new IllegalArgumentException("Page index must not be less than zero!");
        }

        if (0 >= size) {
            throw new IllegalArgumentException("Page size must not be less than or equal to zero!");
        }

        this.page = page;
        this.size = size;
    }

    public int getPageSize() {
        return size;
    }

    public int getPageNumber() {
        return page;
    }

    public int getOffset() {
        return page * size;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (!PageRequest.class.isInstance(obj)) {
            return false;
        }

        final PageRequest that = PageRequest.class.cast(obj);
        return this.page == that.page && this.size == that.size;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + page;
        result = 31 * result + size;
        return result;
    }
}
