package com.github.rmannibucau.test.blog.rest.util;

import com.mysema.query.jpa.JPQLSubQuery;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.springframework.aop.config.AdviceEntry;
import org.springframework.asm.AnnotationVisitor;
import org.springframework.beans.annotation.AnnotationBeanUtils;
import org.springframework.cache.interceptor.CachePutOperation;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.dao.support.DaoSupport;
import org.springframework.data.convert.EntityConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.expression.BeanResolver;
import org.springframework.jdbc.core.metadata.CallMetaDataProvider;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.io.File;

import static org.apache.ziplock.JarLocation.jarLocation;

public final class ShrinkWraps {
    private ShrinkWraps() {
        // no-op
    }

    @Deployment(testable = false)
    public static WebArchive javaEEBlog() {
        return ShrinkWrap.create(WebArchive.class, "je-blog.war")
                        // main code
                .addPackages(true, "com.github.rmannibucau.blog")
                        // configuration
                .addAsWebInfResource(new FileAsset(new File("src/main/webapp/WEB-INF/beans.xml")), "beans.xml")
                .addAsWebInfResource(new FileAsset(new File("src/main/webapp/WEB-INF/persistence.xml")), "persistence.xml")
                .addAsWebInfResource(new FileAsset(new File("src/main/webapp/WEB-INF/openejb-jar.xml")), "openejb-jar.xml")
                .addAsWebInfResource(new FileAsset(new File("src/main/webapp/WEB-INF/resources.xml")), "resources.xml")
                        // spring-data-jpa dependencies
                .addAsLibraries(jarLocation(EntityConverter.class))
                .addAsLibraries(jarLocation(AdviceEntry.class))
                .addAsLibraries(jarLocation(AnnotationVisitor.class))
                .addAsLibraries(jarLocation(AnnotationBeanUtils.class))
                .addAsLibraries(jarLocation(CachePutOperation.class))
                .addAsLibraries(jarLocation(AnnotationAttributes.class))
                .addAsLibraries(jarLocation(BeanResolver.class))
                .addAsLibraries(jarLocation(CallMetaDataProvider.class))
                .addAsLibraries(jarLocation(ObjectOptimisticLockingFailureException.class))
                .addAsLibraries(jarLocation(DaoSupport.class))
                .addAsLibraries(jarLocation(JpaRepository.class))
                .addAsLibraries(jarLocation(JPQLSubQuery.class))
                        // deltaspike dependencies
                .addAsLibraries(jarLocation(BeanProvider.class));
    }
}
