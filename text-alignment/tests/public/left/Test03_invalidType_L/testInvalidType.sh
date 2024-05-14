#!/bin/bash
PROGOUT=$( java TextAlignment "$TESTDIR/../../../pratchett.txt" cat 80 )

if [[ $PROGOUT != "usage: java TextAlignment <filename> <alignmentType> <lineLength>" ]]; then
  echo "Expected 'usage: java TextAlignment <filename> <alignmentType> <lineLength>'"
  echo "Your program printed '$PROGOUT'"
  exit 1
fi;
