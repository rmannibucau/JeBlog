package com.github.rmannibucau.test.blog;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import java.io.File;

public final class ShrinkWraps {
    private ShrinkWraps() {
        // no-op
    }

    public static WebArchive javaEEBlog() {
        return ShrinkWrap.create(WebArchive.class, "je-blog.war")
                .addPackages(true, "com.github.rmannibucau.blog")
                .addAsWebInfResource(new FileAsset(new File("src/main/webapp/WEB-INF/beans.xml")), "beans.xml")
                .addAsWebInfResource(new FileAsset(new File("src/main/webapp/WEB-INF/persistence.xml")), "persistence.xml");
    }
}
