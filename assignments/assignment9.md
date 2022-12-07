# Assignment 9 - Mutation Testing (White-box Testing)

*Mutation Testing* is a type of software testing in which certain statements of the source code are changed/mutated to check if the test cases are able to find source code errors.
Its goal is to ensure the quality of test cases in terms of robustness so that they should fail the mutated source code.
The changes made in the mutant program should be kept extremely small so that it does not affect the program's overall objective.
A **mutation** is nothing but a single syntactic change that is made to the program statement.
If the tests fail, the **mutation** is killed; if your tests pass, the **mutation** survives.
The quality of the tests can be gauged from the percentage of **mutations** killed.

Mutants that cannot be killed are called **equivalent mutants**.
An **equivalent mutant** is a mutant that always behaves the same way as the original program.
If the mutant behaves like the normal code, it will always give the same output as the original program for any given input.
Clearly, this makes this mutant (which is basically the same program as the one under test) impossible to be killed by the tests.

To assess the robustness of our tests, we used the **Pitest** tool, a mutation testing system.
The first step we took was to configure **Pitest** in our project.
For this, we had to update the *JUnit jupiter* plugins version to 5.9.1 and the *maven-surefire-plugin* version to 3.0.0-M7, in addition to removing the *junit-platform-surefire-provider* plugin.
Then, we needed to add the **Pitest** plugin, including a dependency that would support **JUnit 5**.
Lastly, we had to exclude some test classes, namely the main and GUI classes, since it was not our goal to test them.

Upon setting up this tool, we executed an initial **mutation analysis** by running the command `mvn test-compile org.pitest:pitest-maven:mutationCoverage`.
After analyzing the results, we first identified the **equivalent mutants** and only then created more unit tests to kill the remaining mutants.
Both of these steps are described in the following sections.
Finally, this report ends with the execution of the final **mutation analysis** after we have developed these tests.

## 1) Initial Mutation Score

// TODO: ("Analyze which mutants survive to your unit test cases and which parts of the source code has the most not-killed mutants") -> Isto não devia ser na secção dos equivalent mutants?



We first need to view our initial Pit Test Coverage report to start mutation testing.
Thus, we decided to exclude the tests associated with the GUI, done through the `pom.xml` file, resulting in the following coverage:
![Initial Mutation Score](./images/mt_initial_score.png)

Since we performed extensive testing in previous assignments, either with black-box and white-box testing techniques, the initial *Pit Test Coverage Report* contains:

- Line Coverage - 99%
- Mutation Coverage - 80%
- Test Strength - 80%

Looking at the previous figure, we can see that several mutants were already "killed" by the tests performed in previous assignments.

The mutants that still need to be addressed are all inside the `jtimesched.project` directory, whose class breakdown can be better visualized in the following figure:

![Initial Mutation Score](./images/mt_initial_score2.png)

The goal of this assignment is to increase the scores of the report, covering all mutation cases.

## 2) Equivalent Mutants

### In file `Project.java`

#### Lines 163 & 177

The first mutants we found consisted of removing calls to the `printStackTrace` method from exceptions thrown in the code.
These exceptions would only happen if there was an attempt to get the elapsed seconds of a project that is not running.
The mutants survived, as removing the calls had no effect on the program's behavior.
The main cause of this is essentially due to the fact that this scenario never happens, as it is prevented by an `if` statement performed before calling the method that could throw them.
This verification ensures that no exception is thrown, so the statement at hand is never executed.

![First Equivalent Mutants](./images/mt_equivalent_mutant1.png)

### In file `ProjectSerializer.java`

#### Line 63

This next mutant also consists of removing a method call, namely a method responsible for defining the number of spaces used in the indentation of the XML file to be written.
The fact that the mutant survives, despite correctly testing by reading the resulting XML, suggests that this call is not fundamental to the program at all.
We also noticed that the code contained an author comment with a link to a given bug in a bug database ([Bug Link/ID](http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6296446)).
After studying the subject, we concluded that, despite the default indentation value being already 4, the author was forced to make this function call due to the aforementioned bug, which is currently fixed.
Therefore, this call is dispensable, resulting in an **equivalent mutant**.

![Second Equivalent Mutant](./images/mt_equivalent_mutant2.png)

#### Line 67

This next mutant consisted of removing a call to a function that defines properties of the XML file to be written, more precisely the type of encoding.
Since the defined value is the default value for reading the XML files, the presence of this function call also turns out to be unnecessary, thus resulting in an **equivalent mutant**.
(ENCODING default value = "encoding" = "UTF-8", sources = [Purpose](https://docs.oracle.com/javase/7/docs/api/javax/xml/transform/OutputKeys.html), [Default Values](https://docs.oracle.com/javase/7/docs/api/constant-values.html))

![Third Equivalent Mutant](./images/mt_equivalent_mutant3.png)

#### Lines 71 & 111

The following mutants also consist of removing function calls.
More specifically, the invoked functions serve to notify the start and end of a document, respectively.
However, both are optional, having no impact on the program's behavior, hence these cases are considered **equivalent mutants**.
(source = [Purpose](https://docs.oracle.com/javase/7/docs/api/org/xml/sax/ContentHandler.html), [Optionality](https://stackoverflow.com/a/4267492))

![Fourth Equivalent Mutants](./images/mt_equivalent_mutant4.png)

#### Lines 87, 92 & 99

The next three mutants result from removing a call responsible for clearing the list of attributes for reuse, freeing up little memory.
Since this call does not affect the behavior of the program, serving only for memory management purposes, this case is an **equivalent mutant**.
(source = [Purpose](https://docs.oracle.com/javase/7/docs/api/org/xml/sax/helpers/AttributesImpl.html#clear()))

![Fifth Equivalent Mutants](./images/mt_equivalent_mutant5.png)

#### Lines 113 & 114

Similarly to the last mutants, these result from removing calls to functions related to memory management.
The first is responsible for flushing the stream, and the second for closing it.
Thus, as they do not interfere with the behavior of the program, they constitute **equivalent mutants**.
(sources = [Flush](https://www.tutorialspoint.com/java/io/outputstreamwriter_flush.htm), [Close](https://www.tutorialspoint.com/java/io/outputstreamwriter_close.htm))

![Sixth Equivalent Mutants](./images/mt_equivalent_mutant6.png)

#### Line 193

The reason for this mutant is the same as the previous ones: removal of a call to a function, in this case to *Java*'s print function.
Logically, this function has no interference with the output of the program, so its presence is insignificant, thus resulting in an **equivalent mutant**.

![Seventh Equivalent Mutant](./images/mt_equivalent_mutant7.png)

// TODO: ProjectTableModel.java -> estas serão Equivalent? A 205 foi corrigida... (linhas 210, 218)

## 3) Unit Tests

// TODO: Brief description of test cases developed to increase project’s mutation score.

Project.java -> matamos aqueles 3 mutantes dos times:

Linha 185 -> Project.java
Linha 193 -> Project.java
Linha 201 -> Project.java

### In file `ProjectSerializer.java`

#### Line 69

This next mutant consisted of removing a call to a function that defines properties of the XML file to be written, more precisely the presence of indentation.
To kill it, we verified the indentation of the XML file by checking the presence of four consecutive spaces.
In the absence of the function call, the XML is written without any indentation, leading to test failure.

// TODO: imagem antes/depois do mutation score
// TODO: excerto do código do teste que aplica isto

ProjectSerializer.java -> matamos o mutante ao verificar a versão no teste do writeXML (linha 74)
ProjectSerializer.java -> matamos o mutante das quotas no writeXML (linha 95)
ProjectSerializer.java -> matamos o mutante do time started (linha 146)
ProjectSerializer.java -> matamos o mutante do quota overall e quota today (linha 165, 167)
este mutante foi morto ao longo do processo... não mencionar?: Linha 204 -> ProjectSerializer.java (quando attributes é null, por default é considerado como um AttributesImpl vazio - If there are no attributes, it shall be an empty Attributes object - https://docs.oracle.com/javase/7/docs/api/org/xml/sax/ContentHandler.html#startElement(java.lang.String,%20java.lang.String,%20java.lang.String,%20org.xml.sax.Attributes))

ProjectTableModel.java -> matamos o mutante ao adicionar testes ao logger (linha 160, 183, 187, 205 -> este acho que é killed porque notifica os listeners, que envolvem logs)
ProjectTableModel.java -> matamos o mutante ao adicionar teste que verifica se o printstackstrace foi printed (linha 191, 192)

// TODO: ProjectTime.java -> o construtor private não deve ser para testar, certo?

## 4) Final Mutation Score

// TODO: descrever score final de Mutation

After performing mutation testing, we ended up with the following code coverage:

// TODO: Add the final score image
![Final Mutation Score](./images/mt_final_score.png)

Therefore, we were able to improve:
- *Line Coverage* from 99% to &&%
- *Mutation Coverage* from 80% to &&%
- *Test Strength* from 80% to &&%

Thus, we reached test coverage values above &&%, making the *JTimeSched* program more robust and error-free.

The remaining score that prevented us from reaching 100% is associated with the Equivalent Mutants that can't be killed, which were thoroughly explained in section 2 of the assignment.

-----

## Group 10

- Hugo Guimarães, up201806490
- Paulo Ribeiro, up201806505

## Sources

- [Class Slides - Prof. José Campos](https://paginas.fe.up.pt/~jcmc/tvvs/2022-2023/lectures/lecture-8.pdf)
- [Mutation Testing - Guru99](https://www.guru99.com/mutation-testing.html)
- [Pitest - Pitest](http://pitest.org/)
- [Testing Logging - Effective Agile](https://effectiveagile.com/testing-and-handling-logging-in-java/)
- [Constant Values - Oracle](https://docs.oracle.com/javase/7/docs/api/constant-values.html)
- [OutputKeys - Oracle](https://docs.oracle.com/javase/7/docs/api/javax/xml/transform/OutputKeys.html)
- [AttributesImpl - Oracle](https://docs.oracle.com/javase/7/docs/api/org/xml/sax/helpers/AttributesImpl.html)
- [ContentHandler - Oracle](https://docs.oracle.com/javase/7/docs/api/org/xml/sax/ContentHandler.html)