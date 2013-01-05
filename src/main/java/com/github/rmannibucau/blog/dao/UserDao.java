package com.github.rmannibucau.blog.dao;


import com.github.rmannibucau.blog.domain.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface UserDao extends PagingAndSortingRepository<User, Long> {
    User findByNameAndPassword(@Param("login") String name, @Param("password") String password);
}
