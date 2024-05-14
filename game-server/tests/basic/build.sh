#!/bin/bash
javac -cp "$TESTDIR/javax.json-1.0.jar:." $(find . -name '*.java')
