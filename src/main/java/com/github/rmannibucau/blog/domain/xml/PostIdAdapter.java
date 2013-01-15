package com.github.rmannibucau.blog.domain.xml;

import com.github.rmannibucau.blog.domain.Post;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class PostIdAdapter extends XmlAdapter<Long, Post> {
    @Override
    public Post unmarshal(final Long v) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public Long marshal(final Post v) throws Exception {
        return v.getId();
    }
}
