package com.github.rmannibucau.blog.domain;

import com.github.rmannibucau.blog.domain.xml.DateAdaptor;

import javax.enterprise.inject.Typed;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

@Entity
@Typed
@Table(name = "jeblog_category")
@XmlRootElement
@XmlType(propOrder = {
        "id",
        "name",
        "description",
        "created"
})
public class Category {
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

    @Column(unique = true)
    private String name;
    private String description;


    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
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
        if (!Category.class.isInstance(o)) {
            return false;
        }

        final Category that = (Category) o;

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
