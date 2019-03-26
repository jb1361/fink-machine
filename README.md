### Requirements
* Java 8+ sdk
* Python 3
* IntelliJ Ide (can use others but this readme assumes you are using IntelliJ)
* [Java FX Scene Builder](https://www.oracle.com/technetwork/java/javase/downloads/javafxscenebuilder-info-2157684.html)

### Building
To Build the jar you go to Build->Build Artifacts. This will create the jar file in the out directory.

### Running

To run the jar file by passing in a file you run `java -jar jarname.jar fsmname.fsm outputfilename.py`

There is a sample fsm file in the fsm-files directory.
Currently only python is supported as an output file-type so make sure to pass in a .py file as the second argument.