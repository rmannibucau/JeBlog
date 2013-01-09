package com.github.rmannibucau.blog.service;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(propOrder = {
        "id",
        "title",
        "content",
        "category"
})
public class FormUpdatePost extends FormPost {
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
