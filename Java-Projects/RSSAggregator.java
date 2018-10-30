import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.xmltree.XMLTree;
import components.xmltree.XMLTree1;

/**
 * This program inputs an XML file with RSS feeds (version 2.0) and outputs an
 * index of RSS html files created from URLs from the XML input.
 *
 * @author Tyler McDowell
 *
 */
public final class RSSAggregator {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private RSSAggregator() {
    }

    /**
     * Finds the first occurrence of the given tag among the children of the
     * given {@code XMLTree} and return its index; returns -1 if not found.
     *
     * @param xml
     *            the {@code XMLTree} to search
     * @param tag
     *            the tag to look for
     * @return the index of the first child of the {@code XMLTree} matching the
     *         given tag or -1 if not found
     * @requires [the label of the root of xml is a tag]
     * @ensures <pre>
     * getChildElement =
     *  [the index of the first child of the {@code XMLTree} matching the
     *   given tag or -1 if not found]
     * </pre>
     */
    private static int getChildElement(XMLTree xml, String tag) {
        assert xml != null : "Violation of: xml is not null";
        assert tag != null : "Violation of: tag is not null";
        assert xml.isTag() : "Violation of: the label root of xml is a tag";

        int children = xml.numberOfChildren();
        int i = 0;
        int index = -1;
        String name = "";
        while (i < children && !(tag.equals(name))) {
            name = xml.child(i).label();
            if (tag.equals(name)) {
                index = i;
            }
            i++;
        }
        return index;
    }

    /**
     * check if rss feed is version 2.0
     *
     * @param xml
     * @return
     */
    public static boolean checkRSS(XMLTree xml) {
        boolean checkedRSS = false;
        String label = "";
        if (xml.isTag()) {
            label = xml.label();
            if (label.equals("RSS") || label.equals("rss")) {
                if (xml.hasAttribute("version")) {
                    if (xml.attributeValue("version").equals("2.0")) {
                        checkedRSS = true;
                    }
                }
            }
        }
        return checkedRSS;
    }

    /**
     * generates index html file
     *
     * @param xml
     * @param fileOut
     */
    private static void writeFileIndex(XMLTree xml, SimpleWriter fileOut) {
        String url = "";
        String file = "RSSfeed";
        String name = "";
        String title = "";
        SimpleWriter index = new SimpleWriter1L("data/index.html");
        XMLTree feed = xml.child(getChildElement(xml, "feed"));
        title = xml.attributeValue("title");
        //xml.display();
        /*
         * write first part of html index page
         */
        index.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>");
        index.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
        index.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
        index.println("<head>");
        index.println("<title>Top Sotries</title>");
        index.println("</head>");
        index.println("<body>");
        index.println("<h2>Top Stories</h2>");
        index.println("<ul>");
        /*
         * extract each feed (url) then pass information onto the html doc
         */
        for (int i = getChildElement(xml, "feed"); i < xml.numberOfChildren(); i++) {
            //file = file + i;
            if (getChildElement(xml, "feed") != -1) {
                url = "" + xml.child(i).attributeValue("url");
                name = "" + xml.child(i).attributeValue("name");
                file = "" + xml.child(i).attributeValue("file");
                processFeed(url, file, fileOut);
                index.println("<li><a href=\"" + file + "\">" + name
                        + "</a></li>");
            }
        }
        /*
         * write end of html file
         */
        index.println("</ul>");
        index.println("</body>");
        index.println("</html>");
        /*
         * close I/O
         */
        index.close();
    }

    /**
     * Processes one XML RSS (version 2.0) feed from a given URL converting it
     * into the corresponding HTML output file.
     *
     * @param url
     *            the URL of the RSS feed
     * @param file
     *            the name of the HTML output file
     * @param fileOut
     *            the output stream to report progress or errors
     * @updates out.content
     * @requires out.is_open
     * @ensures <pre>
     * [reads RSS feed from url, saves HTML document with table of news items
     *   to file, appends to out.content any needed messages]
     * </pre>
     */
    private static void processFeed(String url, String file,
            SimpleWriter fileOut) {
        SimpleWriter out = new SimpleWriter1L("data/" + file);
        String pubDate = "";
        String source = "";
        String news = "";
        String header = "";
        String headerLink = "";
        String headerDes = "";
        String sourceName = "";
        String newsLink = "";
        /*
         * assign xml trees for short cuts
         */
        XMLTree xml = new XMLTree1(url);
        //xml.display();
        XMLTree channel = xml.child(getChildElement(xml, "channel"));
        XMLTree item = channel.child(getChildElement(channel, "item"));
        /*
         * get header
         */
        header = "" + channel.child(getChildElement(channel, "title")).child(0);
        /*
         * get header link
         */
        headerLink = ""
                + channel.child(getChildElement(channel, "link")).child(0);
        /*
         * get header description
         */
        if (channel.child(getChildElement(channel, "description"))
                .numberOfChildren() > 0) {
            headerDes = ""
                    + channel.child(getChildElement(channel, "description"))
                    .child(0);
        }
        /*
         * creates RSSfeed.html and pass on header, header link and header
         * Description
         */
        writeFileTitle(out, xml, header, headerLink, headerDes); //write
        /*
         * pars through channel and look at all items available to pass to the
         * the html doc
         */
        for (int i = getChildElement(channel, "item"); i < channel
                .numberOfChildren(); i++) {
            /*
             * gather time stamp for each item in channel and pass it on to the
             * html doc
             */
            if (getChildElement(channel.child(i), "pubDate") != -1) {
                pubDate = ""
                        + channel.child(i)
                        .child(getChildElement(item, "pubDate"))
                        .child(0);
            } else {
                pubDate = "No date available";
            }
            /*
             * check if there is a source, if source is available pass onto the
             * html doc. If not pass no source
             */
            if (getChildElement(channel.child(i), "source") != -1) {
                source = ""
                        + channel
                                .child(i)
                                .child(getChildElement(channel.child(i),
                                        "source")).attributeValue("url");
                sourceName = ""
                        + channel
                                .child(i)
                                .child(getChildElement(channel.child(i),
                                        "source")).child(0);
            } else {
                source = "No source available";
            }
            /*
             * gather news and news link to pass onto the news block of the html
             * table
             */
            if (getChildElement(channel.child(i), "title") != -1) {
                news = ""
                        + channel.child(i)
                        .child(getChildElement(item, "title")).child(0);
                newsLink = ""
                        + channel.child(i).child(getChildElement(item, "link"))
                        .child(0);
            } else {
                news = "No news available";
            }
            /*
             * at the end of every loop add a row to the table in the html doc
             */
            writeFileTable(out, pubDate, source, news, newsLink, sourceName);
        }
        /*
         * add final touches to html doc
         */
        writeFileClose(out);
        out.close();
    }

    /**
     * writes the first part of the an html doc includes header and header
     * description
     *
     * @param fileOut
     * @param xml
     * @param header
     * @param headerLink
     * @param headerDes
     */
    public static void writeFileTitle(SimpleWriter fileOut, XMLTree xml,
            String header, String headerLink, String headerDes) {
        fileOut.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>");
        fileOut.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
        fileOut.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
        /*
         * write header
         */
        fileOut.println("<head>");
        fileOut.println("<title>" + header + ",</title>");
        fileOut.println("</head>");
        /*
         * write headline 1 with hyperlink
         */
        fileOut.println("<body>");
        fileOut.println("<h1><a href=\"" + headerLink + "\">" + header
                + "</a></h1>");
        /*
         * write description
         */
        fileOut.println("<p>" + headerDes + "</p>");
        /*
         * start table for second writeFileTable
         */
        fileOut.println("<table border=\"1\">");
        fileOut.println("<tr>");
        fileOut.println("<th>Date</th>");
        fileOut.println("<th>Source</th>");
        fileOut.println("<th>News</th>");
        fileOut.println("</tr>");
    }

    /**
     * Writes second part of the html doc includes the table of news articles,
     * sources and their time stamp
     *
     * @param fileOut
     * @param pubDate
     * @param source
     * @param news
     * @param newsLink
     * @param sourceName
     */
    public static void writeFileTable(SimpleWriter fileOut, String pubDate,
            String source, String news, String newsLink, String sourceName) {
        fileOut.println("<tr>");
        /*
         * write date
         */
        fileOut.println("<td>" + pubDate + "</td>");
        /*
         * write source
         */
        if (!source.equals("No source available")) {
            fileOut.println("<td>" + "<a href=\"" + source + "\">" + sourceName
                    + "</a></td>");
        } else {
            fileOut.println("<td>" + source + "</td>");
        }
        /*
         * write news
         */
        fileOut.println("<td>" + "<a href=\"" + newsLink + "\">" + news
                + "</a></td>");
        fileOut.println("</tr>");
    }

    /**
     * writes the last part of the html doc and closes includes closing marks
     *
     * @param fileOut
     */
    public static void writeFileClose(SimpleWriter fileOut) {
        fileOut.println("</table>");
        fileOut.println("</body>");
        fileOut.println("</html>");
    }

    /**
     * gets a valid url that is RSS 2.0 from the user
     *
     * @param in
     * @param out
     * @return
     */
    public static String getValidURL(SimpleReader in, SimpleWriter out) {
        String validURL = "";
        boolean okRSS = false;

        while (!okRSS) {
            out.print("Enter the URL of an RSS 2.0 news feed: ");
            validURL = in.nextLine();
            /*
             * Read XML input and initialize XMLTree. If input is not legal XML,
             * this statement will fail.
             */

            XMLTree xml = new XMLTree1(validURL);
            /*
             * Check for valid RSS feed version 2.0
             */
            okRSS = checkRSS(xml);
            if (!okRSS) {
                out.println("Not a valid RSS 2.0 feed!");
            }
        }
        return validURL;
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments; unused here
     */
    public static void main(String[] args) {
        /*
         * Open I/O streams and assign variables
         */
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();
        String userXMLin = "";
        /*
         * Input the source of an XML file that contains URLs for RSS 2.0 feeds.
         */
        out.print("Enter an XML file containing a list of URLs for RSS v2.0 (don't forget \".xml\"): ");
        userXMLin = in.nextLine();
        userXMLin = "data/" + userXMLin;
        //SimpleReader userIn = new SimpleReader1L(userXMLin);
        XMLTree xmlIn = new XMLTree1(userXMLin);
        writeFileIndex(xmlIn, out);
        /*
         * Close I/O streams.
         */
        in.close();
        out.close();
    }
}
