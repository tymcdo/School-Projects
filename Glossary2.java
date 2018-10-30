import java.util.Comparator;

import components.queue.Queue;
import components.queue.Queue1L;
import components.set.Set;
import components.set.Set1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * This program inputs an XML file with RSS feeds (version 2.0) and outputs an
 * index of RSS html files created from URLs from the XML input.
 *
 * @author Tyler McDowell
 *
 */
public final class Glossary2 {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private Glossary2() {
    }

    /**
     * Generates the set of characters in the given {@code String} into the
     * given {@code Set}.
     *
     * @param str
     *            the given {@code String}
     * @param strSet
     *            the {@code Set} to be replaced
     * @replaces strSet
     * @ensures strSet = entries(str)
     */
    public static void generateElements(String str, Set<Character> strSet) {
        assert str != null : "Violation of: str is not null";
        assert strSet != null : "Violation of: strSet is not null";

        char index = ' ';
        strSet.clear();
        for (int i = 0; i < str.length() - 1; i++) {
            index = str.charAt(i);
            strSet.add(index);
        }
    }

    /**
     * indexs the first term for later use
     *
     * @param line
     * @param terms
     *
     * @ensures all 1 word terms are in a set terms
     */
    public static void dealWithLine(String line, Set<String> terms,
            Set<Character> separator) {
        int lineLength = line.length() - 1;
        int count = 0;
        boolean done = false;
        boolean noContain = false;
        /*
         * check string for empty space, if no space (aka is a term) add it to
         * the set
         */
        while (!done && count < lineLength) {
            if (!line.isEmpty()) {
                if (separator.contains(line.charAt(count))) {
                    noContain = true;
                    done = true;
                }
            }
            count++;
        }
        if (line.isEmpty()) {
            noContain = true;
        }

        if (!noContain) {
            terms.add(line);
        }
    }

    /**
     * Finds the info that goes along with the giving word (termIn)
     *
     * @param fileName
     * @param termIn
     * @return info for termIn
     *
     */
    public static String matchSet(String fileName, String termIn) {
        StringBuilder infoBuilder = new StringBuilder();
        String info = "";
        SimpleReader file = new SimpleReader1L(fileName);
        while (!file.atEOS()) {
            String line = file.nextLine();
            if (line.compareTo(termIn) == 0) {
                while (!line.isEmpty()) {
                    line = (file.nextLine());
                    infoBuilder.append(line);
                    if (file.atEOS()) {
                        line = "";
                    }
                }
            }
        }
        file.close();
        info = infoBuilder.toString();
        return info;
    }

    /**
     * generates an html doc for a giving term and meaning (info)
     *
     * @param term
     * @param info
     *
     * @ensures html doc is mad for term
     */
    public static void generateTerm(String term, String info) {
        SimpleWriter out = new SimpleWriter1L("data/" + term + ".html");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>" + term + "</title");
        out.println("</head>");
        out.println("<body>");
        out.println("<h2><b><i><font color=\"red\">" + term
                + "</font></i></b></h2>");
        out.println("<blockquote>" + info + "</blockquote>");
        out.println("<hr />");
        out.println("<p>Return to <a href=\"index.html\">index</a>.</p>");
        out.println("</body>");
        out.println("</html>");
        out.close();
    }

    /**
     * creates an html doc for indexing
     *
     * @param alphabetized
     *
     * @ensures index.html is created
     */
    public static void generateIndex(Queue<String> alphabetized) {
        SimpleWriter out = new SimpleWriter1L("data/index.html");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Glossary</title");
        out.println("</head>");
        out.println("<body>");
        out.println("<h2>Glossary</h2>");
        out.println("<hr />");
        out.println("<h3>Index</h3>");
        out.println("<ul>");
        int alpha = alphabetized.length();
        for (int i = 0; i < alpha; i++) {
            String term = alphabetized.dequeue();
            out.println("<li><a href=\"" + term + ".html\">" + term
                    + "</a></li>");
        }
        out.println("</ul>");
        out.println("</body>");
        out.println("</html>");
        out.close();
    }

    /**
     *
     * compare {@code Strings}s in lexicographic order
     *
     */
    private static class StringLT implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            return o1.compareTo(o2);
        }
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        /*
         * Define separator characters for test
         */
        final String separatorStr = " \t, ";
        Set<Character> separatorSet = new Set1L<>();
        generateElements(separatorStr, separatorSet);
        /*
         * Open I/O streams and assign variables
         */
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();
        String userTextFile = "";
        String line = "";
        Set<String> terms = new Set1L<String>();
        /*
         * Input the source of an XML file that contains URLs for RSS 2.0 feeds.
         */
        out.print("Enter a file to generate a glossery: ...data/");
        userTextFile = in.nextLine();
        userTextFile = "data/" + userTextFile;
        SimpleReader readFile = new SimpleReader1L(userTextFile);
        /*
         * get next line and generate set of terms
         */
        while (!readFile.atEOS()) {
            line = readFile.nextLine();
            dealWithLine(line, terms, separatorSet);
        }
        /*
         * alphabatise set of terms and generate html index doc
         */
        Queue<String> alphabetized = new Queue1L<>();
        Comparator<String> cs = new StringLT();
        for (String ele : terms) {
            alphabetized.enqueue(ele);
        }
        alphabetized.sort(cs);
        //out.print(alphabetized);//test
        /*
         * match set to next line, retore alphabetized
         */
        int alpha = alphabetized.length();
        for (int i = 0; i < alpha; i++) {
            String termRemoved = alphabetized.dequeue();
            String termInfo = matchSet(userTextFile, termRemoved);
            /*
             * generate html doc for term
             */
            generateTerm(termRemoved, termInfo);
            /*
             * retore alphabetized
             */
            alphabetized.enqueue(termRemoved);
        }
        /*
         * generate index html doc
         */
        generateIndex(alphabetized);
        /*
         * Close I/O streams.
         */
        readFile.close();
        in.close();
        out.close();
    }
}
