package com.github.rmannibucau.test.blog.importer.wordpress;

import com.github.rmannibucau.blog.importer.Importer;
import com.github.rmannibucau.blog.importer.wordpress.WordpressImporter;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class WordpressImporterTest {
    @Test
    public void importWpFile() {
        final InputStream is = getClass().getResourceAsStream("/wordpress-data.xml");
        assertNotNull(is);

        final Importer.Report r = new WordpressImporter().doImport(is);
        assertEquals(0, r.getPosts());
        assertEquals(0, r.getUsers());
    }
}
