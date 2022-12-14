import static org.junit.Assert.assertEquals;

import java.util.Comparator;

import org.junit.Test;

import components.sortingmachine.SortingMachine;

/**
 * JUnit test fixture for {@code SortingMachine<String>}'s constructor and
 * kernel methods.
 *
 * @author Roshan Varma & Quantez Merchant
 *
 */
public abstract class SortingMachineTest {

    /**
     * Invokes the appropriate {@code SortingMachine} constructor for the
     * implementation under test and returns the result.
     *
     * @param order
     *            the {@code Comparator} defining the order for {@code String}
     * @return the new {@code SortingMachine}
     * @requires IS_TOTAL_PREORDER([relation computed by order.compare method])
     * @ensures constructorTest = (true, order, {})
     */
    protected abstract SortingMachine<String> constructorTest(
            Comparator<String> order);

    /**
     * Invokes the appropriate {@code SortingMachine} constructor for the
     * reference implementation and returns the result.
     *
     * @param order
     *            the {@code Comparator} defining the order for {@code String}
     * @return the new {@code SortingMachine}
     * @requires IS_TOTAL_PREORDER([relation computed by order.compare method])
     * @ensures constructorRef = (true, order, {})
     */
    protected abstract SortingMachine<String> constructorRef(
            Comparator<String> order);

    /**
     *
     * Creates and returns a {@code SortingMachine<String>} of the
     * implementation under test type with the given entries and mode.
     *
     * @param order
     *            the {@code Comparator} defining the order for {@code String}
     * @param insertionMode
     *            flag indicating the machine mode
     * @param args
     *            the entries for the {@code SortingMachine}
     * @return the constructed {@code SortingMachine}
     * @requires IS_TOTAL_PREORDER([relation computed by order.compare method])
     * @ensures <pre>
     * createFromArgsTest = (insertionMode, order, [multiset of entries in args])
     * </pre>
     */
    private SortingMachine<String> createFromArgsTest(Comparator<String> order,
            boolean insertionMode, String... args) {
        SortingMachine<String> sm = this.constructorTest(order);
        for (int i = 0; i < args.length; i++) {
            sm.add(args[i]);
        }
        if (!insertionMode) {
            sm.changeToExtractionMode();
        }
        return sm;
    }

    /**
     *
     * Creates and returns a {@code SortingMachine<String>} of the reference
     * implementation type with the given entries and mode.
     *
     * @param order
     *            the {@code Comparator} defining the order for {@code String}
     * @param insertionMode
     *            flag indicating the machine mode
     * @param args
     *            the entries for the {@code SortingMachine}
     * @return the constructed {@code SortingMachine}
     * @requires IS_TOTAL_PREORDER([relation computed by order.compare method])
     * @ensures <pre>
     * createFromArgsRef = (insertionMode, order, [multiset of entries in args])
     * </pre>
     */
    private SortingMachine<String> createFromArgsRef(Comparator<String> order,
            boolean insertionMode, String... args) {
        SortingMachine<String> sm = this.constructorRef(order);
        for (int i = 0; i < args.length; i++) {
            sm.add(args[i]);
        }
        if (!insertionMode) {
            sm.changeToExtractionMode();
        }
        return sm;
    }

    /**
     * Comparator<String> implementation to be used in all test cases. Compare
     * {@code String}s in lexicographic order.
     */
    private static class StringLT implements Comparator<String> {

        @Override
        public int compare(String s1, String s2) {
            return s1.compareToIgnoreCase(s2);
        }

    }

    /**
     * Comparator instance to be used in all test cases.
     */
    private static final StringLT ORDER = new StringLT();

    /*
     * Test case for constructors
     */

    @Test
    public final void testConstructor() {
        /*
         * Set up variables and call method under test
         */
        SortingMachine<String> m = this.constructorTest(ORDER);
        SortingMachine<String> mExpected = this.constructorRef(ORDER);
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(mExpected, m);
    }

    @Test
    public final void testCreateFromArgs() {
        /*
         * Set up variables and call method under test
         */
        SortingMachine<String> m = this.createFromArgsRef(ORDER, true, "green",
                "blue");
        SortingMachine<String> mExpected = this.createFromArgsTest(ORDER, true,
                "green", "blue");
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(mExpected, m);
    }

    /*
     * Test cases for kernel methods
     */

    @Test
    public final void testAddEmpty() {
        /*
         * Set up variables
         */
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true);
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true,
                "green");
        /*
         * Call method under test
         */
        m.add("green");
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(mExpected, m);
    }

    @Test
    public final void testAddNotEmpty() {
        /*
         * Set up variables
         */
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true,
                "green");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true,
                "green", "blue");
        /*
         * Call method under test
         */
        m.add("blue");
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(mExpected, m);
    }

    @Test
    public final void testAddStartingWithDuplicates() {
        /*
         * Set up variables
         */
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true, "green",
                "green", "blue");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true,
                "green", "green", "blue", "red");
        /*
         * Call method under test
         */
        m.add("red");
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(mExpected, m);
    }

    @Test
    public final void testAddDuplicates() {
        /*
         * Set up variables
         */
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true, "green",
                "blue");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true,
                "green", "blue", "green");
        /*
         * Call method under test
         */
        m.add("green");
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(mExpected, m);
    }

    @Test
    public final void testChangeToExtractionModeEmpty() {
        /*
         * Set up variables
         */
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true);
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false);
        /*
         * Call method under test
         */
        m.changeToExtractionMode();
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(mExpected, m);
    }

    @Test
    public final void testChangeToExtractionMode() {
        /*
         * Set up variables
         */
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true, "green",
                "yellow", "blue");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false,
                "blue", "green", "yellow");
        /*
         * Call method under test
         */
        m.changeToExtractionMode();
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(mExpected, m);
    }

    @Test
    public final void testChangeToExtractionModeWithDuplicates() {
        /*
         * Set up variables
         */
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true, "green",
                "yellow", "blue", "blue");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false,
                "blue", "blue", "green", "yellow");
        /*
         * Call method under test
         */
        m.changeToExtractionMode();
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(mExpected, m);
    }

    @Test
    public final void testRemoveFirstLeavingEmpty() {
        /*
         * Set up variables
         */
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false,
                "green");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false);
        /*
         * Call method under test
         */
        String str = m.removeFirst();
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(str, "green");
        assertEquals(mExpected, m);
    }

    @Test
    public final void testRemoveFirstNotLeavingEmpty() {
        /*
         * Set up variables
         */
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false, "blue",
                "green", "yellow");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false,
                "green", "yellow");
        /*
         * Call method under test
         */
        String str = m.removeFirst();
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(str, "blue");
        assertEquals(mExpected, m);
    }

    @Test
    public final void testRemoveFirstWithDuplicates() {
        /*
         * Set up variables
         */
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false, "blue",
                "green", "green", "yellow");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false,
                "green", "green", "yellow");
        /*
         * Call method under test
         */
        String str = m.removeFirst();
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(str, "blue");
        assertEquals(mExpected, m);
    }

    @Test
    public final void testIsInInsertionModeTrue() {
        /*
         * Set up variables
         */
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true);
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true);
        /*
         * Call method under test
         */
        boolean b = m.isInInsertionMode();
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(b, true);
        assertEquals(mExpected, m);
    }

    @Test
    public final void testIsInInsertionModeTrueWithElements() {
        /*
         * Set up variables
         */
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true, "blue");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true,
                "blue");
        /*
         * Call method under test
         */
        boolean b = m.isInInsertionMode();
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(b, true);
        assertEquals(mExpected, m);
    }

    @Test
    public final void testIsInInsertionModeTrueWithDuplicates() {
        /*
         * Set up variables
         */
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true, "blue",
                "blue");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true,
                "blue", "blue");
        /*
         * Call method under test
         */
        boolean b = m.isInInsertionMode();
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(b, true);
        assertEquals(mExpected, m);
    }

    @Test
    public final void testIsInInsertionModeFalse() {
        /*
         * Set up variables
         */
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false);
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false);
        /*
         * Call method under test
         */
        boolean b = m.isInInsertionMode();
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(b, false);
        assertEquals(mExpected, m);
    }

    @Test
    public final void testOrderInsertionMode() {
        /*
         * Set up variables and call method under test
         */
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true);
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true);
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(m.order(), mExpected.order());
        assertEquals(mExpected, m);
    }

    @Test
    public final void testOrderInsertionModeWithElements() {
        /*
         * Set up variables and call method under test
         */
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true, "blue");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true,
                "blue");
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(m.order(), mExpected.order());
        assertEquals(mExpected, m);
    }

    @Test
    public final void testOrderInsertionModeWithDuplicates() {
        /*
         * Set up variables and call method under test
         */
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true, "blue",
                "blue");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true,
                "blue", "blue");
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(m.order(), mExpected.order());
        assertEquals(mExpected, m);
    }

    @Test
    public final void testOrderExtractionMode() {
        /*
         * Set up variables and call method under test
         */
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false);
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false);
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(m.order(), mExpected.order());
        assertEquals(mExpected, m);
    }

    @Test
    public final void testSizeEmptyInsertionMode() {
        /*
         * Set up variables
         */
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true);
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true);
        /*
         * Call method under test
         */
        int i = m.size();
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(i, mExpected.size());
        assertEquals(mExpected, m);
    }

    @Test
    public final void testSizeEmptyExtractionMode() {
        /*
         * Set up variables
         */
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false);
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false);
        /*
         * Call method under test
         */
        int i = m.size();
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(i, mExpected.size());
        assertEquals(mExpected, m);
    }

    @Test
    public final void testSizeNotEmptyInsertionMode() {
        /*
         * Set up variables
         */
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true, "blue",
                "green", "yellow");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true,
                "blue", "green", "yellow");
        /*
         * Call method under test
         */
        int i = m.size();
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(i, mExpected.size());
        assertEquals(mExpected, m);
    }

    @Test
    public final void testSizeNotEmptyExtractionMode() {
        /*
         * Set up variables
         */
        SortingMachine<String> m = this.createFromArgsTest(ORDER, false, "blue",
                "green", "yellow");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, false,
                "blue", "green", "yellow");
        /*
         * Call method under test
         */
        int i = m.size();
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(i, mExpected.size());
        assertEquals(mExpected, m);
    }

    @Test
    public final void testSizeDuplicatesInsertionMode() {
        /*
         * Set up variables
         */
        SortingMachine<String> m = this.createFromArgsTest(ORDER, true, "blue",
                "green", "yellow");
        SortingMachine<String> mExpected = this.createFromArgsRef(ORDER, true,
                "blue", "green", "yellow");
        /*
         * Call method under test
         */
        int i = m.size();
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(i, mExpected.size());
        assertEquals(mExpected, m);
    }
}
