package com.github.rmannibucau.test.blog.util;

import org.apache.deltaspike.core.api.config.view.metadata.ViewConfigResolver;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.apache.deltaspike.core.impl.config.ConfigurationExtension;
import org.apache.deltaspike.jsf.impl.config.view.ViewConfigResolverProducer;
import org.apache.deltaspike.security.api.authorization.annotation.Secured;
import org.apache.deltaspike.security.impl.extension.SecurityExtension;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;

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
                        // deltaspike dependencies
                .addAsLibraries(jarLocation(ViewConfigResolver.class))
                .addAsLibraries(jarLocation(ViewConfigResolverProducer.class))
                .addAsLibraries(jarLocation(Secured.class))
                .addAsLibraries(jarLocation(SecurityExtension.class))
                .addAsLibraries(jarLocation(ConfigurationExtension.class))
                .addAsLibraries(jarLocation(BeanProvider.class));
    }
}
