package com.github.rmannibucau.blog.domain;

import javax.enterprise.inject.Typed;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Typed
@Table(name = "jeblog_post")
public class Post implements Serializable {
    public static enum Status {
        DRAFT, PUBLISHED
    }

    @Id
    @GeneratedValue
    protected Long id;

    @Temporal(TemporalType.TIMESTAMP)
    protected Date created;

    @XmlTransient
    @Temporal(TemporalType.TIMESTAMP)
    protected Date modified;

    @XmlTransient
    @Version
    protected long version;

    private String title;

    @Lob
    private String content;

    @ManyToOne
    private Category category;

    @ManyToOne
    private User author;

    @XmlTransient
    @Enumerated(EnumType.STRING)
    private Status status;

    @XmlTransient
    @OneToMany
    private List<Comment> comments;

    @PrePersist
    public void forceStatus() {
        if (status == null) {
            status = Status.DRAFT;
        }
        created = new Date();
        modified = created;
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
            comments = new ArrayList<>();
        }
        return comments;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public Date getModified() {
        return modified;
    }

    public long getVersion() {
        return version;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!Post.class.isInstance(o)) {
            return false;
        }

        final Post that = (Post) o;

        if (id == null || that.id == null || id <= 0) {
            return this == that;
        }
        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        if (id == null) {
            return super.hashCode();
        }
        return (int) (id ^ (id >>> 32));
    }
}
