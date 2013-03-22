package com.github.rmannibucau.blog.front.dto;

import com.github.rmannibucau.blog.domain.Post;

import java.util.Date;

public class PostDto {
    private long id;
    private String title;
    private String content;
    private Date created;
    private Date modified;
    private String author;
    private String category;
    private Post.Status status;

    public PostDto() {
        // no-op
    }

    public PostDto(final long id, final String title, final String content,
                   final Date created, final Date modified,
                   final String user, final String category, final Post.Status status) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.created = created;
        this.modified = modified;
        this.author = user;
        this.category = category;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Date getCreated() {
        return created;
    }

    public Date getModified() {
        return modified;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }

    public Post.Status getStatus() {
        return status;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public void setCategory(final String category) {
        this.category = category;
    }

    public void setStatus(final Post.Status status) {
        this.status = status;
    }
}
