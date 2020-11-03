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
 * This class represents the iterative implementation of the IListManipulator interface.
 *
 */
public class IterativeListManipulator implements IListManipulator {

    /*
    Calculates the size of the list using an iterative approach.
     */
    @Override
    public int size(ListNode head) {
        int size = 0;
        while (head != null) {
            size++;
            head = head.next;
        }
        return size;
    }

    /*
    Iteratively checks if the list contains a specific element.
     */
    @Override
    public boolean contains(ListNode head, Object element) {
        while (head != null) {
            if (head.element.equals(element)) {
                return true;
            }
            head = head.next;
        }
        return false;
    }

    /*
    Counts up the number of occurrences of a specific element in the list iteratively.
     */
    @Override
    public int count(ListNode head, Object element) {
        int count = 0;
        while (head != null) {
            if (head.element.equals(element)) {
                count++;
            }
            head = head.next;
        }
        return count;
    }

    /*
    An iterative method to convert a list to a comma separated string of elements.
     */
    @Override
    public String convertToString(ListNode head) {
        String list = "";
        while (head != null) {
            // Used String.valueOf() because it has support for conversion from multiple types, unlike Integer.valueOf() etc.
            list = list.concat(String.valueOf(head.element));
            if (head.next != null) {
                list = list.concat(",");
            }
            head = head.next;
        }
        return list;
    }

    /*
    An iterative method to get the n-th element from the front of the list.
     */
    @Override
    public Object getFromFront(ListNode head, int n) throws InvalidIndexException {
        if (n >= size(head) || n < 0) {
            throw new InvalidIndexException();
        }
        for (int i = 0; i < n; i++) {
            head = head.next;
        }
        return head.element;
    }

    /*
    An iterative method to get the n-th element from the front of the list.
     */
    @Override
    public Object getFromBack(ListNode head, int n) throws InvalidIndexException {
        if (n >= size(head) || n < 0) {
            throw new InvalidIndexException();
        }
        return getFromFront(head, size(head) - n - 1); // Using get from front to reduce complexity
    }

    /*
    An iterative method that checks if two lists are equal.
    */
    @Override
    public boolean equals(ListNode head1, ListNode head2) {
        while (head1 != null && head2 != null) {
            if (!head1.equals(head2)) { // Checking if nodes are equal (specifically if elements are equal)
                return false;
            }
            head1 = head1.next;
            head2 = head2.next;
        }
        // If lists have identical length then this will return true since both lists were iterated over concurrently
        // so both head1 and head2 must be null.
        return head1 == head2;
    }

    /*
    Iteratively checks if the list contains any duplicate nodes.
     */
    @Override
    public boolean containsDuplicates(ListNode head) {
        while (head != null) {
            if (contains(head.next, head.element)) { // Doesnt matter if head.next is null because method can deal with nulls
                return true;
            }
            head = head.next;
        }
        return false;
    }

    /*
    Iteratively joins two lists together, with the second list joined to the back of the first list.
     */
    @Override
    public ListNode append(ListNode head1, ListNode head2) {
        if (head1 != null) {
            ListNode curr = head1;
            while (curr != null && curr.next != null) { // Move to the end of the first list
                curr = curr.next;
            }
            curr.next = head2; // Set the last node of the first list to point to the first node in the second list.
            return head1;
        }
        return head2;
    }

    /*
    Iteratively reverses a linked list.
     */
    @Override
    public ListNode reverse(ListNode head) {
        ListNode prev = null;
        ListNode curr = head;
        ListNode next;
        while (curr != null) {
            next = curr.next;
            curr.next = prev; // Pointing the current node to the previous node in the list
            prev = curr;  // Moving along the list
            curr = next;
        }
        return prev;
    }

    /*
    Splits the list at the n-th listNode into two sub-lists and returns a list of those two sub-lists. This method does
    not change the original list. The method works by calling the iterative method copy that creates a copy of the
    initial list. Then the method iterates through the copied list to find the listNode that the list should be split at.
    A copy of the listNode after the split node is then created, this is the head of the second list. Finally the split
    node is set to point to null, creating the separate lists. These lists are then appended together into one larger list.
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
        ListNode split = head1;
        for (int i = 0; i < (n - 1); i++) {
            split = split.next;
        }

        ListNode head2 = split.next; // The head of the second list is the node after the split
        split.next = null; // The split node should then point to null, separating the two lists
        return append(new ListNode(head1), new ListNode(head2));
    }

    /*
    Creates a new copy of the list iteratively and returns the head listNode of this copied. Is a private iterative
    method for use within the split method.
     */
    private ListNode copy(ListNode head) {
        ListNode newCurr = null;
        while (head != null) {
            newCurr = append(newCurr, new ListNode(head.element));
            head = head.next;
        }
        return newCurr;
    }

    /*
    This iterative method converts a list of lists down into a single list, where ordering within the lists is preserved.
    This method iteratively steps through the list and appends each sub-list to the new list until the list doesn't have
    any more sub-lists to append. Then the new list is returned. This method does not change the original list passed in
    as an argument.
     */
    @Override
    public ListNode flatten(ListNode head) {
        ListNode newList = null;
        while (head != null) {
            newList = append(newList, (ListNode) head.element);
            head = head.next;
        }
        return newList;
    }

    /*
    An iterative function that returns a boolean value, stating if the linked list is perfectly circular (not just
    cyclic). This method uses an iterative version of floyd's cycle-finding algorithm with a O(n) time complexity and
    O(1) space complexity. This algorithm entails moving through the list iteratively with a listNode that moves round
    quickly and one that moves round slowly. If at any point the slow listNode equals the large listNode the a loop must
    be present within the list. The method then checks if both the fast and slow and head listNode are all equal, since
    if the loop is cyclic and cycles right round back to the head of the list then it is perfectly circular. If at any
    point the fast listNode points to a value of null, then the the method returns false, since if the loop is cyclic,
    no endpoint should be able to be reached. Note that an empty list or single node list is not considered circular.
     */
    @Override
    public boolean isCircular(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;

            if (slow == fast) {
                return (fast == head); // If the list is cyclic from the head of the list then it is circular
            }
        }
        return false;
    }

    /*
    An iterative function that returns a boolean value, stating if the linked list is cyclic (has a loop in it). This
    method uses an iterative version of floyd's cycle-finding algorithm with a O(n) time complexity and O(1) space
    complexity. This algorithm entails moving through the list recursively with a listNode that moves round quickly and
    one that moves round slowly. If at any point the slow listNode equals the large listNode the a loop must be present
    within the list and so the method returns true. If at any point the fast listNode points to a value of null, then
    the the method returns false, since if the loop is cyclic, no endpoint should be able to be reached. Note that an
    empty list or single node list is not considered circular.
     */
    @Override
    public boolean containsCycles(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next; // Fast listNode skipping an element in the list each recursive step

            if (slow == fast) {
                return true;
            }
        }
        return false;
    }

    /*
    Using bubble sort for sorting as it is an iterative algorithm. Therefore this method has a time complexity of O(n^2).
    Whilst bubble sort has a worse average time complexity than merge and quick sort, it is easier to implement and runs
    with the iterative design of this class. Moreover its algorithmic simplicity means it is actually faster for smaller
    lists. This bubble sort algorithm involves swapping data rather than swapping references between nodes since this
    implementation is more simple.
     */
    @Override
    public ListNode sort(ListNode head, Comparator comparator) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode node1 = head;
        while (node1 != null) {
            ListNode node2 = node1.next;
            while (node2 != null) {
                // If the element in node 1 is greater than the element in node 2
                if (comparator.compare(node1.element, node2.element) > 0) {
                    Object curr = node1.element;
                    node1.element = node2.element; // Swapping element values between nodes, not references or nodes themselves
                    node2.element = curr;
                }
                node2 = node2.next;
            }
            node1 = node1.next;
        }
        return head;
    }

    /*
    An iterative map method that creates a new list that whereby each element has been transformed a certain way from its
    initial value in the first list. Each iteration a new listNode is created whose element has been transformed,
    this is then added to the end of the new list. If the end of the old array has been reached the recursive method
    returns the head to this new mapped list.
     */
    @Override
    public ListNode map(ListNode head, IMapTransformation transformation) {
        if (head == null) {
            return null;
        }
        ListNode oldCurr = head;
        ListNode newCurr = null;

        while (oldCurr != null) {
            // New listNodes ensure that the original input list is not effected by mapping
            newCurr = append(newCurr, new ListNode(transformation.transform(oldCurr.element)));
            oldCurr = oldCurr.next;
        }
        return newCurr;
    }

    /*
    An iterative reduce method that performs an operation to combine all elements in the list iteratively and add to
    all this an initial object to combine with. This combination is performed iteratively until the head variable is
    null and all of the list has been iterated through.
     */
    @Override
    public Object reduce(ListNode head, IReduceOperator operator, Object initial) {
        if (head == null) {
            return initial;
        }
        while (head != null) {
            initial = operator.operate(head.element, initial);
            head = head.next;
        }
        return initial;
    }

    /*
    An iterative filter method to remove elements from create a new list of elements that all meet a certain condition
    and filter out invalid elements. Each iteration if an element satisfies a condition in the old list it is appended
    to the new list. If the end of the old list has been reached, we return from the list node that is the head of the
    new filtered list.
     */
    @Override
    public ListNode filter(ListNode head, IFilterCondition condition) {
        ListNode newCurr = null;
        while (head != null) {
            if (condition.isSatisfied(head.element)) { // Add to new list if the element satisfies the condition
                newCurr = append(newCurr, new ListNode(head.element));
            }
            head = head.next;
        }
        return newCurr;
    }
}
