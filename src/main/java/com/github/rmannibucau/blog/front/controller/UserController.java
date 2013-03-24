package com.github.rmannibucau.blog.front.controller;

import com.github.rmannibucau.blog.dao.UserRepository;
import com.github.rmannibucau.blog.domain.User;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named("user")
@SessionScoped
public class UserController implements Serializable {
    private static final String ANONYMOUS = "Guest";

    private String name = ANONYMOUS;
    private String login;
    private String password;
    private boolean validated = false;

    @Inject
    private UserRepository users;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public Class<? extends Navigation> login() {
        final User user = users.findByLoginAndPassword(name, password);
        if (user == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login and password don't match", null));
            return null;
        }

        name = user.getDisplayName();
        login = user.getLogin();
        validated = true;
        password = null; // don't keep it in memory
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Login successful", null));
        return Navigation.Index.class;
    }

    public Class<? extends Navigation> logout() {
        validated = false;
        name = ANONYMOUS;
        password = null;
        login = null;
        return Navigation.Index.class;
    }

    public boolean isLogged() {
        return validated;
    }
}
