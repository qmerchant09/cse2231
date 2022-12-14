import components.map.Map;
import components.program.Program;
import components.program.Program1;
import components.queue.Queue;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.statement.Statement;
import components.utilities.Reporter;
import components.utilities.Tokenizer;

/**
 * Layered implementation of secondary method {@code parse} for {@code Program}.
 *
 * @author Roshan Varma & Quantez Merchant
 *
 */
public final class Program1Parse1 extends Program1 {

    /*
     * Private members --------------------------------------------------------
     */

    /**
     * Parses a single BL instruction from {@code tokens} returning the
     * instruction name as the value of the function and the body of the
     * instruction in {@code body}.
     *
     * @param tokens
     *            the input tokens
     * @param body
     *            the instruction body
     * @return the instruction name
     * @replaces body
     * @updates tokens
     * @requires <pre>
     * [<"INSTRUCTION"> is a prefix of tokens]  and
     *  [<Tokenizer.END_OF_INPUT> is a suffix of tokens]
     * </pre>
     * @ensures <pre>
     * if [an instruction string is a proper prefix of #tokens]  and
     *    [the beginning name of this instruction equals its ending name]  and
     *    [the name of this instruction does not equal the name of a primitive
     *     instruction in the BL language] then
     *  parseInstruction = [name of instruction at start of #tokens]  and
     *  body = [Statement corresponding to statement string of body of
     *          instruction at start of #tokens]  and
     *  #tokens = [instruction string at start of #tokens] * tokens
     * else
     *  [report an appropriate error message to the console and terminate client]
     * </pre>
     */
    private static String parseInstruction(Queue<String> tokens,
            Statement body) {
        assert tokens != null : "Violation of: tokens is not null";
        assert body != null : "Violation of: body is not null";
        assert tokens.length() > 0 && tokens.front().equals("INSTRUCTION") : ""
                + "Violation of: <\"INSTRUCTION\"> is proper prefix of tokens";
        //Dequeue the instruction keyword
        tokens.dequeue();
        //Make sure instruction name is valid identifier
        String instrName1 = tokens.dequeue();
        Reporter.assertElseFatalError(Tokenizer.isIdentifier(instrName1),
                "Invalid Identifier");
        Reporter.assertElseFatalError(!Tokenizer.isKeyword(instrName1),
                "Instruction name cannot be a keyword");
        Reporter.assertElseFatalError(!Tokenizer.isCondition(instrName1),
                "Instruction name cannot be a condition");
        //Check if the instruction has the same name as a primitive instruction
        Reporter.assertElseFatalError(
                !(instrName1.equals("move") || instrName1.equals("turnleft")
                        || instrName1.equals("turnright")
                        || instrName1.equals("infect")
                        || instrName1.equals("skip")),
                "Instruction name cant be a primitive instruction");
        //Make sure "IS" follows instruction name
        Reporter.assertElseFatalError(tokens.dequeue().equals("IS"),
                "Keyword IS not present");
        //Parse the block into the statement
        body.parseBlock(tokens);
        //Make sure "END" is correct
        Reporter.assertElseFatalError(tokens.dequeue().equals("END"),
                "keyword END not present");
        String instrName2 = tokens.dequeue();
        //Check if the beginning name matches the end name
        Reporter.assertElseFatalError(instrName1.equals(instrName2),
                "Identifier names do not match");
        //Return the instruction name
        return instrName1;
    }

    /*
     * Constructors -----------------------------------------------------------
     */

    /**
     * No-argument constructor.
     */
    public Program1Parse1() {
        super();
    }

    /*
     * Public methods ---------------------------------------------------------
     */

    @Override
    public void parse(SimpleReader in) {
        assert in != null : "Violation of: in is not null";
        assert in.isOpen() : "Violation of: in.is_open";
        Queue<String> tokens = Tokenizer.tokens(in);
        this.parse(tokens);
    }

    @Override
    public void parse(Queue<String> tokens) {
        assert tokens != null : "Violation of: tokens is not null";
        assert tokens.length() > 0 : ""
                + "Violation of: Tokenizer.END_OF_INPUT is a suffix of tokens";
        //Makes sure the file begins with PROGRAM
        Reporter.assertElseFatalError(tokens.dequeue().equals("PROGRAM"),
                "keyword PROGRAM not present");
        //Make sure program name is identifier
        String programName1 = tokens.dequeue();
        Reporter.assertElseFatalError(Tokenizer.isIdentifier(programName1),
                "Invalid Identifier");
        //Make sure "IS" follows program name
        Reporter.assertElseFatalError(tokens.dequeue().equals("IS"),
                "Invalid Keyword");
        //Create a map to store the instructions
        Map<String, Statement> pMap = this.newContext();
        //Loop through the queue until the front is begin
        while (!tokens.front().equals("BEGIN")) {
            //Parse body
            Statement body = this.newBody();
            String instr = parseInstruction(tokens, body);
            //Check if the map contains duplicates
            Reporter.assertElseFatalError(!pMap.hasKey(instr),
                    "Cannot have duplicate Identifiers");
            pMap.add(instr, body);
        }

        //Make sure "BEGIN" is correct
        Reporter.assertElseFatalError(tokens.dequeue().equals("BEGIN"),
                "keyword BEGIN not present");

        //Parse the program body into the statement
        Statement pBody = this.newBody();
        pBody.parseBlock(tokens);
        //Make sure "END" is correct
        Reporter.assertElseFatalError(tokens.dequeue().equals("END"),
                "keyword END not present");
        //Make sure name at end is same as beginning
        String programName2 = tokens.dequeue();
        Reporter.assertElseFatalError(programName1.equals(programName2),
                "Identifiers dont match");
        //Make sure at end of program
        Reporter.assertElseFatalError(
                tokens.front().equals(Tokenizer.END_OF_INPUT),
                "There is content still in tokens");
        //Add the program name, context, and body to this
        this.setName(programName1);
        this.swapContext(pMap);
        this.swapBody(pBody);
    }

    /*
     * Main test method -------------------------------------------------------
     */

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();
        /*
         * Get input file name
         */
        out.print("Enter valid BL program file name: ");
        String fileName = in.nextLine();
        /*
         * Parse input file
         */
        out.println("*** Parsing input file ***");
        Program p = new Program1Parse1();
        SimpleReader file = new SimpleReader1L(fileName);
        Queue<String> tokens = Tokenizer.tokens(file);
        file.close();
        p.parse(tokens);
        /*
         * Pretty print the program
         */
        out.println("*** Pretty print of parsed program ***");
        p.prettyPrint(out);

        in.close();
        out.close();
    }

}
