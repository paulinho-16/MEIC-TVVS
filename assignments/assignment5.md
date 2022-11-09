# Assignment 5 - Structural Testing: Line and Decision coverage (White-box Testing)

*Structural Testing* is a white-box testing technique used to test the internal design of the software or the structure of the coding for the particular software.
*Code coverage* is a metric that can help you understand how much of your source code is tested.
Measurement of *structural coverage* of code is an objective means of assessing the thoroughness of testing.
There are various industry-standard metrics available for measuring structural coverage, but in this report, we will explore only two of them:
- *Line Coverage*: how many of lines of source code have been tested
- *Branch Coverage*: how many of the branches of the control structures (if statements for instance) have been executed

To analyze the depth of our unit tests, we used the **JaCoCo** tool, a code coverage reports generator for **Java** projects.
The first step we took was to assess the coverage resulting from tests developed for previous assignments.
To configure this tool in our project, we had to modify the `pom.xml` file by adding the *JaCoCo* and *Surefire* plugins.

After a first analysis of the coverage of our tests, we developed more unit tests to get closer to 100% coverage (excluding GUI features).
So, we focused on the `project` and `misc` sub-packages, in addition to testing the main class, located in the `jtimesched` package.
We will explain the logic of each test, developed with the desired coverage in mind, in addition to discussing their outcome.

Finally, after reaching the desired depth, a final report on the project's coverage will be generated and discussed in the last section.

## 1) Initial Line and Branch Coverage

// TODO: prints da coverage antes de fazer testes, e descrever um pouco o coverage report

## 2) Unit Tests

// TODO: introdução a dizer quais as classes que decidimos testar e porquê

// TODO: no formatDate remover testes inúteis -> após perguntar ao stor o que está pinned

### Class X

// TODO: descrever unit testes para a class X
// TODO: outcome do testes, justificar se algum falhar

### Class Y

// TODO: descrever unit testes para a class Y
// TODO: outcome do testes, justificar se algum falhar

### Class Z

// TODO: descrever unit testes para a class Z
// TODO: outcome do testes, justificar se algum falhar

## 3) Final Line and Branch Coverage

// TODO: prints da coverage depois de fazer testes, e descrever um pouco o coverage report

-----

## Group 10

- Hugo Guimarães, up201806490
- Paulo Ribeiro, up201806505

## Sources

- [Class Slides - Prof. José Campos](https://paginas.fe.up.pt/~jcmc/tvvs/2022-2023/lectures/lecture-5.pdf)
- [Structural (Code) Coverage - QA Systems](https://www.qa-systems.com/blog/what-is-meant-by-structural-code-coverage/)
- [Coverage Counters - JaCoCo](https://www.eclemma.org/jacoco/trunk/doc/counters.html)