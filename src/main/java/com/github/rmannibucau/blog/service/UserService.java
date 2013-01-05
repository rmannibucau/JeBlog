package com.github.rmannibucau.blog.service;

import com.github.rmannibucau.blog.dao.UserDao;
import com.github.rmannibucau.blog.domain.User;
import com.github.rmannibucau.blog.service.exception.AuthenticationFailure;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Singleton
@Lock(LockType.READ)
@Path("user")
public class UserService {
    @Inject
    private UserDao dao;

    @POST
    @Path("login")
    public void login(final @FormParam("username") String name, final @FormParam("password") String password) {
        try {
            dao.findByNameAndPassword(name, password);
        } catch (NoResultException nre) {
            throw new AuthenticationFailure(name);
        }
    }

    @POST
    @Path("create")
    public User create(final @FormParam("username") String name, final @FormParam("password") String password,
                       final @FormParam("password") String displayName) {
        final User user = new User().login(name).password(password).displayName(displayName);
        dao.save(user);
        return user;
    }
}
