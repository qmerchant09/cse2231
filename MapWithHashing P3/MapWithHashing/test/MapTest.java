import static org.junit.Assert.assertEquals;

import org.junit.Test;

import components.map.Map;
import components.map.Map.Pair;

/**
 * JUnit test fixture for {@code Map<String, String>}'s constructor and kernel
 * methods.
 *
 * @author Roshan Varma & Quantez Merchant
 *
 */
public abstract class MapTest {

    /**
     * Invokes the appropriate {@code Map} constructor for the implementation
     * under test and returns the result.
     *
     * @return the new map
     * @ensures constructorTest = {}
     */
    protected abstract Map<String, String> constructorTest();

    /**
     * Invokes the appropriate {@code Map} constructor for the reference
     * implementation and returns the result.
     *
     * @return the new map
     * @ensures constructorRef = {}
     */
    protected abstract Map<String, String> constructorRef();

    /**
     *
     * Creates and returns a {@code Map<String, String>} of the implementation
     * under test type with the given entries.
     *
     * @param args
     *            the (key, value) pairs for the map
     * @return the constructed map
     * @requires <pre>
     * [args.length is even]  and
     * [the 'key' entries in args are unique]
     * </pre>
     * @ensures createFromArgsTest = [pairs in args]
     */
    private Map<String, String> createFromArgsTest(String... args) {
        assert args.length % 2 == 0 : "Violation of: args.length is even";
        Map<String, String> map = this.constructorTest();
        for (int i = 0; i < args.length; i += 2) {
            assert !map.hasKey(args[i]) : ""
                    + "Violation of: the 'key' entries in args are unique";
            map.add(args[i], args[i + 1]);
        }
        return map;
    }

    /**
     *
     * Creates and returns a {@code Map<String, String>} of the reference
     * implementation type with the given entries.
     *
     * @param args
     *            the (key, value) pairs for the map
     * @return the constructed map
     * @requires <pre>
     * [args.length is even]  and
     * [the 'key' entries in args are unique]
     * </pre>
     * @ensures createFromArgsRef = [pairs in args]
     */
    private Map<String, String> createFromArgsRef(String... args) {
        assert args.length % 2 == 0 : "Violation of: args.length is even";
        Map<String, String> map = this.constructorRef();
        for (int i = 0; i < args.length; i += 2) {
            assert !map.hasKey(args[i]) : ""
                    + "Violation of: the 'key' entries in args are unique";
            map.add(args[i], args[i + 1]);
        }
        return map;
    }

    // We intended to test a max int value for table size but it caused a memory error, and is not included

    /*
     * Test cases for constructors
     */

    @Test
    public final void testNoArgumentConstructor() {
        /*
         * Set up variables and call method under test
         */
        Map<String, String> m = this.createFromArgsTest();
        Map<String, String> mExpected = this.createFromArgsRef();
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(mExpected, m);
    }

    /*
     * Test cases for kernel methods
     */

    @Test
    public final void testAddToEmpty() {
        /*
         * Set up variables and call method under test
         */
        Map<String, String> m = this.createFromArgsTest();
        Map<String, String> mExpected = this.createFromArgsRef("AA", "BB");

        m.add("AA", "BB");
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(mExpected, m);
    }

    @Test
    public final void testAddToNonEmpty() {
        /*
         * Set up variables
         */
        Map<String, String> m = this.createFromArgsTest("AA", "BB");
        Map<String, String> mExpected = this.createFromArgsRef("AA", "BB", "CC",
                "DD");
        /*
         * Call method under test
         */
        m.add("CC", "DD");
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(mExpected, m);
    }

    @Test
    public final void testRemoveLeavingEmpty() {
        /*
         * Set up variables
         */

        Map<String, String> m = this.createFromArgsTest("AA", "BB");
        Map<String, String> mExpected = this.createFromArgsRef();
        /*
         * Call method under test
         */
        Pair<String, String> x = m.remove("AA");
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(mExpected, m);
        assertEquals("AA", x.key());
        assertEquals("BB", x.value());
    }

    @Test
    public final void testRemoveLeavingNotEmpty() {
        /*
         * Set up variables
         */
        Map<String, String> m = this.createFromArgsTest("AA", "BB", "CC", "DD",
                "EE", "FF");
        Map<String, String> mExpected = this.createFromArgsRef("AA", "BB", "EE",
                "FF");
        /*
         * Call method under test
         */
        Pair<String, String> x = m.remove("CC");
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(mExpected, m);
        assertEquals("CC", x.key());
        assertEquals("DD", x.value());
    }

    @Test
    public final void testRemoveAnyLeavingEmpty() {
        /*
         * Set up variables
         */
        Map<String, String> m = this.createFromArgsTest("AA", "BB");
        Map<String, String> mExpected = this.createFromArgsRef("AA", "BB");
        /*
         * Call method under test
         */
        Pair<String, String> x = m.removeAny();

        /*
         * Assert that values of variables match expectations
         */
        assertEquals(mExpected.hasKey(x.key()), true);
        mExpected.remove(x.key());
        assertEquals(mExpected, m);
    }

    @Test
    public final void testRemoveAnyLeavingNotEmpty() {
        /*
         * Set up variables
         */
        Map<String, String> m = this.createFromArgsTest("AA", "BB", "CC", "DD",
                "EE", "FF");
        Map<String, String> mExpected = this.createFromArgsRef("AA", "BB", "CC",
                "DD", "EE", "FF");
        /*
         * Call method under test
         */
        Pair<String, String> x = m.removeAny();

        /*
         * Assert that values of variables match expectations
         */
        assertEquals(mExpected.hasKey(x.key()), true);
        mExpected.remove(x.key());
        assertEquals(mExpected, m);
    }

    @Test
    public final void testValue() {
        /*
         * Set up variables
         */
        Map<String, String> m = this.createFromArgsTest("AA", "BB", "CC", "DD",
                "EE", "FF");
        Map<String, String> mExpected = this.createFromArgsRef("AA", "BB", "CC",
                "DD", "EE", "FF");
        /*
         * Call method under test
         */
        String str = m.value("CC");

        /*
         * Assert that values of variables match expectations
         */
        assertEquals(str, mExpected.value("CC"));
        assertEquals(mExpected, m);
    }

    @Test
    public final void testHasKeyTrue() {
        /*
         * Set up variables
         */
        Map<String, String> m = this.createFromArgsTest("AA", "BB", "CC", "DD",
                "EE", "FF");
        Map<String, String> mExpected = this.createFromArgsRef("AA", "BB", "CC",
                "DD", "EE", "FF");
        /*
         * Call method under test
         */
        boolean b = m.hasKey("EE");

        /*
         * Assert that values of variables match expectations
         */
        assertEquals(b, mExpected.hasKey("EE"));
        assertEquals(mExpected, m);
    }

    @Test
    public final void testHasKeyFalse() {
        /*
         * Set up variables
         */
        Map<String, String> m = this.createFromArgsTest("AA", "BB", "CC", "DD",
                "EE", "FF");
        Map<String, String> mExpected = this.createFromArgsRef("AA", "BB", "CC",
                "DD", "EE", "FF");
        /*
         * Call method under test
         */
        boolean b = m.hasKey("GG");

        /*
         * Assert that values of variables match expectations
         */
        assertEquals(b, mExpected.hasKey("GG"));
        assertEquals(mExpected, m);
    }

    @Test
    public final void testHasKeyFalseEmpty() {
        /*
         * Set up variables
         */
        Map<String, String> m = this.createFromArgsTest();
        Map<String, String> mExpected = this.createFromArgsRef();
        /*
         * Call method under test
         */
        boolean b = m.hasKey("AA");

        /*
         * Assert that values of variables match expectations
         */
        assertEquals(b, mExpected.hasKey("AA"));
        assertEquals(mExpected, m);
    }

    @Test
    public final void testSizeZero() {
        /*
         * Set up variables
         */
        Map<String, String> m = this.createFromArgsTest();
        Map<String, String> mExpected = this.createFromArgsRef();
        /*
         * Call method under test
         */
        int i = m.size();

        /*
         * Assert that values of variables match expectations
         */
        assertEquals(i, mExpected.size());
    }

    @Test
    public final void testSizeNotZero() {
        /*
         * Set up variables
         */
        Map<String, String> m = this.createFromArgsTest("AA", "BB", "CC", "DD",
                "EE", "FF");
        Map<String, String> mExpected = this.createFromArgsRef("AA", "BB", "CC",
                "DD", "EE", "FF");
        /*
         * Call method under test
         */
        int i = m.size();

        /*
         * Assert that values of variables match expectations
         */
        assertEquals(i, mExpected.size());
    }

}
