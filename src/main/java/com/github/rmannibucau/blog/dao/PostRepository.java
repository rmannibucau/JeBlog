package com.github.rmannibucau.blog.dao;

import com.github.rmannibucau.blog.dao.api.JpaRepository;
import com.github.rmannibucau.blog.dao.api.Page;
import com.github.rmannibucau.blog.dao.api.PageRequest;
import com.github.rmannibucau.blog.dao.api.Param;
import com.github.rmannibucau.blog.dao.api.Query;
import com.github.rmannibucau.blog.dao.api.Repository;
import com.github.rmannibucau.blog.domain.Post;
import com.github.rmannibucau.blog.domain.Tag;

import javax.enterprise.context.ApplicationScoped;

@Repository
@ApplicationScoped
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByStatus(@Param("status") Post.Status status, PageRequest pageable);
    Page<Post> findByStatusAndTag(@Param("status") Post.Status status, @Param("tag") Tag tag, PageRequest pageable);
}
