package com.github.rmannibucau.blog.dao;

import com.github.rmannibucau.blog.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface UserDao extends JpaRepository<User, Long> {
    User findByLoginAndPassword(@Param("login") String name, @Param("password") String password);
    User findByLogin(String login);
}
