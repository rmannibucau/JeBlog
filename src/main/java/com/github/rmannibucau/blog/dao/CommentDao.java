package com.github.rmannibucau.blog.dao;

import com.github.rmannibucau.blog.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentDao extends JpaRepository<Comment, Long> {
    List<Comment> findByStatus(Comment.Status status);
}
