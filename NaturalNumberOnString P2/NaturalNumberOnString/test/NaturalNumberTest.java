import static org.junit.Assert.assertEquals;

import org.junit.Test;

import components.naturalnumber.NaturalNumber;
import components.naturalnumber.NaturalNumber1L;

/**
 * JUnit test fixture for {@code NaturalNumber}'s constructors and kernel
 * methods.
 *
 * @authors Roshan Varma & Quantez Merchant
 *
 */
public abstract class NaturalNumberTest {

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * implementation under test and returns the result.
     *
     * @return the new number
     * @ensures constructorTest = 0
     */
    protected abstract NaturalNumber constructorTest();

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * implementation under test and returns the result.
     *
     * @param i
     *            {@code int} to initialize from
     * @return the new number
     * @requires i >= 0
     * @ensures constructorTest = i
     */
    protected abstract NaturalNumber constructorTest(int i);

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * implementation under test and returns the result.
     *
     * @param s
     *            {@code String} to initialize from
     * @return the new number
     * @requires there exists n: NATURAL (s = TO_STRING(n))
     * @ensures s = TO_STRING(constructorTest)
     */
    protected abstract NaturalNumber constructorTest(String s);

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * implementation under test and returns the result.
     *
     * @param n
     *            {@code NaturalNumber} to initialize from
     * @return the new number
     * @ensures constructorTest = n
     */
    protected abstract NaturalNumber constructorTest(NaturalNumber n);

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * reference implementation and returns the result.
     *
     * @return the new number
     * @ensures constructorRef = 0
     */
    protected abstract NaturalNumber constructorRef();

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * reference implementation and returns the result.
     *
     * @param i
     *            {@code int} to initialize from
     * @return the new number
     * @requires i >= 0
     * @ensures constructorRef = i
     */
    protected abstract NaturalNumber constructorRef(int i);

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * reference implementation and returns the result.
     *
     * @param s
     *            {@code String} to initialize from
     * @return the new number
     * @requires there exists n: NATURAL (s = TO_STRING(n))
     * @ensures s = TO_STRING(constructorRef)
     */
    protected abstract NaturalNumber constructorRef(String s);

    /**
     * Invokes the appropriate {@code NaturalNumber} constructor for the
     * reference implementation and returns the result.
     *
     * @param n
     *            {@code NaturalNumber} to initialize from
     * @return the new number
     * @ensures constructorRef = n
     */
    protected abstract NaturalNumber constructorRef(NaturalNumber n);

    /*
     * Test cases for constructors
     */

    @Test
    public final void testNoArgumentConstructor() {
        /*
         * Set up variables and call method under test
         */
        NaturalNumber q = this.constructorTest();
        NaturalNumber qExpected = this.constructorRef();
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(qExpected, q);
    }

    // test ints
    @Test
    public final void testConstructorZeroInt() {
        /*
         * Set up variables and call method under test
         */
        NaturalNumber n = this.constructorTest(0);
        NaturalNumber nExpected = this.constructorRef(0);
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(nExpected, n);
    }

    @Test
    public final void testConstructorNonZeroInt() {
        /*
         * Set up variables and call method under test
         */
        NaturalNumber n = this.constructorTest(1);
        NaturalNumber nExpected = this.constructorRef(1);
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(nExpected, n);
    }

    @Test
    public final void testConstructorMaxInt() {
        /*
         * Set up variables and call method under test
         */
        NaturalNumber n = this.constructorTest(Integer.MAX_VALUE);
        NaturalNumber nExpected = this.constructorRef(Integer.MAX_VALUE);
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(nExpected, n);
    }

    // test strings
    @Test
    public final void testConstructorZeroString() {
        /*
         * Set up variables and call method under test
         */
        NaturalNumber n = this.constructorTest("0");
        NaturalNumber nExpected = this.constructorRef("0");
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(nExpected, n);
    }

    @Test
    public final void testConstructorNonZeroString() {
        /*
         * Set up variables and call method under test
         */
        NaturalNumber n = this.constructorTest("1");
        NaturalNumber nExpected = this.constructorRef("1");
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(nExpected, n);
    }

    // test NaturalNumbers
    @Test
    public final void testConstructorZeroNaturalNumber() {
        /*
         * Set up variables and call method under test
         */
        NaturalNumber n1 = new NaturalNumber1L();
        NaturalNumber n2 = this.constructorTest(n1);
        NaturalNumber nExpected = this.constructorRef(n1);
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(nExpected, n2);
    }

    @Test
    public final void testConstructorNonZeroNaturalNumber() {
        /*
         * Set up variables and call method under test
         */
        NaturalNumber n1 = new NaturalNumber1L(1);
        NaturalNumber n2 = this.constructorTest(n1);
        NaturalNumber nExpected = this.constructorRef(n1);
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(nExpected, n2);
    }

    /*
     * Test cases for kernel methods
     */

    // test multiplyBy10()
    @Test
    public final void testMultiplyBy10StartingWithZero() {
        /*
         * Set up variables and call method under test
         */
        NaturalNumber n = this.constructorTest(0);
        NaturalNumber nExpected = this.constructorRef(7);

        n.multiplyBy10(7);
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(nExpected, n);
    }

    @Test
    public final void testMultiplyBy10StartingWithNonZero() {
        /*
         * Set up variables and call method under test
         */
        NaturalNumber n = this.constructorTest(12);
        NaturalNumber nExpected = this.constructorRef(123);

        n.multiplyBy10(3);
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(nExpected, n);
    }

    @Test
    public final void testMultiplyBy10StartingWithIntMax() {
        /*
         * Set up variables and call method under test
         */
        NaturalNumber n = this.constructorTest(Integer.MAX_VALUE);
        NaturalNumber nExpected = this.constructorRef(Integer.MAX_VALUE);

        n.multiplyBy10(5);
        nExpected.multiplyBy10(5);
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(nExpected, n);
    }

    // test divideBy10()
    @Test
    public final void testDivideBy10EndingWithZero() {
        /*
         * Set up variables and call method under test
         */
        NaturalNumber n = this.constructorTest(1);
        NaturalNumber nExpected = this.constructorRef(0);

        int i = n.divideBy10();
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(nExpected, n);
        assertEquals(i, 1);
    }

    @Test
    public final void testDivideBy10EndingWithNonZero() {
        /*
         * Set up variables and call method under test
         */
        NaturalNumber n = this.constructorTest(1234);
        NaturalNumber nExpected = this.constructorRef(123);

        int i = n.divideBy10();
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(nExpected, n);
        assertEquals(i, 4);
    }

    @Test
    public final void testDivideBy10EndingWithValueOverIntMax() {
        /*
         * Set up variables and call method under test
         */
        NaturalNumber n = this.constructorTest("214748364793");
        NaturalNumber nExpected = this.constructorRef("21474836479");

        int i = n.divideBy10();
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(nExpected, n);
        assertEquals(i, 3);
    }

    // test izZero()
    @Test
    public final void testIsZeroWithZero() {
        /*
         * Set up variables and call method under test
         */
        NaturalNumber n = this.constructorTest(0);
        NaturalNumber nExpected = this.constructorRef(0);

        boolean i = n.isZero();
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(nExpected, n);
        assertEquals(i, true);
    }

    @Test
    public final void testIsZeroWithNonZero() {
        /*
         * Set up variables and call method under test
         */
        NaturalNumber n = this.constructorTest(12345);
        NaturalNumber nExpected = this.constructorRef(12345);

        boolean i = n.isZero();
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(nExpected, n);
        assertEquals(i, false);
    }

    @Test
    public final void testIsZeroWithIntMax() {
        /*
         * Set up variables and call method under test
         */
        NaturalNumber n = this.constructorTest(Integer.MAX_VALUE);
        NaturalNumber nExpected = this.constructorRef(Integer.MAX_VALUE);

        boolean i = n.isZero();
        /*
         * Assert that values of variables match expectations
         */
        assertEquals(nExpected, n);
        assertEquals(i, false);
    }
}
