import csv
import matplotlib
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D
from matplotlib import cm

matplotlib.rcParams.update({'font.size': 14})

edit_distances, array_sizes, execution_times = [], [], []

# Must be same value as used within the Java QuickSortComplexityAnalyser class
RESULTS_RESOLUTION = 50

with open('results.csv') as csv_file:
    csv_reader = csv.reader(csv_file, delimiter=' ')
    for row in csv_reader:
        array_sizes.append(float(row[0]))
        edit_distances.append(float(row[1]))
        execution_times.append(float(row[2]))

# Obtaining arrays of the values when the number of swaps is at a maximum and a minimum, this is so lines to show how the
# quicksort algorithm scales up on larger arrays when the array is sorted or not can be drawn.
x_max, y_max, z_max, x_min, y_min, z_min = [], [], [], [], [], []
for i in range(len(array_sizes)):
    if i % RESULTS_RESOLUTION == 0:
        x_max.append(array_sizes[i])
        y_max.append(edit_distances[i])
        z_max.append(execution_times[i])
    if i % RESULTS_RESOLUTION == RESULTS_RESOLUTION - 1:
        x_min.append(array_sizes[i])
        y_min.append(edit_distances[i])
        z_min.append(execution_times[i])

# Creating 3D graph
fig = plt.figure()
ax = Axes3D(fig)
ax.plot_trisurf(array_sizes, edit_distances, execution_times, edgecolor='none', linewidth=0, cmap=cm.inferno, zorder=1, antialiased=False)
ax.plot(x_max, y_max, z_max, label="$O(n^2)$", color="#f20089", zorder=3, linewidth=3)
ax.plot(x_min, y_min, z_min, label="$O(nlog(n))$", color="#8900f2", zorder=3, linewidth=3)
ax.set_xlabel('Array Size')
ax.set_ylabel('Sortedness (edit distance)')
ax.set_zlabel('Execution Time (ms)')
plt.legend()

# Adjusting positioning of graph for images
fig.set_size_inches(12.5, 12)
ax.view_init(elev=20, azim=-245)
fig.subplots_adjust(left=0, right=1, bottom=0, top=1)
plt.tight_layout()
plt.savefig("sortedness_vs_time_vs_size.png", pad_inches=0)
ax.view_init(elev=-3, azim=98)
plt.savefig("sortedness_vs_time_vs_size_2.png", pad_inches=0)