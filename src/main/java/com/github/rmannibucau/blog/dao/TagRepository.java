package com.github.rmannibucau.blog.dao;

import com.github.rmannibucau.blog.dao.api.Param;
import com.github.rmannibucau.blog.dao.api.Repository;
import com.github.rmannibucau.blog.dao.api.JpaRepository;
import com.github.rmannibucau.blog.domain.Tag;

import javax.enterprise.context.ApplicationScoped;

@Repository
@ApplicationScoped
public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findByName(@Param("name") String name);
}
