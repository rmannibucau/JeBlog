package com.github.rmannibucau.blog.importer;

import java.io.InputStream;
import java.io.Serializable;

public interface Importer {
    Report doImport(InputStream inputStream);

    public static class Report implements Serializable {
        private int posts;
        private int users;

        public Report(final int posts, final int users) {
            this.posts = posts;
            this.users = users;
        }

        public Report() {
            // no-op
        }

        public int getPosts() {
            return posts;
        }

        public int getUsers() {
            return users;
        }
    }
}
