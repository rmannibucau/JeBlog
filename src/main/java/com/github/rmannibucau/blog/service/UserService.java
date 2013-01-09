package com.github.rmannibucau.blog.service;

import com.github.rmannibucau.blog.dao.UserDao;
import com.github.rmannibucau.blog.domain.User;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@Singleton
@Lock(LockType.READ)
@Path("user")
public class UserService {
    public static final String SESSION_USER = "name";
    public static final String DEFAULT_DISPLAY_NAME = "?";

    @Inject
    private UserDao users;

    @POST
    @Path("login")
    public Response login(final @FormParam("username") String name,
                          final @FormParam("password") String password,
                          final @Context HttpServletRequest request) {
        if (users.findByLoginAndPassword(name, password) == null) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        request.getSession(true).setAttribute(SESSION_USER, name);
        return Response.status(Response.Status.OK).build();
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
    public User create(final @FormParam("username") String name,
                       final @FormParam("password") String password,
                       final @FormParam("displayName") @DefaultValue(DEFAULT_DISPLAY_NAME) String displayName) {
        final User user = new User().login(name).password(password);
        setDisplayName(user, name, displayName);
        return users.saveAndFlush(user);
    }

    @POST
    @Path("{id}")
    public User update(final @PathParam("id") long id,
                       final @FormParam("username") String name,
                       final @FormParam("password") String password,
                       final @FormParam("displayName") @DefaultValue(DEFAULT_DISPLAY_NAME) String displayName) {
        final User user = users.findOne(id);
        user.login(name).password(password);
        setDisplayName(user, name, displayName);
        return users.saveAndFlush(user);
    }

    @DELETE
    @Path("{id}")
    public void delete(final @PathParam("id") long id) {
        users.delete(id);
    }

    @GET
    @Path("{id}")
    public User read(final @PathParam("id") long id) {
        return users.findOne(id);
    }

    @GET
    @Path("/login/{login}")
    public User readByLogin(final @PathParam("login") String name) {
        return users.findByLogin(name);
    }

    public String currentUser(final HttpSession session) {
        if (session == null) {
            return null;
        }
        return (String) session.getAttribute(SESSION_USER);
    }

    private static void setDisplayName(final User user, final String name, final String displayName) {
        if (DEFAULT_DISPLAY_NAME.equals(user.getDisplayName())) {
            user.displayName(name);
        } else {
            user.displayName(displayName);
        }
    }
}
