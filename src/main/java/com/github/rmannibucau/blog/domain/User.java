package com.github.rmannibucau.blog.domain;

import javax.enterprise.inject.Typed;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@Entity
@Typed
@Table(name = "jeblog_user")
@NamedQuery(
        name = User.FIND_BY_NAME_AND_PASSWORD,
        query = "select u from User u where u.login = :login and  u.password = :password")
@XmlRootElement
@XmlType(propOrder = {
    "login",
    "password",
    "displayName"
})
public class User extends AuditedEntity {
    public static final String FIND_BY_NAME_AND_PASSWORD = "User.findByNameAndPassword";

    @NotNull
    @Size(max = 255)
    @Column(nullable = false)
    private String login;

    @NotNull
    @Size(max = 255)
    @Column(nullable = false)
    private String password;

    @NotNull
    @Size(max = 255)
    @Column(nullable = false)
    private String displayName;

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
}
