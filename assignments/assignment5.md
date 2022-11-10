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

Each test was developed with the desired coverage in mind, and we tried to make the most of **JUnit**'s features, which will be described in the second section.
As for the naming of test methods, we follow a *MethodName_StateUnderTest_ExpectedBehaviour* approach.

Finally, after reaching the desired depth, a final report on the project's coverage will be generated and discussed in the last section.

## 1) Initial Line and Branch Coverage

Aqui se calhar deviamos correr os testes de novo no commit inicial, por causa dos teste da GUI que excluímos

// TODO: prints da coverage antes de fazer testes, e descrever um pouco o coverage report

## 2) JUnit features explored

// TODO: introdução a dizer quais as classes que decidimos testar e porquê

// TODO: no formatDate remover testes inúteis -> após perguntar ao stor o que está pinned

### Feature X

// TODO: descrever feature X

One of the main goals of testing is to compare the *expected value* to the *actual value* of a code partition.
To perform this comparison, the *JUnit* *Assertions* library provides the following methods:
- `static void assertTrue(boolean condition)` - Asserts that the provided *condition* is true
- `static void assertFalse(boolean condition)` - Asserts that the provided *condition* is false
- `static void assertEquals(Object Expected, Object Actual)` - Asserts that *expected* and *actual* are equal. Both arguments must be of the same type
- `static <T extends Throwable> assertThrows(assertThrows(Class<T> expectedType, Executable executable)` - Asserts that the execution of the supplied executable throws an exception of te expectedType and returns the exception
- `static void assertAll(Executable... executables)` - Asserts that all supplied executables do not throw exceptions


#### 1) **assertTrue** && **assertFalse**
These methods were used when the evaluated result's variable type was **boolean**


#### 2) **assertEquals**
This method was used when the valur of two variables of non-boolean type were being compared. The `assertSame()` method could also be used to compare *primitives*, given that it uses the '==' operator instead of the *equals* method. For simplicity's sake and to avoid unnecessary mistakes, only assertEquals was used.

https://stackoverflow.com/questions/1201927/is-javas-assertequals-method-reliable

#### 3) **assertThrows**
This method allowed testing code segments where a specific exception should be thrown, testing the code's behaviour in error scenarios.

#### 4) **assertAll**
This method allows running the aforementioned *assertions* inside a single statement. It always checks all of the assertions that are passed to it, no matter how many fail. This means that if at least one fails, a detailed result of all test is prompted. It is best used for asserting a test set of properties that belong together, such as the fields of a non primitive object with no predefined *equals* and *hashCodes* methods.

https://stackoverflow.com/questions/40796756/assertall-vs-multiple-assertions-in-junit5

### Feature Y

// TODO: descrever feature Y

- `@Test`- This tag was used to indicate that the ensuing code segment must be interpreted as a *Java* test
- `@BeforeEach` - This tag was used to always run a code segment before every test within the same scope
- `@Nested` - This tag was used to form logical groups of test cases while also improving the organization of big test classes
- `@ParameterizedTest`- This feature enables executing a single test method multiple times with different parameters
    - `(name = "...")` - Parameterized test can have a name displayed on the *Debug Console* after execution. Also, the number of the parametrized test can be passed, as well as its arguments, providing distinction between each test instance to improve readability
    - `@ValueSource` - Enables the specification of a single array of literal values and can only be used for providing a single argument per parametrized test invocation
    - `@CsvSource` - Expresses arguments lists as comma-separated values
    - `@MethodSource`- Used to match an existing method, such as a `static Stream<Arguments>`, to allow passing complex objects as arguments.
- `@DisplayName`- Provides a name to the test, improving readability while debugging.



https://stackoverflow.com/questions/36220889/whats-the-purpose-of-the-junit-5-nested-annotation

https://www.baeldung.com/parameterized-tests-junit-5


https://junit.org/junit5/docs/current/user-guide/#writing-tests-parameterized-tests-sources-ValueSource

### Feature Z

// TODO: descrever feature Z

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