import static org.junit.Assert.assertEquals;

import org.junit.Test;

import components.set.Set;

/**
 * JUnit test fixture for {@code Set<String>}'s constructor and kernel methods.
 *
 * @author Roshan Varma and Quantez Merchant
 *
 */
public abstract class SetTest {

    /**
     * Invokes the appropriate {@code Set} constructor for the implementation
     * under test and returns the result.
     *
     * @return the new set
     * @ensures constructorTest = {}
     */
    protected abstract Set<String> constructorTest();

    /**
     * Invokes the appropriate {@code Set} constructor for the reference
     * implementation and returns the result.
     *
     * @return the new set
     * @ensures constructorRef = {}
     */
    protected abstract Set<String> constructorRef();

    /**
     * Creates and returns a {@code Set<String>} of the implementation under
     * test type with the given entries.
     *
     * @param args
     *            the entries for the set
     * @return the constructed set
     * @requires [every entry in args is unique]
     * @ensures createFromArgsTest = [entries in args]
     */
    private Set<String> createFromArgsTest(String... args) {
        Set<String> set = this.constructorTest();
        for (String s : args) {
            assert !set.contains(
                    s) : "Violation of: every entry in args is unique";
            set.add(s);
        }
        return set;
    }

    /**
     * Creates and returns a {@code Set<String>} of the reference implementation
     * type with the given entries.
     *
     * @param args
     *            the entries for the set
     * @return the constructed set
     * @requires [every entry in args is unique]
     * @ensures createFromArgsRef = [entries in args]
     */
    private Set<String> createFromArgsRef(String... args) {
        Set<String> set = this.constructorRef();
        for (String s : args) {
            assert !set.contains(
                    s) : "Violation of: every entry in args is unique";
            set.add(s);
        }
        return set;
    }

    /*
     * Test case for constructor
     */

    @Test
    public final void testNoArgumentConstructor() {
        /*
         * Set up variables and call method under test
         */
        Set<String> st = this.constructorTest();
        Set<String> stExpected = this.constructorRef();
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(stExpected, st);
    }

    @Test
    public final void testConstructorWithArguments() {
        /*
         * Set up variables and call method under test
         */
        Set<String> st = this.createFromArgsTest("banana", "apple", "orange");
        Set<String> stExpected = this.createFromArgsRef("banana", "apple",
                "orange");
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(stExpected, st);
    }

    /*
     * Test cases for kernel methods
     */

    @Test
    public final void testAddEmpty() {
        /*
         * Set up variables
         */
        Set<String> st = this.createFromArgsTest();
        Set<String> stExpected = this.createFromArgsRef("apple");
        /*
         * Call method under test
         */
        st.add("apple");
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(stExpected, st);
    }

    @Test
    public final void testAddLeftEmpty() {
        /*
         * Set up variables
         */
        Set<String> st = this.createFromArgsTest("banana", "orange");
        Set<String> stExpected = this.createFromArgsRef("banana", "apple",
                "orange");
        /*
         * Call method under test
         */
        st.add("apple");
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(stExpected, st);
    }

    @Test
    public final void testAddRightEmpty() {
        /*
         * Set up variables
         */
        Set<String> st = this.createFromArgsTest("banana", "apple");
        Set<String> stExpected = this.createFromArgsTest("banana", "apple",
                "orange");
        /*
         * Call method under test
         */
        st.add("orange");
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(stExpected, st);
    }

    @Test
    public final void testRemoveRootLeavingEmpty() {
        /*
         * Set up variables
         */
        Set<String> st = this.createFromArgsTest("apple");
        Set<String> stExpected = this.createFromArgsRef();
        /*
         * Call method under test
         */
        String x = st.remove("apple");
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(stExpected, st);
        assertEquals("apple", x);
    }

    @Test
    public final void testRemoveRootRightEmpty() {
        /*
         * Set up variables
         */
        Set<String> st = this.createFromArgsTest("banana", "apple");
        Set<String> stExpected = this.createFromArgsRef("apple");
        /*
         * Call method under test
         */
        String x = st.remove("banana");
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(stExpected, st);
        assertEquals("banana", x);
    }

    @Test
    public final void testRemoveRootLeftEmpty() {
        /*
         * Set up variables
         */
        Set<String> st = this.createFromArgsTest("banana", "orange");
        Set<String> stExpected = this.createFromArgsRef("orange");
        /*
         * Call method under test
         */
        String x = st.remove("banana");
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(stExpected, st);
        assertEquals("banana", x);
    }

    @Test
    public final void testRemoveLeft() {
        /*
         * Set up variables
         */
        Set<String> st = this.createFromArgsTest("banana", "apple");
        Set<String> stExpected = this.createFromArgsRef("banana");
        /*
         * Call method under test
         */
        String x = st.remove("apple");
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(stExpected, st);
        assertEquals("apple", x);
    }

    @Test
    public final void testRemoveRight() {
        /*
         * Set up variables
         */
        Set<String> st = this.createFromArgsTest("banana", "orange");
        Set<String> stExpected = this.createFromArgsRef("banana");
        /*
         * Call method under test
         */
        String x = st.remove("orange");
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(stExpected, st);
        assertEquals("orange", x);
    }

    @Test
    public final void testRemoveAnyLeavingEmpty() {
        /*
         * Set up variables
         */
        Set<String> st = this.createFromArgsTest("apple");
        Set<String> stExpected = this.createFromArgsRef();
        /*
         * Call method under test
         */
        String x = st.removeAny();
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(stExpected, st);
        assertEquals("apple", x);
    }

    @Test
    public final void testRemoveAnyLeftNotEmpty() {
        /*
         * Set up variables
         */
        Set<String> st = this.createFromArgsTest("apple", "banana");
        Set<String> stExpected = this.createFromArgsRef("apple", "banana");
        /*
         * Call method under test
         */
        String x = st.removeAny();
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(stExpected.contains(x), true);
        String y = stExpected.remove(x);
        assertEquals(y, x);
    }

    @Test
    public final void testRemoveAnyLeftEmpty() {
        /*
         * Set up variables
         */
        Set<String> st = this.createFromArgsTest("apple", "orange");
        Set<String> stExpected = this.createFromArgsRef("apple", "orange");
        /*
         * Call method under test
         */
        String x = st.removeAny();
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(stExpected.contains(x), true);
        String y = stExpected.remove(x);
        assertEquals(y, x);
    }

    @Test
    public final void testContainsEmpty() {
        /*
         * Set up variables
         */
        Set<String> st = this.createFromArgsTest("");
        Set<String> stExpected = this.createFromArgsRef("");
        /*
         * Call method under test
         */
        boolean b = st.contains("");
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(stExpected, st);
        assertEquals(true, b);
    }

    @Test
    public final void testContainsInRoot() {
        /*
         * Set up variables
         */
        Set<String> st = this.createFromArgsTest("apple");
        Set<String> stExpected = this.createFromArgsRef("apple");
        /*
         * Call method under test
         */
        boolean b = st.contains("apple");
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(stExpected, st);
        assertEquals(true, b);
    }

    @Test
    public final void testContainsInLeft() {
        /*
         * Set up variables
         */
        Set<String> st = this.createFromArgsTest("banana", "apple");
        Set<String> stExpected = this.createFromArgsRef("banana", "apple");
        /*
         * Call method under test
         */
        boolean b = st.contains("apple");
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(stExpected, st);
        assertEquals(true, b);
    }

    @Test
    public final void testContainsInRight() {
        /*
         * Set up variables
         */
        Set<String> st = this.createFromArgsTest("banana", "orange");
        Set<String> stExpected = this.createFromArgsRef("banana", "orange");
        /*
         * Call method under test
         */
        boolean b = st.contains("orange");
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(stExpected, st);
        assertEquals(true, b);
    }

    @Test
    public final void testContainsFalse() {
        /*
         * Set up variables
         */
        Set<String> st = this.createFromArgsTest("banana", "orange");
        Set<String> stExpected = this.createFromArgsRef("banana", "orange");
        /*
         * Call method under test
         */
        boolean b = st.contains("apple");
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(stExpected, st);
        assertEquals(false, b);
    }

    @Test
    public final void testSizeEmpty() {
        /*
         * Set up variables
         */
        Set<String> st = this.createFromArgsTest();
        Set<String> stExpected = this.createFromArgsRef();
        /*
         * Call method under test
         */
        int i = st.size();
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(stExpected, st);
        assertEquals(stExpected.size(), i);
    }

    @Test
    public final void testSizeNotEmpty() {
        /*
         * Set up variables
         */
        Set<String> st = this.createFromArgsTest("apple", "banana");
        Set<String> stExpected = this.createFromArgsRef("apple", "banana");
        /*
         * Call method under test
         */
        int i = st.size();
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(stExpected, st);
        assertEquals(stExpected.size(), i);
    }

}
