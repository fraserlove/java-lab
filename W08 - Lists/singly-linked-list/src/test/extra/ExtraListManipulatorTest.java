package test.extra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Comparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import common.InvalidIndexException;
import common.InvalidListException;
import common.ListNode;
import interfaces.IListManipulator;
import interfaces.IReduceOperator;
import interfaces.IMapTransformation;

/**
 * Extended Abstract test class for extra testing of IListManipulator implementations.
 */
public abstract class ExtraListManipulatorTest {

    private final float pi = 3.14f;
    private final float e = 2.72f;
    private final char a = 'a';
    private final char b = 'b';
    private final int one = 1;
    private final int two = 2;
    private final int three = 3;

    private IListManipulator manipulator;

    private ListNode nullList;
    private ListNode listOfNulls;
    private ListNode charList;
    private ListNode floatList;
    private ListNode intList;

    private static final int TEN = 10;
    private static final int SEVEN = 7;
    private static final int FOUR = 4;

    /**
     * The factory method that must be implemented by any concrete subclass of ListManipulatorTest in order to instantiate a particular implementation of IListManipulator.
     * @return the desired concrete implementation of IListManipulator.
     */
    public abstract IListManipulator makeListManipulator();

    /**
     * Method used to set up common test objects prior to every test.
     */
    @BeforeEach
    public void setup() {
        manipulator = makeListManipulator();

        nullList = new ListNode(null);
        listOfNulls = new ListNode(null, nullList);

        charList = new ListNode(a, new ListNode(b));
        floatList = new ListNode(pi, new ListNode(e));
        intList = new ListNode(one, new ListNode(two, new ListNode(three)));
    }

    /**
     * Tests if count() can deal with null values and lists of null values. Nulls are not counted
     */
    @Test
    public void countDealWithNullsAndListsOfNulls() {
        assertEquals(0, manipulator.count(null, null));
        assertEquals(0, manipulator.count(null, 0));
        assertEquals(0, manipulator.count(nullList, null));
        assertEquals(0, manipulator.count(nullList, 0));
        assertEquals(0, manipulator.count(listOfNulls, null));
    }

    /**
     * Tests if count() can count different types. More importantly if the comparison is correct.
     */
    @Test
    public void countDealWithDifferentTypes() {
        assertEquals(1, manipulator.count(charList, a));
        assertEquals(1, manipulator.count(floatList, pi));
    }

    /**
     * Tests if count() can deal with a list having elements of one type and the item to count for being another type.
     */
    @Test
    public void countDealWithDifferentListAndArgumentTypes() {
        assertEquals(0, manipulator.count(floatList, a));
    }

    /**
     * Tests if convertToString() can convert a list of nulls and an empty list into suitable a string.
     */
    @Test
    public void convertToStringWithListOfNulls() {
        assertEquals("", manipulator.convertToString(null));
        assertEquals("null,null", manipulator.convertToString(listOfNulls));
    }

    /**
     * Tests if convertToString() can convert a lists of different types into suitable a string.
     */
    @Test
    public void convertToStringWithDifferentTypes() {
        assertEquals(a + "," + b, manipulator.convertToString(charList));
        assertEquals(pi + "," + e, manipulator.convertToString(floatList));
    }

    /**
     * Tests if getFromFront() throws a IndexToLarge error when trying to access index to large.
     * @throws InvalidIndexException not expected to be thrown during this test.
     */
    @Test
    public void getFromFrontInvalidIndex() throws InvalidIndexException {
        assertThrows(InvalidIndexException.class, () -> manipulator.getFromFront(null, 0));
        assertThrows(InvalidIndexException.class, () -> manipulator.getFromFront(charList, -1));
        assertEquals(a, manipulator.getFromFront(charList, 0));
        assertEquals(b, manipulator.getFromFront(charList, 1));
        assertThrows(InvalidIndexException.class, () -> manipulator.getFromFront(charList, 2));
    }

    /**
     * Tests if getFromBack() throws a IndexToLarge error when trying to access index to large.
     * @throws InvalidIndexException not expected to be thrown during this test.
     */
    @Test
    public void getFromBackInvalidIndex() throws InvalidIndexException {
        assertThrows(InvalidIndexException.class, () -> manipulator.getFromBack(null, 0));
        assertThrows(InvalidIndexException.class, () -> manipulator.getFromBack(charList, -1));
        assertEquals(b, manipulator.getFromBack(charList, 0));
        assertEquals(a, manipulator.getFromBack(charList, 1));
        assertThrows(InvalidIndexException.class, () -> manipulator.getFromBack(charList, 2));
    }

    /**
     * Tests is append() can append a list of nulls.
     */
    @Test
    public void appendListOfNulls() {
        assertTrue(manipulator.equals(listOfNulls, manipulator.append(listOfNulls, null)));
    }

    /**
     * Tests if map() can handle a null value.
     */
    @Test
    public void mapNull() {
        assertNull(manipulator.map(null, multiplyFour));
    }

    /**
     * Tests if map() can work with different types, provided a new IMapTransformation.
     */
    @Test
    public void mapDifferentTypes() {
        assertTrue(manipulator.equals(new ListNode(pi * FOUR, new ListNode(e * FOUR)), manipulator.map(floatList, multiplyFour)));
    }

    /**
     * Tests is reduce() can work with more complex IReduceOperator's.
     */
    @Test
    public void complexReduce() {
        assertEquals(SEVEN + one + SEVEN + two + SEVEN + three + TEN, manipulator.reduce(intList, addSeven, TEN));
        assertEquals(one * two * three, manipulator.reduce(intList, multiply, 1));
    }

    /**
     * Tests if containsDuplicates() can deal with null values. Nulls are not considered elements and so contains should
     * be skipped.
     */
    @Test
    public void containsDuplicatesListOfNulls() {
        assertFalse(manipulator.containsDuplicates(listOfNulls));
    }

    /**
     * This ITransformation is to permit the map method to multiply each element by 4.
     */
    private final IMapTransformation multiplyFour = new IMapTransformation() {

        @Override
        public Object transform(Object element) {
            return (Float) element * FOUR;
        }
    };

    /**
     * This IOperator is to permit the reduce method to perform addition when combining 2 elements.
     */
    private final IReduceOperator addSeven = new IReduceOperator() {

        @Override
        public Object operate(Object element1, Object element2) {
            return (Integer) element1 + (Integer) element2 + SEVEN;
        }
    };

    /**
     * This IOperator is to permit the reduce method to perform multiplication when combining 2 elements.
     */
    private final IReduceOperator multiply = new IReduceOperator() {

        @Override
        public Object operate(Object element1, Object element2) {
            return (Integer) element1 * (Integer) element2;
        }
    };
}
