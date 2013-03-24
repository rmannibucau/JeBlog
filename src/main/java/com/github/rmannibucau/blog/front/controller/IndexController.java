package com.github.rmannibucau.blog.front.controller;

import com.github.rmannibucau.blog.dao.PostDao;
import com.github.rmannibucau.blog.dao.Repository;
import com.github.rmannibucau.blog.dao.TagDao;
import com.github.rmannibucau.blog.domain.Post;
import com.github.rmannibucau.blog.domain.Tag;
import com.github.rmannibucau.blog.front.dto.PostDto;
import com.github.rmannibucau.blog.processor.ContentProcessor;
import org.apache.commons.lang3.StringUtils;
import org.apache.deltaspike.core.api.config.annotation.ConfigProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("index")
@ViewScoped
public class IndexController implements Serializable {
    @Inject
    @Repository
    private PostDao posts;

    @Inject
    @Repository
    private TagDao tags;

    @Inject
    @ConfigProperty(name = "jeblog.page-size", defaultValue = "15")
    private Integer pageSize;

    @Inject
    @ConfigProperty(name = "jeblog.preview-size", defaultValue = "100")
    private Integer previewSize;

    @Inject
    private ContentProcessor processor;

    @Inject
    private EntityManager em;

    private int pageIndex = 0;
    private Page<PostDto> page;
    private String tag;

    // @PostConstruct // need viewParam so don't use cdi here
    public void init() {
        setPage(0); // force page to be != null
    }

    public List<PostDto> getPosts() {
        return page.getContent();
    }

    public void nextPage() {
        setPage(++pageIndex);
    }

    public void previousPage() {
        setPage(Math.max(0, --pageIndex));
    }

    public void beginning() {
        setPage(0);
    }

    public void end() {
        setPage(page.getTotalPages() - 1);
    }

    public boolean getHasPrevious() {
        return page.hasPreviousPage();
    }

    public boolean getHasNext() {
        return page.hasNextPage();
    }

    public int getCurrent() {
        return page.getNumber() + 1;
    }

    private void setPage(final int idx) {
        pageIndex = idx;

        final PageRequest pageable = new PageRequest(idx, pageSize, new Sort(Sort.Direction.DESC, "created"));

        final Page<Post> queryResult;
        if (tag == null) {
            queryResult = posts.findByStatus(Post.Status.PUBLISHED, pageable);
        } else {
            final Tag tagValue = tags.findByName(tag);
            if (tagValue == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Can't find tag " + tag, null));
                queryResult = posts.findByStatus(Post.Status.PUBLISHED, pageable);
            } else {
                queryResult = new PageImpl<>(
                        em.createQuery("select p from Post p where :tag member of p.tags and p.status = :status", Post.class)
                            .setParameter("status", Post.Status.PUBLISHED)
                            .setParameter("tag", tagValue).getResultList(),
                        pageable,
                        em.createQuery("select count(p) from Post p where :tag member of p.tags and p.status = :status", Long.class)
                            .setParameter("status", Post.Status.PUBLISHED)
                            .setParameter("tag", tagValue).getSingleResult());
            }
        }


        page = new PageImpl<>(previewContent(queryResult), pageable, queryResult.getTotalElements());
    }

    private List<PostDto> previewContent(final Page<Post> byStatus) {
        final List<PostDto> posts = new ArrayList<>();
        for (final Post p : byStatus) {
            posts.add(new PostDto(p.getId(), p.getTitle(),
                    processor.toHtml(p.getFormat(), StringUtils.abbreviate(p.getContent(), previewSize)), p.getFormat(),
                    p.getCreated(), p.getModified(), p.getAuthor().getDisplayName(),
                    p.getTagsAsString(), p.getStatus()));
        }
        return posts;
    }

    public void setTag(final String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }
}
