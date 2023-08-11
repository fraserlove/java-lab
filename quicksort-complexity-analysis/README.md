# QuickSort Complexity Analysis

This project investigates the complexity of pathological cases surrounding the Quicksort sorting algorithm. This Quicksort implementation must be a not in-place implementation with the pivot being chosen as the last element of the list, a pathological case. The metric used to describe the sortedness of a list was chosen to be the edit distance, (minimum number of swaps needed to sort the list). This program generates lists according to this metric. The time it takes for Quicksort to sort generated lists of varying sortedness is then recorded and displayed as a graph. This graph shows the relation between sortedness and the execution time of Quicksort


### Usage
The program can be compiled and ran using the following commands inside the `/src` directory.
1. **Compile Java from Source:**
```bash
javac QuickSortComplexityAnalyser.java
```
2. **Run the Compiled Program:**

Include the path to a valid `.fsm` and an input `.txt` file as follows:
```bash
java QuickSortComplexityAnalyser
```
   
This will create a file called `results.csv` which will store all of the results generated
from the program. Note that the results generated will be for a forward sorted list, if 
the results for a reverse sorted list are required change the `ASC_SORT_ORDER` constant to
`false`. Further variables can be changed within the `QuickSortComplexityAnalyser` if required. 
For example, decreasing the `TIMED_ITERATIONS` may speed up the result generation on lower
end hardware. Note that with the default settings, on my computer the result generation finished
in 30 minutes. If this is taking significantly longer for you, I suggest decreasing the iterations
or decreasing the maximum size of the array in the `MAX_ARRAY_SIZE` variable.
 
 The python file `graph_generator.py` can then be ran using the command below to display a graph showing the relation between array size, sorted ness and the execution time of Quicksort.

```bash
python graph_generator.py
```