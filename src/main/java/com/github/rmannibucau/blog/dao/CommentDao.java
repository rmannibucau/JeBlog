package com.github.rmannibucau.blog.dao;

import com.github.rmannibucau.blog.domain.Comment;
import com.github.rmannibucau.blog.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentDao extends JpaRepository<Comment, Long> {
    List<Comment> findByStatus(Comment.Status status, Pageable pageRequest);
    Page<Comment> findByPost(Post post, Pageable pageRequest);
}
