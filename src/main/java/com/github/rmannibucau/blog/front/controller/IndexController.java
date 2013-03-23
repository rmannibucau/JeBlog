package com.github.rmannibucau.blog.front.controller;

import com.github.rmannibucau.blog.dao.PostDao;
import com.github.rmannibucau.blog.dao.Repository;
import com.github.rmannibucau.blog.domain.Post;
import com.github.rmannibucau.blog.front.Navigation;
import com.github.rmannibucau.blog.front.dto.PostDto;
import com.petebevin.markdown.MarkdownProcessor;
import org.apache.commons.lang3.StringUtils;
import org.apache.deltaspike.core.api.config.annotation.ConfigProperty;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigResolver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
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
    private ViewConfigResolver configResolver;

    @Inject
    @ConfigProperty(name = "jeblog.page-size", defaultValue = "15")
    private Integer pageSize;

    @Inject
    @ConfigProperty(name = "jeblog.preview-size", defaultValue = "100")
    private Integer previewSize;

    private int pageIndex = 0;
    private Page<PostDto> page;

    @PostConstruct
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
        final Page<Post> byStatus = posts.findByStatus(Post.Status.PUBLISHED, pageable);
        page = new PageImpl<>(previewContent(byStatus), pageable, byStatus.getTotalElements());
    }

    private List<PostDto> previewContent(final Page<Post> byStatus) {
        final List<PostDto> posts = new ArrayList<>();
        for (final Post p : byStatus) {
            posts.add(new PostDto(p.getId(), p.getTitle(),
                    new MarkdownProcessor().markdown(StringUtils.abbreviate(p.getContent(), previewSize)),
                    p.getCreated(), p.getModified(), p.getAuthor().getDisplayName(),
                    p.getCategoryAsString(), p.getStatus()));
        }
        return posts;
    }
}
