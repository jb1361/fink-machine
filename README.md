### Requirements
* [Java 8+ jdk](https://www.oracle.com/technetwork/java/javaee/downloads/jdk8-downloads-2133151.html)
* [Python 3](https://www.python.org/download/releases/3.0/)
* [IntelliJ](https://www.jetbrains.com/idea/) - This project was built while using IntelliJ so all installation and project setup assumes you are also using it. You may have a bad time if you don't.
* [Java FX Scene Builder](https://www.oracle.com/technetwork/java/javase/downloads/javafxscenebuilder-info-2157684.html)

### Building
To Build the jar you go to Build->Build Artifacts. This will create the jar file in the out directory.

### Running 

To run the jar file by passing in a file you run `java -jar jarname.jar fsmname.fsm outputfilename.py`

There is a sample fsm file in the fsm-files directory.
Currently only python is supported as an output file-type so make sure to pass in a .py file as the second argument.