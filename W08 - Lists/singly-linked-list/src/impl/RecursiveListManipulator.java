package impl;

import java.util.Comparator;

import common.InvalidIndexException;
import common.InvalidListException;
import common.ListNode;
import interfaces.IFilterCondition;
import interfaces.IListManipulator;
import interfaces.IMapTransformation;
import interfaces.IReduceOperator;

/**
 * This class represents the recursive implementation of the IListManipulator interface.
 *
 */
public class RecursiveListManipulator implements IListManipulator {

    /*
    Recursively calculates the size of the list.
     */
    @Override
    public int size(ListNode head) {
        if (head == null)  {
            return 0;
        }
        return 1 + size(head.next);
    }

    /*
    Recursively checks if the list contains a specific element.
     */
    @Override
    public boolean contains(ListNode head, Object element) {
        return head != null && (head.element.equals(element) || contains(head.next, element));
    }

    /*
    Counts up the number of occurrences of a specific element in the list recursively.
     */
    @Override
    public int count(ListNode head, Object element) {
        if (head == null) {
            return 0;
        }
        if (!head.element.equals(element)) {
            return count(head.next, element);
        }
        return 1 + count(head.next, element);
    }

    /*
    A recursive method to convert a list to a comma separated string of elements.
     */
    @Override
    public String convertToString(ListNode head) {
        if (head == null) {
            return "";
        }
        if (head.next == null) {
            return String.valueOf(head.element);
        }
        return head.element + "," + convertToString(head.next);
    }

    /*
    A recursive method to get the n-th element from the front of the list.
     */
    @Override
    public Object getFromFront(ListNode head, int n) throws InvalidIndexException {
        if (n >= size(head) || n < 0) {
            throw new InvalidIndexException();
        }
        return (n == 0) ? head.element : getFromFront(head.next, n - 1);
    }

    /*
    A recursive method to get the n-th element from the back of the list.
     */
    @Override
    public Object getFromBack(ListNode head, int n) throws InvalidIndexException {
        if (n >= size(head) || n < 0) {
            throw new InvalidIndexException();
        }
        return getFromFront(head, size(head) - n - 1); // Using get from front to reduce complexity
    }

    /*
    A recursive method that checks if two lists are equal.
     */
    @Override
    public boolean equals(ListNode head1, ListNode head2) {
        // If lists have identical length then this will return true since with both be null, otherwise lists are of
        // differing lengths
        if (head1 == null || head2 == null) {
            return head1 == head2;
        }
        if (!head1.equals(head2)) { // Checking if nodes are equal (specifically if elements are equal)
            return false;
        }
        return equals(head1.next, head2.next);
    }

    /*
    Recursively checks if the list contains any duplicate nodes.
     */
    @Override
    public boolean containsDuplicates(ListNode head) {
        if (head == null) {
            return false;
        }
        if (contains(head.next, head.element)) {
            return true;
        }
        return containsDuplicates(head.next);
    }

    /*
    Recursively joins two lists together, with the second list joined to the back of the first list.
     */
    @Override
    public ListNode append(ListNode head1, ListNode head2) {
        if (head1 == null) {  // If either of the lists are null, return the other list
            return head2;
        }
        if (head2 == null) {
            return head1;
        }
        return new ListNode(head1.element, append(head1.next, head2));
    }

    /*
    Recursively reverses a linked list.
     */
    @Override
    public ListNode reverse(ListNode head) {
        if (head == null || head.next == null) { // A list with 0 or 1 nodes cannot be reversed
            return head;
        }
        ListNode newHead = reverse(head.next); // The end node is the new head node
        head.next.next = head; // The head nodes next node should now point to previous head node (now end node)
        head.next = null; // The previous head node should now point to null (making it the end node)
        return newHead;
    }

    /*
    Splits the list at the n-th listNode into two sub-lists and returns a list of those two sub-lists. This method does
    not change the original list. The method works by calling the recursive method copy that creates a copy of the
    initial list. Then another private recursive method getIndex is called and this returns the listNode that the list
    should be split at. A copy of the listNode after the split node is then created, this is the head of the second list.
    Finally the split node is set to point to null, creating the separate lists. These lists are then appended together
    into one larger list.
     */
    @Override
    public ListNode split(ListNode head, int n) throws InvalidIndexException, InvalidListException {
        if (head == null || head.next == null) {
            throw new InvalidListException(); // List cannot be split if it only has 1 or less nodes
        }
        if (n >= size(head) || n <= 0) {
            throw new InvalidIndexException();
        }

        ListNode head1 = copy(head); // List needs to be copied so that this method doesnt split original list
        ListNode split = getIndex(head1, n - 1);
        ListNode head2 = split.next; // The head of the second list is the node after the split
        split.next = null; // The split node should then point to null, separating the two lists
        return append(new ListNode(head1), new ListNode(head2));
    }

    /*
    Creates a new copy of the list recursively and returns the head listNode of this copied. Is a private recursive
    method for use within the split method.
     */
    private ListNode copy(ListNode head) {
        if (head == null) { // A list with a length of zero is just null, so return null
            return null;
        }
        return new ListNode(head.element, copy(head.next));
    }

    /*
    A private recursive method for use within the split method. Recursively moves along the list from the head to fetch
    the n-th listNode from the front.
     */
    private ListNode getIndex(ListNode head, int n) {
        if (n == 0) {
            return head;
        }
        return getIndex(head.next, n - 1);
    }

    /*
    This recursive method converts a list of lists down into a single list, where ordering within the lists is preserved.
    This method recursively steps through the list and appends each sub-list to the new list until the list doesn't have
    any more sub-lists to append. Then the new list is returned. This method does not change the original list passed in
    as an argument.
     */
    @Override
    public ListNode flatten(ListNode head) {
        if (head == null) { // A list with a length of zero can already be considered flattened
            return null;
        }
        return append((ListNode) head.element, flatten(head.next));
    }

    /*
    An auxiliary method for the private recursive isCircular function.
     */
    @Override
    public boolean isCircular(ListNode head) {
        if (head == null || head.next == null) {
            return false;
        }
        return isCircular(head, head.next.next, head.next);
    }

    /*
    A recursive function that returns a boolean value, stating if the linked list is perfectly circular (not just
    cyclic). This method uses a recursive version of floyd's cycle-finding algorithm with a O(n) time complexity and
    O(1) space complexity. This algorithm entails moving through the list recursively with a listNode that moves round
    quickly and one that moves round slowly. If at any point the slow listNode equals the large listNode the a loop must
    be present within the list. The method then checks if both the fast and slow and head listNode are all equal, since
    if the loop is cyclic and cycles right round back to the head of the list then it is perfectly circular. If at any
    point the fast listNode points to a value of null, then the the method returns false, since if the loop is cyclic,
    no endpoint should be able to be reached.
     */
    private boolean isCircular(ListNode head, ListNode fast, ListNode slow) {
        if (fast == null || fast.next == null) {  // Aan empty list or single node list is not considered circular
            return false;
        }
        if (slow == fast) {
            return (fast == head); // If the list is cyclic from the head of the list then it is circular
        }
        return isCircular(head, fast.next.next, slow.next);
    }

    /*
    An auxiliary method for the private recursive containsCycles function.
     */
    @Override
    public boolean containsCycles(ListNode head) {
        if (head == null || head.next == null) {
            return false;
        }
        return containsCycles(head.next.next, head.next);
    }

    /*
    A recursive function that returns a boolean value, stating if the linked list is cyclic (has a loop in it). This
    method uses a recursive version of floyd's cycle-finding algorithm with a O(n) time complexity and O(1) space
    complexity. This algorithm entails moving through the list recursively with a listNode that moves round quickly and
    one that moves round slowly. If at any point the slow listNode equals the large listNode the a loop must be present
    within the list and so the method returns true. If at any point the fast listNode points to a value of null, then
    the the method returns false, since if the loop is cyclic, no endpoint should be able to be reached.
     */
    private boolean containsCycles(ListNode fast, ListNode slow) {
        if (fast == null || fast.next == null) { // An list with a length of zero or one cannot be considered cyclic
            return false;
        }
        if (slow == fast) {
            return true;
        }
        return containsCycles(fast.next.next, slow.next); // Fast listNode skipping an element in the list each recursive step
    }

    /*
    Using merge sort for sorting as is a recursive divide and conquer algorithm. Therefore this method has a time complexity
    of O(nlog(n)). Merge sort was preferred to quicksort because a split method was already included. Merge sort has the
    advantage over quick sort in that it doesnt need any extra space when merging the linked lists since the ListNodes can
    be joined, so no new ListNodes have to be made, giving it a O(1) constant time complexity (better than the array
    implementation!). Further more, a reliable quick sort algorithm with a minimal amount of pathological cases requires
    a random pivot. This random pivot cannot be simply accessed like in a sequential data structure, instead the list would
    need to be iterated through, actually increasing the complexity of the algorithm.
     */
    @Override
    public ListNode sort(ListNode head, Comparator comparator) {
        if (head == null || head.next == null) {
            return head; // A list of length 0 or 1 is already or cannot be sorted
        }
        ListNode list;
        try { // Try catch block is needed here since split method, can throw exceptions
            list = split(head, size(head) / 2); // Using the already available split method to divide the list in two
        }
        catch (InvalidIndexException | InvalidListException exception) {
            return null;
        }
        ListNode firstList = (ListNode) list.element;
        ListNode secondList = (ListNode) list.next.element;

        firstList = sort(firstList, comparator);
        secondList = sort(secondList, comparator);
        return merge(firstList, secondList, comparator);
    }

    /*
    A modified version of the append method that takes in a comparator and returns the a new merged list that is sorted.
     */
    private ListNode merge(ListNode head1, ListNode head2, Comparator comparator) {
        if (head1 == null) {
            return head2;
        }
        if (head2 == null) {
            return head1;
        }
        // If the element in head 1 is less than or equal to the element in node 2
        if (comparator.compare(head1.element, head2.element) <= 0) {
            head1.next = merge(head1.next, head2, comparator);
            return head1;
        }
        else {
            head2.next = merge(head1, head2.next, comparator);
            return head2;
        }
    }

    /*
    An auxiliary method for the private recursive map function.
    */
    @Override
    public ListNode map(ListNode head, IMapTransformation transformation) {
        if (head == null) {
            return null;
        }
        return map(null, head, transformation);
    }

    /*
    A recursive map method that creates a new list that whereby each element has been transformed a certain way from its
    initial value in the first list. Each recursive step a new listNode is created whose element has been transformed,
    this is then appended to the end of the new list. If the end of the old array has been reached the recursive method
    returns the head to this new mapped list.
     */
    private ListNode map(ListNode newCurr, ListNode oldCurr, IMapTransformation transformation) {
        if (oldCurr == null) {
            return newCurr;
        }
        else {
            newCurr = append(newCurr, new ListNode(transformation.transform(oldCurr.element)));
        }
        return map(newCurr, oldCurr.next, transformation);
    }

    /*
    A recursive reduce method that performs an operation to combine all elements in the list recursively and add to
    all this an initial object to combine with. This combination is performed recursively until the head variable is
    null and all of the list has been iterated through.
     */
    @Override
    public Object reduce(ListNode head, IReduceOperator operator, Object initial) {
        if (head == null) {
            return initial;
        }
        initial = operator.operate(head.element, initial);
        return reduce(head.next, operator, initial);
    }

    /*
    An auxiliary method for the private recursive filter function.
     */
    @Override
    public ListNode filter(ListNode head, IFilterCondition condition) {
        if (head == null) {
            return null;
        }
        return filter(null, head, condition);
    }

    /*
    Filter function that takes in an extra newCurr variable from auxiliary function which is the head of the new list.
    Each recursive step if an element satisfies a condition in the old list it is appended to the new list. If the end
    of the old list has been reached, we return from the recursive method the list node that is the head of the new
    filtered list.
     */
    private ListNode filter(ListNode newCurr, ListNode oldCurr, IFilterCondition condition) {
        if (oldCurr == null) {
            return newCurr;
        }
        if (condition.isSatisfied(oldCurr.element)) {
            newCurr = append(newCurr, new ListNode(oldCurr.element));
        }
        return filter(newCurr, oldCurr.next, condition);
    }
}
