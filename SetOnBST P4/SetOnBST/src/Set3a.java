import java.util.Iterator;

import components.binarytree.BinaryTree;
import components.binarytree.BinaryTree1;
import components.set.Set;
import components.set.SetSecondary;

/**
 * {@code Set} represented as a {@code BinaryTree} (maintained as a binary
 * search tree) of elements with implementations of primary methods.
 *
 * @param <T>
 *            type of {@code Set} elements
 * @mathdefinitions <pre>
 * IS_BST(
 *   tree: binary tree of T
 *  ): boolean satisfies
 *  [tree satisfies the binary search tree properties as described in the
 *   slides with the ordering reported by compareTo for T, including that
 *   it has no duplicate labels]
 * </pre>
 * @convention IS_BST($this.tree)
 * @correspondence this = labels($this.tree)
 *
 * @author Roshan Varma and Quantez Merchant
 *
 */
public class Set3a<T extends Comparable<T>> extends SetSecondary<T> {

    /*
     * Private members --------------------------------------------------------
     */

    /**
     * Elements included in {@code this}.
     */
    private BinaryTree<T> tree;

    /**
     * Returns whether {@code x} is in {@code t}.
     *
     * @param <T>
     *            type of {@code BinaryTree} labels
     * @param t
     *            the {@code BinaryTree} to be searched
     * @param x
     *            the label to be searched for
     * @return true if t contains x, false otherwise
     * @requires IS_BST(t)
     * @ensures isInTree = (x is in labels(t))
     */
    private static <T extends Comparable<T>> boolean isInTree(BinaryTree<T> t,
            T x) {
        assert t != null : "Violation of: t is not null";
        assert x != null : "Violation of: x is not null";
        // Initialize boolean to false
        boolean isInTree = false;
        // Recursively go through the tree to see if x is in the tree
        if (t.size() > 0) {
            BinaryTree<T> left = t.newInstance();
            BinaryTree<T> right = t.newInstance();
            T root = t.disassemble(left, right);
            if (root.compareTo(x) == 0) {
                isInTree = true;
            } else if (root.compareTo(x) > 0) {
                isInTree = isInTree(left, x);
            } else {
                isInTree = isInTree(right, x);
            }
            t.assemble(root, left, right);
        }
        // Return boolean
        return isInTree;
    }

    /**
     * Inserts {@code x} in {@code t}.
     *
     * @param <T>
     *            type of {@code BinaryTree} labels
     * @param t
     *            the {@code BinaryTree} to be searched
     * @param x
     *            the label to be inserted
     * @aliases reference {@code x}
     * @updates t
     * @requires IS_BST(t) and x is not in labels(t)
     * @ensures IS_BST(t) and labels(t) = labels(#t) union {x}
     */
    private static <T extends Comparable<T>> void insertInTree(BinaryTree<T> t,
            T x) {
        assert t != null : "Violation of: t is not null";
        assert x != null : "Violation of: x is not null";
        // If the size of the tree is less than 1, make x the root of the tree
        if (t.size() < 1) {
            BinaryTree<T> empty = t.newInstance();
            BinaryTree<T> empty2 = t.newInstance();
            t.assemble(x, empty, empty2);
        } else {
            // Recursively go through the tree, finding the right position for x
            BinaryTree<T> left = t.newInstance();
            BinaryTree<T> right = t.newInstance();
            T root = t.disassemble(left, right);
            if (root.compareTo(x) > 0) {
                insertInTree(left, x);
            } else {
                insertInTree(right, x);
            }
            t.assemble(root, left, right);
        }

    }

    /**
     * Removes and returns the smallest (left-most) label in {@code t}.
     *
     * @param <T>
     *            type of {@code BinaryTree} labels
     * @param t
     *            the {@code BinaryTree} from which to remove the label
     * @return the smallest label in the given {@code BinaryTree}
     * @updates t
     * @requires IS_BST(t) and |t| > 0
     * @ensures <pre>
     * IS_BST(t)  and  removeSmallest = [the smallest label in #t]  and
     *  labels(t) = labels(#t) \ {removeSmallest}
     * </pre>
     */
    private static <T> T removeSmallest(BinaryTree<T> t) {
        assert t != null : "Violation of: t is not null";
        // Create variable x, and the left and right trees
        T x;
        BinaryTree<T> left = t.newInstance();
        BinaryTree<T> right = t.newInstance();
        // Recursively go through the tree to find the smallest value in the tree
        T root = t.disassemble(left, right);
        if (left.size() > 0) {
            x = removeSmallest(left);
            t.assemble(root, left, right);
        } else {
            x = root;
            t.transferFrom(right);
        }
        // Return x
        return x;
    }

    /**
     * Finds label {@code x} in {@code t}, removes it from {@code t}, and
     * returns it.
     *
     * @param <T>
     *            type of {@code BinaryTree} labels
     * @param t
     *            the {@code BinaryTree} from which to remove label {@code x}
     * @param x
     *            the label to be removed
     * @return the removed label
     * @updates t
     * @requires IS_BST(t) and x is in labels(t)
     * @ensures <pre>
     * IS_BST(t)  and  removeFromTree = x  and
     *  labels(t) = labels(#t) \ {x}
     * </pre>
     */
    private static <T extends Comparable<T>> T removeFromTree(BinaryTree<T> t,
            T x) {
        assert t != null : "Violation of: t is not null";
        assert x != null : "Violation of: x is not null";
        // Create left and right trees
        BinaryTree<T> left = t.newInstance();
        BinaryTree<T> right = t.newInstance();
        // Recursively go through the left and right trees finding x and removing it
        T root = t.disassemble(left, right);
        if (root.compareTo(x) == 0) {
            if (right.size() < 1) {
                t.transferFrom(left);
            } else {
                T smallestRight = removeSmallest(right);
                t.assemble(smallestRight, left, right);
            }
        } else {
            if (root.compareTo(x) > 0) {
                removeFromTree(left, x);
            } else {
                removeFromTree(right, x);
            }
            t.assemble(root, left, right);
        }
        // Return the removed value
        return x;
    }

    /**
     * Creator of initial representation.
     */
    private void createNewRep() {
        // Initialize this.tree
        this.tree = new BinaryTree1<T>();

    }

    /*
     * Constructors -----------------------------------------------------------
     */

    /**
     * No-argument constructor.
     */
    public Set3a() {
        // Call createFromRep
        this.createNewRep();

    }

    /*
     * Standard methods -------------------------------------------------------
     */

    @SuppressWarnings("unchecked")
    @Override
    public final Set<T> newInstance() {
        try {
            return this.getClass().getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(
                    "Cannot construct object of type " + this.getClass());
        }
    }

    @Override
    public final void clear() {
        this.createNewRep();
    }

    @Override
    public final void transferFrom(Set<T> source) {
        assert source != null : "Violation of: source is not null";
        assert source != this : "Violation of: source is not this";
        assert source instanceof Set3a<?> : ""
                + "Violation of: source is of dynamic type Set3<?>";
        /*
         * This cast cannot fail since the assert above would have stopped
         * execution in that case: source must be of dynamic type Set3a<?>, and
         * the ? must be T or the call would not have compiled.
         */
        Set3a<T> localSource = (Set3a<T>) source;
        this.tree = localSource.tree;
        localSource.createNewRep();
    }

    /*
     * Kernel methods ---------------------------------------------------------
     */

    @Override
    public final void add(T x) {
        assert x != null : "Violation of: x is not null";
        assert !this.contains(x) : "Violation of: x is not in this";
        // Call insertInTree to add x to the tree
        insertInTree(this.tree, x);
    }

    @Override
    public final T remove(T x) {
        assert x != null : "Violation of: x is not null";
        assert this.contains(x) : "Violation of: x is in this";
        // Call removeFromTree to remove x from the tree and return it
        return removeFromTree(this.tree, x);
    }

    @Override
    public final T removeAny() {
        assert this.size() > 0 : "Violation of: this /= empty_set";
        // Call removeSmallest to get the smallest value from the tree
        T x = removeSmallest(this.tree);

        // Return the smallest value from the tree
        return x;
    }

    @Override
    public final boolean contains(T x) {
        assert x != null : "Violation of: x is not null";
        // Call isInTree to check if x is in the tree and return true or false
        return isInTree(this.tree, x);
    }

    @Override
    public final int size() {
        // Return the size of the tree
        return this.tree.size();
    }

    @Override
    public final Iterator<T> iterator() {
        return this.tree.iterator();
    }

}
