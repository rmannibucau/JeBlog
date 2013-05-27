package com.github.rmannibucau.blog.dao.api;

import java.io.Serializable;

public interface JpaRepository<Entity, Id> extends Serializable {
    void save(Entity e);
    void saveAndFlush(Entity e);
    void deleteById(Id id);
    void delete(Entity e);
    boolean hasId(Entity e);

    @Query(needTransaction = false)
    Entity findById(Id id);
}
