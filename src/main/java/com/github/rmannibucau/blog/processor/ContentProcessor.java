package com.github.rmannibucau.blog.processor;

import com.petebevin.markdown.MarkdownProcessor;

import javax.enterprise.context.ApplicationScoped;
import java.io.Serializable;

@ApplicationScoped
public class ContentProcessor implements Serializable {
    private static final String HTML = "Html";
    private static final String MARDOWN = "Mardown";

    public static final String[] FORMATS = new String[] { HTML, MARDOWN };

    public String toHtml(final String format, final String content) {
        if (HTML.equals(format)) {
            return content;
        }
        // default
        return new MarkdownProcessor().markdown(content);
    }

    public String[] getFormats() {
        return FORMATS;
    }

    public String getDefaultFormat() {
        return MARDOWN;
    }
}
