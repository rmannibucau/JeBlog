package com.github.rmannibucau.blog.dao;

import com.github.rmannibucau.blog.domain.Category;
import com.github.rmannibucau.blog.domain.Post;
import com.github.rmannibucau.blog.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostDao extends JpaRepository<Post, Long> {
    List<Post> findByCategory(Category category);
    Page<Post> findByStatus(Post.Status status, Pageable pageable);
    List<Post> findByAuthor(User user);
}
