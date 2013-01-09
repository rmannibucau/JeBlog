package com.github.rmannibucau.blog.domain;

import com.github.rmannibucau.blog.domain.xml.DateAdaptor;

import javax.enterprise.inject.Typed;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

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
public class Comment {
    public static enum Status {
        PENDING, APPROVED
    }

    @Id
    @GeneratedValue
    protected Long id;

    @XmlJavaTypeAdapter(DateAdaptor.class)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date created;

    @XmlTransient
    @Temporal(TemporalType.TIMESTAMP)
    protected Date modified;

    @XmlTransient
    @Version
    protected long version;

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
        created = new Date();
        modified = created;
    }

    @PreUpdate
    private void updateModifiedDate() {
        modified = new Date();
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

    public Long getId() {
        return id;
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
        if (!Comment.class.isInstance(o)) {
            return false;
        }

        final Comment that = (Comment) o;

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
