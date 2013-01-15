package com.github.rmannibucau.blog.rest.service.comment.form;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(propOrder = {
        "postId",
        "author",
        "email",
        "content"
})
public class FormComment {
    private long postId;
    private String author;
    private String email;
    private String content;

    public long getPostId() {
        return postId;
    }

    public void setPostId(final long postId) {
        this.postId = postId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(final String author) {
        this.author = author;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }
}
