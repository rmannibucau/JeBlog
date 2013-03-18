package com.github.rmannibucau.blog.front;

import com.github.rmannibucau.blog.dao.PostDao;
import com.github.rmannibucau.blog.dao.Repository;
import com.github.rmannibucau.blog.domain.Post;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
public class IndexController extends LazyDataModel<Post> {
    private static final int DEFAULT_PAGE_SIZE = 15;

    @Inject
    @Repository
    private PostDao posts;

    @PostConstruct
    public void initModel() {
        setPageSize(DEFAULT_PAGE_SIZE);
    }

    @Override
    public List<Post> load(final int first, final int pageSize, final List<SortMeta> ignored1, final Map<String, String> ignored2) {
        final PageRequest pageable = new PageRequest(first / pageSize, pageSize,
                                                    new Sort(Sort.Direction.DESC, "created"));
        final Page<Post> page = posts.findByStatus(Post.Status.PUBLISHED, pageable);
        setRowCount(Long.valueOf(page.getTotalElements()).intValue());
        return page.getContent();
    }

    @Override
    public List<Post> load(final int first, final int pageSize, final String sortField, final SortOrder sortOrder, final Map<String,String> filters) {
        return load(first, pageSize, Arrays.asList(new SortMeta(null, sortField, sortOrder, null)), filters);
    }

    @Override
    public Post getRowData(final String rowKey) {
        return posts.findOne(Long.parseLong(rowKey));
    }

    @Override
    public Object getRowKey(final Post object) {
        return object.getId().toString();
    }
}
