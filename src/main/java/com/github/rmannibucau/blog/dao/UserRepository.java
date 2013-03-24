package com.github.rmannibucau.blog.dao;

import com.github.rmannibucau.blog.dao.api.JpaRepository;
import com.github.rmannibucau.blog.dao.api.Param;
import com.github.rmannibucau.blog.dao.api.Repository;
import com.github.rmannibucau.blog.domain.User;

import javax.enterprise.context.ApplicationScoped;
import java.util.Collection;

@Repository
@ApplicationScoped
public interface UserRepository extends JpaRepository<User, Long> {
    User findByLoginAndPassword(@Param("login") String name, @Param("password") String password);
    User findByLogin(@Param("login") String login);
    Collection<User> findAll();
    long count();
}
