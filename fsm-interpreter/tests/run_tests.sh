#!/bin/bash

# Get the directory where the script is located
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# Directory where your Java program and test files are located
PROGRAM_DIR="$(dirname "$SCRIPT_DIR")/src"  # Set this to the parent directory of the script
TESTS_DIR="$(dirname "$SCRIPT_DIR")/tests"

# Compile your Java program (if needed)
javac -cp $PROGRAM_DIR "$PROGRAM_DIR/FSMInterpreter.java"

# Loop through each test subdirectory
for test_dir in "$TESTS_DIR"/*; do
    if [ -d "$test_dir" ]; then
        description_file="$test_dir/description.fsm"
        input_file="$test_dir/input.txt"
        expected_output_file="$test_dir/expected.out"

        # Run the Java program with description.fsm as input and input.txt as standard input
        actual_output=$(java -cp "$PROGRAM_DIR" FSMInterpreter "$description_file" < "$input_file")

        # Read the expected output
        expected_output=$(<"$expected_output_file")

        # Compare actual and expected output
        if [ "$actual_output" = "$expected_output" ]; then
            echo "Test $test_dir: PASSED"
        else
            echo "Test $test_dir: FAILED"
        fi
    fi
done
