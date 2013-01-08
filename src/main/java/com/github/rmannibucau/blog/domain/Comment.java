package com.github.rmannibucau.blog.domain;

import javax.enterprise.inject.Typed;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@Entity
@Typed
@Table(name = "jeblog_comment")
@XmlRootElement
@XmlType(propOrder = {
        "id",
        "author",
        "content",
        "created"
})
public class Comment extends AuditedEntity {
    public static enum Status {
        PENDING, APPROVED
    }

    @NotNull
    private String author;

    @NotNull
    private String email;

    private Status status;

    @Lob
    @Size(min = 3, max = 512)
    private String content;

    @NotNull
    @ManyToOne
    private Post post;

    @PrePersist
    public void forceStatus() {
        if (status == null) {
            status = Status.PENDING;
        }
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(final String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(final Status status) {
        this.status = status;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(final Post post) {
        this.post = post;
    }
}
