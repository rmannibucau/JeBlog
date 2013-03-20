package com.github.rmannibucau.blog.front;

import com.github.rmannibucau.blog.dao.PostDao;
import com.github.rmannibucau.blog.dao.Repository;
import com.github.rmannibucau.blog.domain.Post;
import org.apache.deltaspike.core.api.config.annotation.ConfigProperty;
import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigResolver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
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

    private int pageIndex = 0;
    private Page<Post> page;

    @PostConstruct
    public void init() {
        setPage(0); // force page to be != null
    }

    public List<Post> getPosts() {
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
        page = posts.findByStatus(Post.Status.PUBLISHED, pageable);
    }

    public Class<? extends Navigation> getPostPage() {
        return Navigation.Post.class;
    }
}
