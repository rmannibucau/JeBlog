package com.github.rmannibucau.blog.dao.util;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class EntityManagerProducer {
    @PersistenceContext(name = "je-blog")
    private EntityManager jeBlogCdiEntityManager;

    @Produces
    @ApplicationScoped
    public EntityManager em() {
        return jeBlogCdiEntityManager;
    }
}
