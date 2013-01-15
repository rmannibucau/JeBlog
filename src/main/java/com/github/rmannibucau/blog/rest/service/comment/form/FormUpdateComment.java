package com.github.rmannibucau.blog.rest.service.comment.form;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(propOrder = {
        "id",
        "postId",
        "author",
        "email",
        "content",
        "status"
})
public class FormUpdateComment extends FormComment {
    private long id;
    private String status;

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }
}
