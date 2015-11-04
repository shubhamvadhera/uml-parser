# uml-generator-java
A Parser which converts Java Source Code into a  UML Diagram
***
###Compile Instructions

####Requirements:
- Java JDK version 1.8
- Working internet connection (only for class diagram)

The program expects following arguments:

1. Keyword:
  - One word string.
  - “class” for generating class diagram and keyword “seq” for generating a sequence
diagram.


2. Path:
  - String, may contain spaces, must be enclosed in double quotes.
  - Full path of the folder which contains all the .java source files. The program picks only
the .java files and ignores other files.
  - Ex - "C:\Users\uml-generator-java\Test Classes\class-diagram-test-1"

3. Name of output file
  - One word string
  - File name of the output png file. The file will be created at the same folder as Path given
in second argument.
  - Do not include extension along with the file name, the program will generate a PNG file.
  - Ex – diagram

Example:-
To generate class diagram

java -jar umlparser.jar class "C:\Users\uml-generator-java\Test Classes\class-diagram-test-1" diagram

*^creates diagram.png*

For sequence diagram, additional 2 arguments are required before the name of output file argument

4. Class Name
  * Class Name in which the method resides for which the sequence diagram is needed
  * Ex: Customer

5. Method Name
  - Method Name for which the sequence diagram is needed
  - Do not include parenthesis, brackets after function name
  - Ex - depositMoney


To generate sequence diagram

java -jar umlparser.jar seq "C:\Users\uml-generator-java\Test Classes\sequence-diagram-test-1" Customer depositMoney diagram

*^creates diagram.png*
***
###Details of libraries and tools used


There are 2 parts of this UML parser program:

- Parser – The parser reads all the java source code in the provided source path, and creates
a grammar language that is interpretable by the UML generator

- UML Generator – This part just generates a diagram as per the input provided

Parser:
For parsing the JAVA code into a usable grammar, I have used the javaparser library:
https://github.com/javaparser/javaparser

The library provides various methods and classes that read the source code and provide access to
each sub-unit of the code via various methods or classes.

UML Generator:
For generating the class diagram from the parsed code returned by the parser, I have used yUML beta.
http://yuml.me/

yUML is a free online tool for creating and publishing UML diagrams, which currently supports
class and use case diagrams.

The program fires a GET query to the URL : http://yuml.me/diagram/plain/class/<Grammar>
and gets the diagram. Thus, an internet connection is needed for the program to work.

For generating the sequence diagram, plantUML is integrated in the code: http://plantuml.com/
