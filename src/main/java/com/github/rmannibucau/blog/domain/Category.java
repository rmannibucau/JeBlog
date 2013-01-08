package com.github.rmannibucau.blog.domain;

import javax.enterprise.inject.Typed;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@Entity
@Typed
@Table(name = "jeblog_category")
@XmlRootElement
@XmlType(propOrder = {
        "name",
        "description"
})
public class Category extends AuditedEntity {
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
}
