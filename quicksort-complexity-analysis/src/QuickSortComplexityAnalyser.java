import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class QuickSortComplexityAnalyser {

    private static final int WARMUP_ITERATIONS = 10;
    // The higher the number of iterations the more accurate the average quicksort execution time will be, as this will
    // gradually decrease the effect that significant long runs due to background processes have in the overall average
    // calculated, reducing the overall error.
    private static final int TIMED_ITERATIONS = 500;
    private static final int MIN_ARRAY_SIZE = 0;
    private static final int MAX_ARRAY_SIZE = 5000;
    // The max number of swaps the array should receive, this could be the same value as the array size, however this
    // increases the number of swaps needed to be performed to each array size and so increases performance. The general
    // relationship between the max swaps and execution time can be found for values of max swaps that are significantly
    // smaller than the maximum array size.
    private static final int MAX_EDIT_DISTANCE = 100;
    private static final int MIN_EDIT_DISTANCE = 0;
    // Used to make sure results are of a specific length, and allowing for lists to iterate through to the max edit
    // distance and max array size with a step so that a certain number (resolution) of results is calculated for each.
    private static final int RESULTS_RESOLUTION = 50;
    private static final boolean ASC_SORT_ORDER = false;

    private static Random random = new Random();

    /*
    Generates an array of sequential integers with no duplicates. The size n can be specified. Elements can range from a
    value of 0 to n - 1 if ascending or from n - 1 to 0 if descending.
     */
    private static int[] generateSequentialIntegers(int n, boolean ascending) {

        int[] array = new int[n];
        for (int i = 0; i < n; i++) {
            if (ascending) {
                array[i] = i;
            }
            else {
                array[i] = n - i - 1;
            }
        }
        return array;
    }

    private static int[] generateSequentialIntegers(int n) {
        return generateSequentialIntegers(n, true);
    }

    /*
    Swaps two random elements a total of n times to shuffle the array and increase edit distance. This shuffling allows for
    a sorted array to be changed into an array with a specified edit distance, since it can specify the minimum number
    of swaps needed for it to be sorted back into a sorted state. There is a small chance that the same element will be
    chosen twice randomly (1/n^2 chance) or that a swap will undo the swap previous to it and lead to the edit distance
    not increasing at all (1/n^4 chance), however the probability of this happening is very small and decreases with
    the size of the array. Therefore for suitably large arrays, this is not a significant issue.
     */
    private static int[] increaseEditDistance(int[] array, int editDistance) {

        // Creating copy of the input array, so that it isn't shuffled.
        int[] shuffled = Arrays.copyOf(array, array.length);
        for (int i = 0; i < editDistance; i++) {
            if (array.length > 0) {
                swap(shuffled, random.nextInt(shuffled.length), random.nextInt(shuffled.length));
            }
        }
        return shuffled;
    }

    /*
    Swaps two elements at chosen indices in the array.
     */
    private static void swap(int[] array, int idx1, int idx2) {

        int temp = array[idx1];
        array[idx1] = array[idx2];
        array[idx2] = temp;
    }

    /*
    A non in-place implementation of Quicksort that uses the last element in the array as the pivot. This leads to a
    time complexity on average of O(nlog(n)) and a worst case time complexity of O(n^2) if used on an already sorted
    array. This implementation has a O(nlog(n)) space complexity since it is not in-place and extra arrays are need to
    temporarily store elements.
     */
    private static void quicksort(int[] array) {

        if (array.length <= 1) {
            return;
        }
        // Pivot set to last element in the array (causes rise of pathological cases)
        int pivot = array[array.length - 1];

        // Since using arrays, have to precalculate sizes before initialising
        int leftSize = 0;
        int rightSize = 0;
        for (int i = 0; i < array.length - 1; i++ )
            if ( array[i] <= pivot )
                leftSize++;
            else
                rightSize++;

        int[] left  = new int[leftSize];
        int[] right = new int[rightSize];

        // Divide step, moving values into new arrays
        int leftIdx = 0;
        int rightIdx = 0;
        for (int k = 0; k < array.length - 1; k++) {
            // Elements smaller than the pivot are move to the left of the pivot and elements greater than the pivot
            // are moved to the right of the pivot.
            if (array[k] <= pivot) {
                left[leftIdx++] = array[k];
            }
            else {
                right[rightIdx++] = array[k];
            }
        }

        // Recurse and conquer step, sorting the left and right arrays, then when they are returned merging them into
        // one larger array in the order of less, equal then greater.
        quicksort(left);
        quicksort(right);

        int arrayIdx = 0;
        for (int i = 0; i < left.length; i++) {
            array[arrayIdx++] = left[i];
        }
        array[arrayIdx++] = pivot;
        for (int i = 0; i < right.length; i++) {
            array[arrayIdx++] = right[i];
        }
    }

    /*
    Finds the mean execution time in ms when sorting the provided array. The mean value is taken over a certain number of
    iterations. The method tries to remove as much error and produce as accurate results as possible. This is done using
    the following:
      - warm-up iterations to optimise JVM code and case usage before the real test is done.
      - many timed iterations to average out the results and decrease the factor that the smallest and largest times have
        on the final result.
      - only timing the time that quicksort is being performed and nothing else, so the timing is as accurate as possible.
      - shuffling the array differently each iteration, but for the same number of swaps (edit distance). This ensures
        that the timings obtained are not from a rare bad case scenario where a terrible swap was performed or vice versa.
        These are averaged out among all the possible swaps that could occur.
      - discarding the lowest and highest 10th percentile results so that only the most accurate results are left, since
        values significantly smaller or larger than the median are removed.
     */
    private static double findMeanSortTime(int[] array, int swaps) {

        long startTime;
        List<Long> times = new ArrayList<>();

        // Warm up iterations to allow for JVM to optimise code and cache usage, increasing timing accuracy.
        for (int i = 0; i < WARMUP_ITERATIONS; i++) {
            quicksort(increaseEditDistance(array, swaps));
        }

        for (int i = 0; i < TIMED_ITERATIONS; i++) {
            // Each iteration, shuffling the array by swapping a certain number of times.
            int[] unsorted = increaseEditDistance(array, swaps);

            // Timing only the quicksort method to be as accurate as possible.
            startTime = System.nanoTime();
            quicksort(unsorted);
            times.add(System.nanoTime() - startTime);

            // Checking each iteration if quicksort has worked correctly and produced a sorted array. This is done by
            // comparing the sorted array to the generated sequential array.
            if (!Arrays.equals(unsorted, generateSequentialIntegers(array.length))) {
                System.out.println("Error: quicksort did not correctly sort the array.");
            }
        }

        // using java standard library, since will have a more optimised sorting algorithm.
        Arrays.sort(times.toArray());
        int bounds = TIMED_ITERATIONS / 10;
        long sum = 0;
        for (int i = 0; i < times.size(); i++) {
            // discarding the lowest 10% and highest 10% of execution times to obtain most accurate results.
            if (i >= bounds && i <= times.size() - bounds) {
                sum += times.get(i);
            }
        }
        // Return the new average from the median 80% of values left and convert from ns to ms
        return sum / (1e6 * (TIMED_ITERATIONS - (bounds * 2)));
    }

    /*
    Runs findMeanSortTime() for an increasing number of swaps (edit distance) and array size. The array size, number of
    swaps and the mean sort time are stored within a list which is to be written to a csv file for graphical analysis.
    The method iterates through 1% of the max array size and number of swaps each step, this reduces the number of data
    points over the entire range of array lengths and number of swaps, increasing performance.
     */
    private static void generateSortingResults() {

        List<Double[]> results = new ArrayList<>();

        for (int arraySize = MIN_ARRAY_SIZE; arraySize < MAX_ARRAY_SIZE; arraySize += MAX_ARRAY_SIZE / RESULTS_RESOLUTION) {
            for (int swaps = MIN_EDIT_DISTANCE; swaps < MAX_EDIT_DISTANCE; swaps += MAX_EDIT_DISTANCE / RESULTS_RESOLUTION) {
                Double[] result = {(double) arraySize, (double) swaps, findMeanSortTime(generateSequentialIntegers(arraySize, ASC_SORT_ORDER), swaps)};
                results.add(result);
            }
            System.out.println("Generating Results: (" + arraySize + "/" + MAX_ARRAY_SIZE + ")");
        }
        writeToCSV(results);
    }

    /*
    Writes the results obtained to a csv file where they can then be analysed graphically.
     */
    private static void writeToCSV(List<Double[]> results) {

        try {
            FileWriter csvWriter = new FileWriter("results.csv");

            for (Double[] result : results) {
                for (double value : result) {
                    csvWriter.append(value + " ");
                }
                csvWriter.append("\n");
            }
            csvWriter.flush();
            csvWriter.close();
        } catch (IOException ioException) {
            System.out.println("File writer produced error when trying to write to csv - " + ioException.getMessage());
        }

    }

    /*
    Runs the method to start generating sorting results (i.e edit distance, array size and execution time of Quicksort)
     and displays the total time taken to finish generating results.
     */
    public static void main(String[] args) {

        long startTime = System.nanoTime();
        generateSortingResults();
        System.out.println("Quicksort Result Generation Duration: " + (double) Math.round(((System.nanoTime() - startTime) / 1e7)) / 100 + "s");
    }
}