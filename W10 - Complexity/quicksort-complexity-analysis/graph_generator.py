import csv
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D
from matplotlib import cm
import numpy as np

sortedness = []
list_size = []
execution_time = []

# Must be same number of max swaps as in Quicksort
MAX_SWAPS = 200

with open('results.csv') as csv_file:
    csv_reader = csv.reader(csv_file, delimiter=' ')
    for row in csv_reader:
        list_size.append(float(row[0]))
        if (float(row[1])) == 0.0:
            print("Yes")
        sortedness.append(float(row[1]))
        execution_time.append(float(row[2]))

fig = plt.figure()
ax = Axes3D(fig)
ax.plot_trisurf(list_size, sortedness, execution_time, edgecolor='none', linewidth=0, cmap="viridis", zorder=1)

# Obtaining lists of the values when the number of swaps is at a maximum and a minimum, this is so lines to show how the
# quicksort algorithm scales up on larger arrays when the array is sorted or not can be drawn.
x_max, y_max, z_max, x_min, y_min, z_min = [], [], [], [], [], []
for i in range(len(list_size)):
    if i % MAX_SWAPS == 0:
        x_max.append(list_size[i])
        y_max.append(sortedness[i])
        z_max.append(execution_time[i])
    if i % MAX_SWAPS == MAX_SWAPS - 1:
        x_min.append(list_size[i])
        y_min.append(sortedness[i])
        z_min.append(execution_time[i])

ax.plot(x_max, y_max, z_max, label="$O(n^2)$", color="red", zorder=3, linewidth=4)
ax.plot(x_min, y_min, z_min, label="$O(nlog(n))$", color="green", zorder=3, linewidth=4)
ax.legend()
ax.set_xlabel('List Size')
ax.set_ylabel('Sortedness (edit distance)')
ax.set_zlabel('Execution Time (ms)')
plt.savefig('execution_time_vs_no_swaps_and_array_size_3d.png')
plt.show()

# Using largest array to draw 2d graph showing the relationship between sortedness and execution time.
sortedness_max = sortedness[len(sortedness) - 100:]
execution_time_max = execution_time[len(execution_time) - 100:]

plt.plot(sortedness_max, execution_time_max, color="purple")
plt.xlabel('Sortedness (no swaps)')
plt.ylabel('Execution Time (ms)')
plt.savefig('execution_time_vs_no_swaps.png')
plt.show()