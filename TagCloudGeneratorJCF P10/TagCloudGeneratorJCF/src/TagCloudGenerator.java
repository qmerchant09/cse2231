import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;

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
            implements Comparator<Map.Entry<String, Integer>> {
        @Override
        public int compare(Map.Entry<String, Integer> o1,
                Map.Entry<String, Integer> o2) {
            int compVal;
            if (o1.getKey().equals(o2.getKey())
                    && o1.getValue().equals(o2.getValue())) {
                compVal = 0;
            } else {
                compVal = o1.getKey().compareTo(o2.getKey());
            }
            return compVal;
        }
    }

    /**
     * Compare {@code Integer}s in decreasing numeric order.
     */
    private static class IntegerLT
            implements Comparator<Map.Entry<String, Integer>> {
        @Override
        public int compare(Map.Entry<String, Integer> o1,
                Map.Entry<String, Integer> o2) {
            int compVal;
            if (o2.getValue().equals(o1.getValue())
                    && o2.getKey().equals(o1.getKey())) {
                compVal = 0;
            } else {
                compVal = o2.getValue().compareTo(o1.getValue());
            }
            return compVal;
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
    public static void processFile(BufferedReader in,
            Map<String, Integer> words) {
        //Loop through the input file and put each word into the map
        String str = "";
        while (str != null) {
            int position = 0;
            try {
                str = in.readLine();
            } catch (IOException e) {
                System.err.println("Error reading from the input file.");
            }
            if (str != null) {
                while (position < str.length()) {
                    String word = nextWordOrSeparator(str, position)
                            .toLowerCase();
                    if (SEPARATORS.indexOf(word.charAt(0)) < 0) {
                        if (words.containsKey(word)) {
                            Integer value = words.get(word);
                            words.replace(word, value + 1);
                        } else {
                            words.put(word, 1);
                        }
                    }
                    position += word.length();
                }
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
    private static void outputPage(PrintWriter out, int num,
            Map<String, Integer> words, String name) {
        assert num >= 0 : "Number of words cannot be less than 0";
        assert num <= words.size() : "Number of words cannot be exceed "
                + words.size();
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

        //Create comparators and ArrayLists for sorting
        Comparator<Entry<String, Integer>> intComp = new IntegerLT();
        Comparator<Entry<String, Integer>> strComp = new StringLT();
        List<Entry<String, Integer>> sortedInt = new ArrayList<>();
        List<Entry<String, Integer>> sortedStr = new ArrayList<>();

        //Sort the ArrayList in decreasing order
        for (Map.Entry<String, Integer> m : words.entrySet()) {
            sortedInt.add(m);
        }
        sortedInt.sort(intComp);

        //Add the top n words to the new ArrayList and sort alphabetically
        for (int i = 0; i < num; i++) {
            sortedStr.add(sortedInt.remove(0));
        }
        sortedStr.sort(strComp);

        //Find the biggest and smallest count in the top words
        int max = 0;
        int min = Integer.MAX_VALUE;
        for (Map.Entry<String, Integer> m : sortedStr) {
            if (m.getValue() > max) {
                max = m.getValue();
            }
            if (m.getValue() < min) {
                min = m.getValue();
            }
        }

        //Print the word and count to output file
        for (Map.Entry<String, Integer> m : sortedStr) {
            int value = m.getValue();
            int size = ((FONT_MAX - FONT_MIN) * (value - min) / (max - min))
                    + FONT_MIN;
            out.println("<span style=\"cursor:default\" class=\"f" + size
                    + "\" title=" + "\"count: " + value + "\">" + m.getKey()
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
        //Initialize the BufferedReader and PrintWriter
        BufferedReader input;
        PrintWriter output;

        //Get the name of the input file
        Scanner in = new Scanner(System.in);
        System.out.print("Enter the name of the input file: ");
        String strIn = in.nextLine();

        //Try to create the input file, catching errors
        try {
            input = new BufferedReader(new FileReader(strIn));
        } catch (IOException e) {
            System.err.println("Could not open the input file.");
            in.close();
            return;
        }

        //Process the input file into a map
        Map<String, Integer> words = new TreeMap<>();
        processFile(input, words);

        //Try to close the input file, catching errors
        try {
            input.close();
        } catch (IOException e) {
            System.err.println("Error closing the input file.");
        }

        //Get the name of the output file
        System.out.print("Enter the name of the output file: ");
        String strOut = in.nextLine();

        //Try to create the output file, catching errors
        try {
            output = new PrintWriter(
                    new BufferedWriter(new FileWriter(strOut)));
        } catch (IOException e) {
            System.err.println("Could not open the output file.");
            in.close();
            return;
        }

        //Get the number of words to be included in the tag cloud
        System.out.print(
                "Number of words to be included in the generated tag cloud "
                        + "(0 min, " + words.size() + " max" + "): ");
        int numOfWords = 0;
        try {
            numOfWords = in.nextInt();
        } catch (Exception e) {
            System.err.println("Could not parse as an int.");
        }

        //Creates the webpage using data from the input file
        outputPage(output, numOfWords, words, strIn);

        //Close input and output streams
        in.close();
        output.close();
    }

}
