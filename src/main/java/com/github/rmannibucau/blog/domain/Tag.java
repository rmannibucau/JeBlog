package com.github.rmannibucau.blog.domain;

import javax.enterprise.inject.Typed;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Typed
@Table(name = "jeblog_tag")
@NamedQueries({
    @NamedQuery(name = "Tag.findByName", query = "select t from Tag t where t.name = :name")
})
public class Tag implements Serializable, Comparable<Tag> {
    @Id
    @GeneratedValue
    protected long id;

    @XmlTransient
    @Version
    protected long version;

    @Column(unique = true)
    private String name;

    @ManyToMany(mappedBy = "tags")
    @OrderBy("created DESC")
    private Set<Post> posts;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public long getVersion() {
        return version;
    }

    public Set<Post> getPosts() {
        if (posts == null) {
            posts = new HashSet<>();
        }
        return posts;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!Tag.class.isInstance(o)) {
            return false;
        }

        final Tag that = (Tag) o;

        if (id <= 0) {
            return this == that;
        }
        return id == that.id;

    }

    @Override
    public int hashCode() {
        if (id == 0) {
            return super.hashCode();
        }
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public int compareTo(final Tag o) {
        return name.compareTo(o.name);
    }
}
