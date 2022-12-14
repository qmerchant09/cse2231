import java.util.Comparator;

import components.map.Map;
import components.map.Map1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.sortingmachine.SortingMachine;
import components.sortingmachine.SortingMachine1L;

/**
 * Generates a tag cloud from a given input text file.
 *
 * @author Roshan Varma & Quantez Merchant
 *
 */
public final class TagCloudGenerator {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private TagCloudGenerator() {
    }

    /**
     * The max font size according to the css file.
     */
    private static final int FONT_MAX = 48;
    /**
     * The min font size according to the css file.
     */
    private static final int FONT_MIN = 11;

    /**
     * Definition of separators.
     */
    private static final String SEPARATORS = " \t\n\r,-.!?[]';:/()“”_{}<>&@#$%*";

    /**
     * Compare {@code String}s in alphabetical order, ignoring capitals.
     */
    private static class StringLT
            implements Comparator<Map.Pair<String, Integer>> {
        @Override
        public int compare(Map.Pair<String, Integer> o1,
                Map.Pair<String, Integer> o2) {
            return o1.key().compareTo(o2.key());
        }
    }

    /**
     * Compare {@code Integer}s in decreasing numeric order.
     */
    private static class IntegerLT
            implements Comparator<Map.Pair<String, Integer>> {
        @Override
        public int compare(Map.Pair<String, Integer> o1,
                Map.Pair<String, Integer> o2) {
            return o2.value().compareTo(o1.value());
        }
    }

    /**
     * Returns the first "word" (maximal length string of characters not in
     * {@code SEPARATORS}) or "separator string" (maximal length string of
     * characters in {@code SEPARATORS}) in the given {@code text} starting at
     * the given {@code position}.
     *
     * @param text
     *            the {@code String} from which to get the word or separator
     *            string
     * @param position
     *            the starting index
     * @return the first word or separator string found in {@code text} starting
     *         at index {@code position}
     * @requires 0 <= position < |text|
     * @ensures <pre>
     * nextWordOrSeparator =
     *   text[position, position + |nextWordOrSeparator|)  and
     * if entries(text[position, position + 1)) intersection entries(SEPARATORS) = {}
     * then
     *   entries(nextWordOrSeparator) intersection entries(SEPARATORS) = {}  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      intersection entries(SEPARATORS) /= {})
     * else
     *   entries(nextWordOrSeparator) is subset of entries(SEPARATORS)  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      is not subset of entries(SEPARATORS))
     * </pre>
     */
    private static String nextWordOrSeparator(String text, int position) {
        assert text != null : "Violation of: text is not null";
        assert 0 <= position : "Violation of: 0 <= position";
        assert position < text.length() : "Violation of: position < |text|";

        //Loop through the characters in text until a separator is found
        int i = position;
        boolean value = SEPARATORS.indexOf(text.charAt(i)) < 0;
        while (i < text.length()
                && value == SEPARATORS.indexOf(text.charAt(i)) < 0) {
            i++;
        }
        return text.substring(position, i);
    }

    /**
     * Reads through the input file and counts how many times every word
     * appears.
     *
     * @param in
     *            the input file
     * @param words
     *            map of words and counts from the input file
     *
     * @updates terms
     * @requires the input file is not empty
     * @ensures terms is filled with data from in
     */
    public static void processFile(SimpleReader in,
            Map<String, Integer> words) {

        //Loop through the input file and put each word into the map
        while (!in.atEOS()) {
            int position = 0;
            String str = in.nextLine();
            while (position < str.length()) {
                String word = nextWordOrSeparator(str, position).toLowerCase();
                if (SEPARATORS.indexOf(word.charAt(0)) < 0) {
                    if (words.hasKey(word)) {
                        Map.Pair<String, Integer> temp = words.remove(word);
                        words.add(temp.key(), temp.value() + 1);
                    } else {
                        words.add(word, 1);
                    }
                }
                position += word.length();
            }
        }
    }

    /**
     * Outputs the HTML code required to create the webpage.
     *
     * @param out
     *            the output file
     * @param num
     *            the number of words included in the tag cloud
     * @param words
     *            map of words and counts
     * @param name
     *            name of the input file
     *
     *
     * @requires the input file is not empty
     * @ensures the output file is filled with data from the map
     */
    private static void outputPage(SimpleWriter out, int num,
            Map<String, Integer> words, String name) {

        //Output HTML tags to the output file
        out.println("<html>");
        out.println("<head>");
        out.println("<title> Top " + num + " words in " + name + "</title>");
        out.println(
                "<link href= tagcloud.css rel=\"stylesheet\" type=\"text/css\"> ");
        out.println("</head>");
        out.println("<body>");
        out.println("<h2> Top " + num + " words in " + name + "</h2>");
        out.println("<hr>");
        out.println("<div class=\"cdiv\">");
        out.println("<p class=\"cbox\">");

        //Create comparators and sorting machines for sorting
        Comparator<Map.Pair<String, Integer>> intComp = new IntegerLT();
        SortingMachine<Map.Pair<String, Integer>> smInt = new SortingMachine1L<>(
                intComp);
        Comparator<Map.Pair<String, Integer>> strComp = new StringLT();
        SortingMachine<Map.Pair<String, Integer>> smStr = new SortingMachine1L<>(
                strComp);
        int max = 0;
        int min = Integer.MAX_VALUE;

        //Add the pairs to the sorting machine
        for (Map.Pair<String, Integer> m : words) {
            smInt.add(m);
        }
        smInt.changeToExtractionMode();

        //Sort in decreasing order, and add the top n words to the new sorting machine
        for (int i = 0; i < num; i++) {
            smStr.add(smInt.removeFirst());
        }
        smStr.changeToExtractionMode();

        //Find the biggest and smallest count in the top words
        for (Map.Pair<String, Integer> m : smStr) {
            if (m.value() > max) {
                max = m.value();
            }
            if (m.value() < min) {
                min = m.value();
            }
        }

        //Print the word and count to output file
        while (smStr.size() > 0) {
            Map.Pair<String, Integer> smMap = smStr.removeFirst();
            int count = smMap.value();
            int size = ((FONT_MAX - FONT_MIN) * (count - min) / (max - min))
                    + FONT_MIN;
            out.println("<span style=\"cursor:default\" class=\"f" + size
                    + "\" title=" + "\"count: " + count + "\">" + smMap.key()
                    + "</span>");
        }

        //Print closing tags
        out.println("</p>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");

    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();
        final int five = 5;

        //Ask the user for the input file and output file, and number of words
        out.print("Enter the name of the input file: ");
        String strIn = in.nextLine();
        SimpleReader inputFile = new SimpleReader1L(strIn);
        Map<String, Integer> words = new Map1L<>();
        processFile(inputFile, words);

        //Check if the input file is empty
        while (words.size() == 0) {
            out.print("Enter the name of a non empty input file: ");
            strIn = in.nextLine();
            inputFile = new SimpleReader1L(strIn);
            processFile(inputFile, words);
        }

        out.print("Enter the name of the output file: ");
        String strOut = in.nextLine();

        //Check if output file is an HTML file
        while (strOut.length() < five || (!strOut
                .substring(strOut.length() - five).equals(".html"))) {
            out.print("Please enter the name of a valid output file: ");
            strOut = in.nextLine();
        }
        SimpleWriter outputFile = new SimpleWriter1L(strOut);

        out.print(
                "Number of words to be included in the generated tag cloud: ");
        int numOfWords = in.nextInteger();

        //Check if number of words for tag cloud exceeds the size of the map
        while (numOfWords < 1 || numOfWords > words.size()) {
            out.print(
                    "Enter a number between 1" + " and " + words.size() + ": ");
            numOfWords = in.nextInteger();
        }

        //Creates the webpage using data from the input file
        outputPage(outputFile, numOfWords, words, strIn);

        //Close input and output streams
        in.close();
        out.close();
    }

}
