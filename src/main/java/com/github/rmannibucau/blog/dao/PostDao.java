package com.github.rmannibucau.blog.dao;

import com.github.rmannibucau.blog.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostDao extends JpaRepository<Post, Long> {
    Page<Post> findByStatus(Post.Status status, Pageable pageable);
}
