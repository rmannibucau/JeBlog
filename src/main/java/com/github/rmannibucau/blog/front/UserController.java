package com.github.rmannibucau.blog.front;

import com.github.rmannibucau.blog.dao.Repository;
import com.github.rmannibucau.blog.dao.UserDao;
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
    private String password;
    private boolean validated = false;

    @Inject
    @Repository
    private UserDao users;

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

    public Class<? extends Navigation> getLoginPage() {
        return Navigation.Login.class;
    }

    public Class<? extends Navigation> login() {
        final User user = users.findByLoginAndPassword(name, password);
        if (user == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login and password don't match", null));
            return null;
        }

        name = user.getDisplayName();
        validated = true;
        password = null; // don't keep it in memory
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Login successful", null));
        return Navigation.Index.class;
    }

    public Class<? extends Navigation> logout() {
        validated = false;
        name = ANONYMOUS;
        password = null;
        return Navigation.Index.class;
    }

    public boolean isLogged() {
        return validated;
    }
}
