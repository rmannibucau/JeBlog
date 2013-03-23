package com.github.rmannibucau.blog.front.dto;

import com.github.rmannibucau.blog.domain.Post;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.TreeSet;

public class PostDto implements Serializable {
    private long id;
    private String title;
    private String format;
    private String content;
    private Date created;
    private Date modified;
    private String author;
    private Collection<String> tags;
    private Post.Status status;

    public PostDto() {
        this.tags = new TreeSet<>();
    }

    public PostDto(final long id, final String title, final String content,
                   final String format, final Date created, final Date modified,
                   final String user, final Collection<String> tags, final Post.Status status) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.format = format;
        this.created = created;
        this.modified = modified;
        this.author = user;
        this.status = status;
        this.tags = tags;
        if (this.tags == null) {
            this.tags = new TreeSet<>();
        }
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
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

    public String[] getTags() {
        return tags.toArray(new String[tags.size()]);
    }

    public Post.Status getStatus() {
        return status;
    }

    public void setStatus(final Post.Status status) {
        this.status = status;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void addTag(final String name) {
        tags.add(name);
    }

    public void removeTag(final String name) {
        tags.remove(name);
    }
}
