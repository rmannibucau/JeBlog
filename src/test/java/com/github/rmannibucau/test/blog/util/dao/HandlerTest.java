package com.github.rmannibucau.test.blog.util.dao;

import com.github.rmannibucau.blog.dao.UserRepository;
import com.github.rmannibucau.blog.domain.User;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.apache.deltaspike.core.impl.config.ConfigurationExtension;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.util.Collection;

import static org.apache.ziplock.JarLocation.jarLocation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
public class HandlerTest {
    @Deployment
    public static Archive<?> war() {
        return ShrinkWrap.create(WebArchive.class, "dao.war")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addClass(UserRepository.class)
                .addPackages(true, "com.github.rmannibucau.blog.dao.api")
                .addPackages(true, "com.github.rmannibucau.blog.dao.internal")
                .addPackages(true, "com.github.rmannibucau.blog.domain")
                .addAsWebInfResource(new FileAsset(new File("src/main/webapp/WEB-INF/beans.xml")), "beans.xml")
                .addAsWebInfResource(new FileAsset(new File("src/main/webapp/WEB-INF/persistence.xml")), "persistence.xml")
                .addAsLibraries(jarLocation(ConfigurationExtension.class))
                .addAsLibraries(jarLocation(BeanProvider.class));
    }

    @Inject
    private UserRepository dao;

    @PersistenceContext(name = "je-blog")
    private EntityManager em;

    @Test
    public void save() {
        final User e = new User();
        e.setLogin("anewuser");
        e.setDisplayName("user");
        e.setPassword("asimplepassword");
        dao.saveAndFlush(e);

        final User byId = em.find(User.class, e.getId());
        assertNotNull(byId);
        assertEquals("user", byId.getDisplayName());
        assertEquals("anewuser", byId.getLogin());
    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void findById() {
        final User e = new User();
        e.setLogin("anewuser");
        e.setDisplayName("user");
        e.setPassword("asimplepassword");
        em.persist(e);
        em.flush();

        final User byId = dao.findById(e.getId());
        assertNotNull(byId);
        assertEquals("user", byId.getDisplayName());
        assertEquals("anewuser", byId.getLogin());

        dao.deleteById(e.getId());
    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void findByNamedQuery() {
        final User e = new User();
        e.setLogin("anewuser");
        e.setDisplayName("user");
        e.setPassword("asimplepassword");
        em.persist(e);
        em.flush();

        final User byId = dao.findByLoginAndPassword(e.getLogin(), e.getPassword());
        assertNotNull(byId);
        assertEquals("user", byId.getDisplayName());
        assertEquals("anewuser", byId.getLogin());
    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void count() {
        final long init = em.createQuery("select count(e) from User e", Long.class).getSingleResult();

        final User e = new User();
        e.setLogin("anewuser2");
        e.setDisplayName("user2");
        e.setPassword("asimplepassword2");
        em.persist(e);
        em.flush();

        assertEquals(init + 1, dao.countAll());
    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void namedQueryWithCollection() {
        final User e = new User();
        e.setLogin("anewuser3");
        e.setDisplayName("user3");
        e.setPassword("asimplepassword3");
        em.persist(e);
        em.flush();

        final Collection<User> list = dao.findAll();
        assertNotNull(list);
        assertTrue(list.size() >= 1);
        assertTrue(list.contains(e));
    }
}
