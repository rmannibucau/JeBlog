package com.github.rmannibucau.blog.importer.wordpress;

import com.github.rmannibucau.blog.importer.Importer;
import com.github.rmannibucau.blog.importer.ParsingException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class WordpressImporter implements Importer {
    private static final SAXParserFactory FACTORY = SAXParserFactory.newInstance();
    static {
        FACTORY.setNamespaceAware(true);
        FACTORY.setValidating(false);
    }

    @Override
    public synchronized Report doImport(final InputStream inputStream) {
        final int posts = 0;
        final int users = 0;

        BufferedInputStream is = null;
        try {
            is = new BufferedInputStream(inputStream);

            final SAXParser parser = FACTORY.newSAXParser();
            final WordpressXmlParser handler = new WordpressXmlParser();
            parser.parse(is, handler);
        } catch (final Exception e) {
            throw new ParsingException("maybe consider adding to your xml file 'xmlns:atom=\"http://www.w3.org/2005/Atom\"'", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // no-op
                }
            }
        }

        return new Report(posts, users);
    }

    private static class WordpressXmlParser extends DefaultHandler {
        private StringBuilder characters = new StringBuilder();

        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            characters = new StringBuilder();
        }

        public void characters(char ch[], int start, int length) {
            characters.append(new String(ch, start, length));
        }

        public void endElement(String uri, String localName, String qName) {
            final String txt = characters.toString();

            if ("http://wordpress.org/export/1.2/".equals(uri)) {
                if ("author_login".equals(localName)) {

                } else if ("author_email".equals(localName)) {

                } else if ("author_first_name".equals(localName)) {

                } else if ("wp_author".equals(localName)) {

                }
            } else if (uri == null || uri.isEmpty()) {
                if ("title".equals(localName)) {

                } else if ("pubDate".equals(localName)) {

                } else if ("post_date".equals(localName)) {

                } else if ("status".equals(localName)) {

                } else if ("description".equals(localName)) {

                }
            } else if ("http://purl.org/dc/elements/1.1/".equals(uri)) {
                if ("creator".equals(localName)) {

                }
            } else if ("http://purl.org/rss/1.0/modules/content/".equals(uri)) {
                if ("encoded".equals(localName)) {

                }
            }

            // debug
            System.out.println(uri + " :: " + localName + "\n\n" + txt);
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        }
    }
}
