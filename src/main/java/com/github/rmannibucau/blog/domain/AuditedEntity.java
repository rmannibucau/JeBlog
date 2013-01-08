package com.github.rmannibucau.blog.domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class AuditedEntity {
    @Id
    @GeneratedValue
    protected long id;

    @Temporal(TemporalType.TIMESTAMP)
    protected Date created;

    @Temporal(TemporalType.TIMESTAMP)
    protected Date modified;

    @Version
    protected long version;

    @PrePersist
    private void initCreatedDate() {
        created = new Date();
        modified = created;
    }

    @PreUpdate
    private void updateModifiedDate() {
        modified = new Date();
    }

    public long getId() {
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
        if (!AuditedEntity.class.isInstance(o)) {
            return false;
        }

        final AuditedEntity that = (AuditedEntity) o;

        if (id <= 0) {
            return this == that;
        }
        return id == that.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
