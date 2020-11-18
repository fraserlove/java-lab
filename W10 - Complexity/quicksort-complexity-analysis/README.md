## Usage

The program can be compiled and ran using the following commands inside the `/src` directory.

    javac QuickSortComplexityAnalyser.java
    java QuickSortComplexityAnalyser
   
This will create a file called `results.csv` which will store all of the results generated
from the program. Note that the results generated will be for a forward sorted list, if 
the results for a reverse sorted list are required change the `ASC_SORT_ORDER` constant to
`false`. Further variables can be changed within the `QuickSortComplexityAnalyser` if required. 
For example, decreasing the `TIMED_ITERATIONS` may speed up the result generation on lower
end hardware. Note that with the default settings, on my computer the result generation finished
in 30 minutes. If this is taking significantly longer for you, I suggest decreasing the iterations
or decreasing the maximum size of the array in the `MAX_ARRAY_SIZE` variable.
 
 The python file `graph_generator.py` can then be ran using the command below. This generates
 the graph seen in the report.
 
     python3 graph_generator.py