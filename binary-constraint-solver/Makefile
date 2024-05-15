all: compile run

compile:
	javac src/*.java

run:
	java -cp src/ SolveCSP $(args)

clean:
	find . -type f -name '*.class' -exec rm {} +

.PHONY: compile run analysis clean