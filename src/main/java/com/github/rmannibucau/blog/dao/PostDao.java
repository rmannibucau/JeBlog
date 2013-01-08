package com.github.rmannibucau.blog.dao;

import com.github.rmannibucau.blog.domain.Category;
import com.github.rmannibucau.blog.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostDao extends JpaRepository<Post, Long> {
    List<Post> findByCategory(Category category);
    List<Post> findByStatus(Post.Status status);
}
