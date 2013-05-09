[![Build Status](https://secure.travis-ci.org/rmannibucau/JeBlog.png)](http://travis-ci.org/rmannibucau/JeBlog)

Just a simple Java EE Blog tested with OpenEJB and [Arquillian](http://arquillian.org).

## Running JeBlog

JeBlog is configured to run self-contained, within the project, on embedded [Apache TomEE](http://tomee.apache.org).

To begin, build the application and run the tests:

    mvn package

Next, launch the application into an embedded Apache TomEE instance:

    mvn tomee:run

The first time you run this command, Maven will need to download all the necessary dependencies.

Once you see the following line in the console, you'll know JeBlog is up and running:

    INFO - Server startup

Visit http://localhost:8080 to see the home page of the application.

To make a new post, first sign in using these credentials:

* **username:** jeblog
* **password:** p@ssword

These settings are configured in `src/main/tomee/conf/tomee.xml`.

After signing in, you can write your blog post in AsciiDoc, Markdown or HTML. AsciiDoc is the default choice, powered by [Asciidoctor](http://asciidoctor.org).
