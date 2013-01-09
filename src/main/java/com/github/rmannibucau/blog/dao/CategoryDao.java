package com.github.rmannibucau.blog.dao;

import com.github.rmannibucau.blog.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface CategoryDao extends JpaRepository<Category, Long> {
    Category findByName(@Param("login") String name);
}
