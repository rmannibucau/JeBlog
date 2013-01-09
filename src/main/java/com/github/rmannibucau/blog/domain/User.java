package com.github.rmannibucau.blog.domain;

import com.github.rmannibucau.blog.domain.xml.DateAdaptor;

import javax.enterprise.inject.Typed;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

@Entity
@Typed
@Table(name = "jeblog_user")
@NamedQuery(
        name = User.FIND_BY_NAME_AND_PASSWORD,
        query = "select u from User u where u.login = :login and  u.password = :password")
@XmlRootElement
@XmlType(propOrder = {
    "id",
    "login",
    "password",
    "displayName",
    "created"
})
@XmlAccessorType(XmlAccessType.FIELD)
public class User {
    public static final String FIND_BY_NAME_AND_PASSWORD = "User.findByLoginAndPassword";

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
    @Size(max = 255)
    @Column(nullable = false, unique = true)
    private String login;

    @NotNull
    @Size(max = 255)
    @Column(nullable = false)
    private String password;

    @NotNull
    @Size(max = 255)
    @Column(name = "display_name", nullable = false)
    private String displayName;

    @PrePersist
    private void initCreatedDate() {
        created = new Date();
        modified = created;
    }

    @PreUpdate
    private void updateModifiedDate() {
        modified = new Date();
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

    public String getLogin() {
        return login;
    }

    public void setLogin(final String login) {
        this.login = login;
    }

    public User login(final String login) {
        setLogin(login);
        return this;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public User password(final String password) {
        this.password = password;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }

    public User displayName(final String displayName) {
        this.displayName = displayName;
        return this;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!User.class.isInstance(o)) {
            return false;
        }

        final User that = (User) o;

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
