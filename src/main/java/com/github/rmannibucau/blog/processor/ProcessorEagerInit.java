package com.github.rmannibucau.blog.processor;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.SessionContext;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

@Singleton
@Startup
public class ProcessorEagerInit {
    @Inject
    private ContentProcessor processor;

    @Resource
    private SessionContext ctx;

    @PostConstruct
    public void forceEagerInit() {
        // force to go through the proxy to get @Async features
        ctx.getBusinessObject(ProcessorEagerInit.class).init();
    }

    @Asynchronous
    public void init() {
        // asciidoctor init is slow so force it to not be done when the first user needs it
        // call whatever method to force @PostConstruct
        processor.getFormats();
    }
}
