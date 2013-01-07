package com.github.rmannibucau.blog.service;

import com.github.rmannibucau.blog.dao.UserDao;
import com.github.rmannibucau.blog.domain.User;
import com.github.rmannibucau.blog.service.exception.AuthenticationFailure;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

@Singleton
@Lock(LockType.READ)
@Path("user")
public class UserService {
    @Inject
    private UserDao dao;

    @POST
    @Path("login")
    public void login(final @FormParam("username") String name, final @FormParam("password") String password,
                      final @Context HttpServletRequest request) {
        if (dao.findByNameAndPassword(name, password) != null) {
            request.getSession(true).setAttribute("name", name);
        } else {
            throw new AuthenticationFailure(name);
        }
    }

    @GET
    @Path("is-logged")
    public boolean isLogged(final @Context HttpServletRequest request) {
        final HttpSession session = request.getSession();
        return session != null && session.getAttribute("name") != null;
    }

    @HEAD
    @Path("logout")
    public void logout(final @Context HttpServletRequest request) {
        final HttpSession session = request.getSession();
        if (session != null) {
            session.invalidate();
        }
    }

    @POST
    @Path("create")
    public User create(final @FormParam("username") String name, final @FormParam("password") String password,
                       final @FormParam("displayName") String displayName) {
        final User user = new User().login(name).password(password).displayName(displayName);
        dao.save(user);
        return user;
    }
}
