package com.github.rmannibucau.blog.domain;

import com.github.rmannibucau.blog.processor.ContentProcessor;
import org.apache.deltaspike.core.api.provider.BeanProvider;

import javax.enterprise.inject.Typed;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Entity
@Typed
@Table(name = "jeblog_post")
public class Post implements Serializable {
    public enum Status {
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

    private String format;

    @Lob
    private String content;

    @Lob
    private String html;

    @ManyToMany(fetch = FetchType.EAGER)
    @OrderBy("name ASC")
    private Set<Tag> tags;

    @ManyToOne
    private User author;

    @XmlTransient
    @Enumerated(EnumType.STRING)
    private Status status;

    @XmlTransient
    @OneToMany
    private List<Comment> comments;

    @PrePersist
    public void forceStatusAndAudit() {
        if (status == null) {
            status = Status.DRAFT;
        }

        modified();
        created = modified;

        computeHtml();
    }

    @PreUpdate
    public void computeHtml() {
        if (content != null) {
            final String newHtml = BeanProvider.getContextualReference(ContentProcessor.class).toHtml(format, content);
            if (!newHtml.equals(html)) {
                modified();
            }
            html = newHtml;
        } else {
            if (html != null) {
                modified();
            }
            html = null;
        }
    }

    private void modified() {
        modified = new Date();
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

    public Set<Tag> getTags() {
        if (tags == null) {
            tags = new TreeSet<>();
        }
        return tags;
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

    public void setCreated(Date created) {
        this.created = created;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public Date getModified() {
        return modified;
    }

    public long getVersion() {
        return version;
    }

    public String getHtml() {
        return html;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(final String format) {
        this.format = format;
    }

    public Collection<String> getTagsAsString() {
        final Collection<String> list = new TreeSet<>();
        for (final Tag t : tags) {
            list.add(t.getName());
        }
        return list;
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
