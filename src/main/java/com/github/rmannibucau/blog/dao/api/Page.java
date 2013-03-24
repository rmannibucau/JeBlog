package com.github.rmannibucau.blog.dao.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Page<T> implements Serializable, Iterable<T> {
    private final List<T> content = new ArrayList<T>();
    private final PageRequest request;
    private final long total;

    public Page(final List<T> content, final PageRequest request, final long total) {
        if (null == content) {
            throw new IllegalArgumentException("Content must not be null!");
        }
        if (null == request) {
            throw new IllegalArgumentException("PageRequest must not be null!");
        }

        this.content.addAll(content);
        this.total = total;
        this.request = request;
    }

    public int getNumber() {
        return request.getPageNumber();
    }

    public int getSize() {
        return request.getPageSize();
    }

    public int getTotalPages() {
        return getSize() == 0 ? 0 : (int) Math.ceil(total / (double) getSize());
    }

    public int getNumberOfElements() {
        return content.size();
    }

    public long getTotalElements() {
        return total;
    }

    public boolean hasPreviousPage() {
        return getNumber() > 0;
    }

    public boolean isFirstPage() {
        return !hasPreviousPage();
    }

    public boolean hasNextPage() {
        return (getNumber() + 1) * getSize() < total;
    }

    public boolean isLastPage() {
        return !hasNextPage();
    }

    public List<T> getContent() {
        return Collections.unmodifiableList(content);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (!Page.class.isInstance(obj)) {
            return false;
        }

        final Page<?> that = Page.class.cast(obj);
        return total == that.total && content.equals(that.content)
            && (request == null ? that.request == null : request.equals(that.request));
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (int) (total ^ total >>> 32);
        result = 31 * result + (request == null ? 0 : request.hashCode());
        result = 31 * result + content.hashCode();
        return result;
    }

    @Override
    public Iterator<T> iterator() {
        return content.iterator();
    }
}
