package com.github.rmannibucau.blog.domain;

import javax.enterprise.inject.Typed;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@Entity
@Typed
@Table(name = "jeblog_post")
@XmlRootElement
@XmlType(propOrder = {
        "id",
        "title",
        "content",
        "category",
        "author",
        "status",
        "created"
})
public class Post extends AuditedEntity {
    public static enum Status {
        DRAFT, PUBLISHED
    }

    private String title;

    @Lob
    private String content;

    @ManyToOne
    private Category category;

    @ManyToOne
    private User author;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany
    private List<Comment> comments;

    @PrePersist
    public void forceStatus() {
        if (status == null) {
            status = Status.DRAFT;
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(final Category category) {
        this.category = category;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(final User author) {
        this.author = author;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(final Status status) {
        this.status = status;
    }

    public List<Comment> getComments() {
        if (comments == null) {
            comments = new ArrayList<Comment>();
        }
        return comments;
    }
}
