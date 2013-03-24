package com.github.rmannibucau.blog.dao;

import com.github.rmannibucau.blog.dao.api.Repository;
import com.github.rmannibucau.blog.dao.api.JpaRepository;
import com.github.rmannibucau.blog.domain.Comment;

import javax.enterprise.context.ApplicationScoped;

@Repository
@ApplicationScoped
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
