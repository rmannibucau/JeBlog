package com.github.rmannibucau.blog.dao.api;

public interface JpaRepository<Entity, Id> {
    void save(Entity e);
    void saveAndFlush(Entity e);
    void deleteById(Id id);
    void delete(Entity e);
    boolean hasId(Entity e);

    @Query(needTransaction = false)
    Entity findById(Id id);
}
