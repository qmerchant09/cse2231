import components.map.Map;
import components.map.Map1L;
import components.queue.Queue;
import components.queue.Queue1L;
import components.set.Set;
import components.set.Set1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * Takes a text file from the user filled with words and creates a webpage with
 * a table filed with each unique word, and how many times that word appears in
 * the text file.
 *
 * @author Quantez Merchant
 *
 */
public final class WordCounter {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private WordCounter() {
    }

    /**
     * Creates an array of strings that takes the keys from term, and lists them
     * alphabetically.
     *
     * @param term
     *            map of words with count values
     *
     * @return An array of string
     * @requires term.length > 0
     * @ensures the array is filled with words from the map, and the words are
     *          alphabetized
     */
    private static String[] sortAplhabetically(Map<String, Integer> term) {
        //Initialize the array and counter
        String[] termArr = new String[term.size()];
        int i = 0;

        //Loop through each pair in the map and put each key into the array
        for (Map.Pair<String, Integer> k : term) {
            termArr[i] = k.key();
            i++;
        }

        //Loop through the array and alphabetize it
        for (i = 0; i < termArr.length; i++) {
            for (int j = i + 1; j < termArr.length; j++) {
                if (termArr[i].compareToIgnoreCase(termArr[j]) > 0) {
                    String str = termArr[i];
                    termArr[i] = termArr[j];
                    termArr[j] = str;
                }
            }
        }
        return termArr;
    }

    /**
     * Counts how many times a word appears in the map.
     *
     *
     * @param words
     *            A queue containing every word from the input file
     * @return A map containing words and the number of times it appears in the
     *         queue
     * @requires the queue is not empty
     * @ensures the map is filled with data from the queue
     */
    private static Map<String, Integer> count(Queue<String> words) {
        //Create the map and initial count value
        Map<String, Integer> wordsCounts = new Map1L<>();
        int count = 1;

        //Loops through the queue, updating the count if a duplicate word is found
        for (int j = 0; j < words.length(); j++) {
            String str = words.front().toLowerCase();
            if (wordsCounts.hasKey(str)) {
                wordsCounts.replaceValue(str, wordsCounts.value(str) + 1);
            } else {
                wordsCounts.add(str, count);
            }
            words.rotate(1);
        }
        return wordsCounts;
    }

    /**
     * Returns the first "word" (maximal length string of characters not in
     * {@code separators}) or "separator string" (maximal length string of
     * characters in {@code separators}) in the given {@code text} starting at
     * the given {@code position}.
     *
     * @param text
     *            the {@code String} from which to get the word or separator
     *            string
     * @param position
     *            the starting index
     * @param separators
     *            the {@code Set} of separator characters
     * @return the first word or separator string found in {@code text} starting
     *         at index {@code position}
     * @requires 0 <= position < |text|
     * @ensures <pre>
     * nextWordOrSeparator =
     *   text[position, position + |nextWordOrSeparator|)  and
     * if entries(text[position, position + 1)) intersection separators = {}
     * then
     *   entries(nextWordOrSeparator) intersection separators = {}  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      intersection separators /= {})
     * else
     *   entries(nextWordOrSeparator) is subset of separators  and
     *   (position + |nextWordOrSeparator| = |text|  or
     *    entries(text[position, position + |nextWordOrSeparator| + 1))
     *      is not subset of separators)
     * </pre>
     */
    private static String nextWordOrSeparator(String text, int position,
            Set<Character> separators) {
        assert text != null : "Violation of: text is not null";
        assert separators != null : "Violation of: separators is not null";
        assert 0 <= position : "Violation of: 0 <= position";
        assert position < text.length() : "Violation of: position < |text|";

        //Convert the String text into a char array, and create a String for the new word
        String str = "";
        char[] cArr = text.substring(position, text.length()).toCharArray();

        //Loop through the char array and put each non separator character into the string
        for (int i = 0; i < cArr.length; i++) {
            char c = cArr[i];
            if (!separators.contains(c)) {
                str = str + c;
            } else if (separators.contains(c) && str.isEmpty()) {
                str = Character.toString(c);
                i = cArr.length;
            } else {
                i = cArr.length;
            }
        }

        return str;
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
    private static void generateElements(String str, Set<Character> strSet) {
        assert str != null : "Violation of: str is not null";
        assert strSet != null : "Violation of: strSet is not null";

        //Loop through the given string and add characters to the set
        for (int i = 0; i < str.length(); i++) {
            strSet.add(str.charAt(i));
        }
    }

    /**
     * Reads through the input file and puts each word into a queue.
     *
     * @param in
     *            the input file
     * @param words
     *            queue of words from the input file
     *
     * @updates terms
     * @requires the input file is not empty
     * @ensures terms is filled with data from in
     */
    public static void processFile(SimpleReader in, Queue<String> words) {
        //Create a Set filled with separators
        final String separators = ".,?!-=+[]{}()<>:;#@$%^&*/\" ";
        Set<Character> separatorSet = new Set1L<>();
        generateElements(separators, separatorSet);

        //Loop through the input file and put each word into a queue
        String str = "";
        String word = "";
        while (!in.atEOS()) {
            int position = 0;
            str = in.nextLine();
            while (position < str.length()) {
                word = nextWordOrSeparator(str, position, separatorSet);
                if (!separatorSet.contains(word.charAt(0))) {
                    words.enqueue(word);
                }
                position += word.length();

            }
        }
    }

    /**
     * Outputs the HTML code required to create the webpage.
     *
     * @param in
     *            the input file
     * @param out
     *            the output file
     *
     *
     * @requires the input file is not empty
     * @ensures the output file is filled with data from the input file
     */
    private static void outputPage(SimpleReader in, SimpleWriter out) {

        //Output HTML tags to the output file
        out.println("<html>");
        out.println("<head>");
        out.println("<title> Words Counted in " + in.name() + "</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h2> Words Counted in " + in.name() + "</h2>");
        out.println("<hr>");
        out.println("<table border = 1>");
        out.println("<tbody>");
        out.println("<tr>");
        out.println("<th>" + "Words" + "</th>");
        out.println("<th>" + "Counts" + "</th>");
        out.println("</tr>");

        //Fills a queue with all the words from the input file
        Queue<String> words = new Queue1L<>();
        processFile(in, words);

        //Put the words into a map and count how many times each word appears
        Map<String, Integer> wordsCounts = count(words);
        //wordsCounts = count(words);

        //Creates a String array that is sorted alphabetically
        String[] termArr = sortAplhabetically(wordsCounts);

        //Outputs the terms to the output file
        for (int i = 0; i < termArr.length; i++) {
            out.println("<tr>");
            out.println("<td>" + termArr[i] + "</td>");
            out.println("<td>" + wordsCounts.value(termArr[i]) + "</td>");
            out.println("</tr>");

        }

        //Close the remaining tags and the output file
        out.println("</tbody>");
        out.println("</table>");
        out.println("</body>");
        out.println("</html>");
        out.close();

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

        //Ask the user for the input file and output file
        out.print("Enter the name of the input file: ");
        String strIn = in.nextLine();
        SimpleReader inputFile = new SimpleReader1L(strIn);
        out.print("Enter the name of the output file: ");
        String strOut = in.nextLine();
        SimpleWriter outputFile = new SimpleWriter1L(strOut);

        //Creates the webpage using data from the input file
        outputPage(inputFile, outputFile);

        //Close input and output streams
        in.close();
        out.close();
    }

}
