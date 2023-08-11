# Singly Linked List Implementation

A dual iterative and recursive implementation of singly linked lists in Java. These implementations should follow the abstract data type `IListManipulator`, the methods of which are outlined below:
 - `size(ListNode head)`
 - `contains(ListNode head, Object element)`
 - `count(ListNode head, Object element)`
 - `convertToString(ListNode head)`
 - `getFromFront(ListNode head, int n)`
 - `getFromBack(ListNode head, int n)`
 - `equals(ListNode head1, ListNode head2)`
 - `containsDuplicates(ListNode head)`
 - `append(ListNode head1, ListNode head2)`
 - `reverse(ListNode head)`
 - `split(ListNode head, int n)`
 - `flatten(ListNode head)`
 - `isCircular(ListNode head)`
 - `containsCycles(ListNode head)`
 - `sort(ListNode head, Comparator comparator)`
 - `map(ListNode head, IMapTransformation transformation)`
 - `reduce(ListNode head, IReduceOperator operator, Object initial)`
 - `getFromFront(ListNode head, IFilterCondition condition)`

 Notes about implementation:
- Apart from specific instances whereby the `IListManipulator` specified
that a method should not alter the original list, all methods in both the
iterative and recursive implementations do not alter the original list(s),
whose head(s) is/are passed in.
- The `flatten()` method should only be expected to flatten a list of lists
into one list and not any deeper list structures (i.e. list of lists of lists).
- Only the `isCircular()` and `containsCycles()` methods should deal with
cyclic lists. All other methods should expect linear linked lists.
- The `IMapTransformation`, `IReduceOperator` and `IFilterCondition` ar-
guments passed into the respective `map()`, `reduce()` and `filter()` meth-
ods will be designed to work with the type of objects in the input list.
- A list with zero or one nodes cannot be reversed, split, considered cyclic
or considered circular.
- A list with no nodes can already be considered flattened, however a list of
one list can still be flattened into a list.