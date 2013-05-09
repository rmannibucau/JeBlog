package com.github.rmannibucau.blog.processor;

import com.petebevin.markdown.MarkdownProcessor;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.AttributesBuilder;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.SafeMode;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.io.Serializable;

@ApplicationScoped
public class ContentProcessor implements Serializable {
    private static final String HTML = "Html";
    private static final String MARKDOWN = "Markdown";
    private static final String ASCIIDOC = "AsciiDoc";

    private static final String[] FORMATS = new String[] { ASCIIDOC, HTML, MARKDOWN };

    private Asciidoctor asciidoctor;

    @PostConstruct
    public void init() {
        asciidoctor = Asciidoctor.Factory.create();
    }

    public String toHtml(final String format, final String content) {
        if (HTML.equals(format)) {
            return content;
        } else if (MARKDOWN.equals(format)) {
            return new MarkdownProcessor().markdown(content);
        }
        // default == asciidoc
        return asciidoctor.render(content, OptionsBuilder.options()
                .safe(SafeMode.SAFE).backend("html5").headerFooter(false).eruby("erubis")
                .attributes(AttributesBuilder.attributes().attribute("icons!", "")
                .attribute("source-highlighter", "coderay").asMap()).asMap());
    }

    public String[] getFormats() {
        return FORMATS;
    }

    public String getDefaultFormat() {
        return ASCIIDOC;
    }
}
