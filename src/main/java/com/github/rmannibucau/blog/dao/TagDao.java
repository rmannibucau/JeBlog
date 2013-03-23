package com.github.rmannibucau.blog.dao;

import com.github.rmannibucau.blog.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface TagDao extends JpaRepository<Tag, Long> {
    Tag findByName(String name);
}
