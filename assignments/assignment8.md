# Assignment 8 - Dataflow Testing (White-box Testing)

*Dataflow testing* is used to analyze the flow of data in the program.
It is the process of collecting information about how the variables flow the data in the program, focusing on each particular point in the process.
It consists of a group of testing strategies to examine the control flow of programs in order to explore the sequence of variables according to the series of events.
It mainly focuses on the points at which values are assigned to the variables and the points at which these values are used.
By concentrating on both points, data flow can be tested.
*Dataflow testing* uses the control flow graph to detect illogical things that can interrupt the flow of data.

There are five concepts to highlight in this type of testing:
- **def**: definition of a variable - it's the occurrence of a variable when a value is bound to it
- **c-use**: computation use - when the value of a variable is used to compute a value for output or for defining another variable
- **p-use**: predicate use - when the value of a variable is used to decide an execution path
- **def-clear path**: definition clear path - path with respect to a variable `v` if it has no variable re-definition of `v` on the path
- **def-use pair**: definition-use pair - pair (`d`,`u`) with respect to a variable `v` where `d` is a node defining `v`, `u` is a node or edge using `v`, and there is a **def-clear path** with respect to `v` from `d` to `u`

In this type of testing, there are different types of coverage criteria. In this report, we will focus on the following:
- **all-defs**: achieved as there is at least one **def-clear path** from every definition of each variable to at least one **c-use** or **p-use** of each variable
- **all-c-uses**: achieved when, for every program variable, at least one **def-clear path** from every definition of that variable to every **c-use** of it must be covered
- **all-p-uses**: achieved when, for every program variable, at least one **def-clear path** from every definition of that variable to every **p-use** of it must be covered
- **all-uses**: achieved when, for every program variable, at least one **def-clear path** from every definition of that variable to every **c-use** and every **p-use** (including all outgoing edges of the predicate statement) of it must be covered - requires that all **def-use pairs** are covered

To try out this technique, we selected three different functions of the *jTimeSched* project.
For each one, we present the reason we decided to test it and its purpose.
Then, we show the **CFG** (control-flow graph) of the function, from which we also build the **def-use graph** (dataflow graph), which captures the flow of definitions (also known as defs) across basic blocks in a program.
After that, we apply *Dataflow Testing* to each variable of that function, displaying a tabular summary with the **def-use pairs** for each.
The last step is to define all paths for each coverage criteria: **all-defs**, **all-c-uses**, **all-p-uses**, and **all-uses**.
Finally, we describe the unit tests we created based on this method and their outcomes.

As for the naming of test methods, we follow a *MethodName_StateUnderTest_ExpectedBehaviour* approach.

## 1) `public static int parseSeconds(String strTime) throws ParseException`

### Description

This method was selected since it contains several useful testing components, such as:
- An argument `strTime` to use within the function (def and c-use)
- An if statement to test branching (p-use)
- Local variable definitions and usage (def and c-use)
- Method invocation of a defined variable (c-use)

#### *Dataflow Testing*

Numbering the lines of the `parseSeconds` method, we get:

```java
1.  public static int parseSeconds(String strTime) throws ParseException {
2.      Pattern p = Pattern.compile("(\\d+):([0-5]?\\d):([0-5]?\\d)");    // 0:00:00
3.      Matcher m = p.matcher(strTime);
4. 
5.      if (!m.matches()) {
6.          throw new ParseException("Invalid seconds-string", 0);
7.      }
8. 
9.      int hours = Integer.parseInt(m.group(1));
10.     int minutes = Integer.parseInt(m.group(2));
11.     int seconds = Integer.parseInt(m.group(3));
12. 
13.     return (hours * 3600 + minutes * 60 + seconds);
14. }
```

The *Control-flow Graph* of this method is as follows:

![parseSeconds CFG](./images/cfg_parseSeconds.png)

In this case, the variables of interest are `strTime`, `p`, `m`, `hours`, `minutes` and `seconds`.
After identifying and classifying the occurrences of all variables in the software under test (computing **defs**, **c-uses** and **p-uses** in each block), we are faced with the following *def-use graph*:

![parseSeconds def-use graph](./images/dug_parseSeconds.png)

The **def-use pairs** identified for the variable `strTime` are:

| pair id | def | use |   path  |
|:-------:|:---:|:---:|:-------:|
|    1    |  1  |  3  | <1,2,3> |

The **def-use pairs** identified for the variable `p` are:

| pair id | def | use |  path |
|:-------:|:---:|:---:|:-----:|
|    1    |  2  |  3  | <2,3> |

The **def-use pairs** identified for the variable `m` are:

| pair id | def |  use  |         path        |
|:-------:|:---:|:-----:|:-------------------:|
|    1    |  3  |   9   |    <3,4,5,7,8,9>    |
|    2    |  3  |   10  |   <3,4,5,7,8,9,10>  |
|    3    |  3  |   11  | <3,4,5,7,8,9,10,11> |
|    4    |  3  | (5,T) |      <3,4,5,6>      |
|    5    |  3  | (5,F) |      <3,4,5,7>      |

The **def-use pairs** identified for the variable `hours` are:

| pair id | def | use |       path      |
|:-------:|:---:|:---:|:---------------:|
|    1    |  9  |  13 | <9,10,11,12,13> |

The **def-use pairs** identified for the variable `minutes` are:

| pair id | def | use |      path     |
|:-------:|:---:|:---:|:-------------:|
|    1    |  10 |  13 | <10,11,12,13> |

The **def-use pairs** identified for the variable `seconds` are:

| pair id | def | use |    path    |
|:-------:|:---:|:---:|:----------:|
|    1    |  11 |  13 | <11,12,13> |

To satisfy the **all-defs** criteria, we must test, for example, the paths in the following pairs:

| **variable** | `strTime` | `p` | `m` | `hours` | `minutes` | `seconds` |
|:------------:|:---------:|:---:|:---:|:-------:|:---------:|:---------:|
| **pair ids** |     1     |  1  |  1  |    1    |     1     |     1     |

To satisfy the **all-c-uses** criteria, we must test, for example, the paths in the following pairs:

| **variable** | `strTime` | `p` |  `m`  | `hours` | `minutes` | `seconds` |
|:------------:|:---------:|:---:|:-----:|:-------:|:---------:|:---------:|
| **pair ids** |     1     |  1  | 1,2,3 |    1    |     1     |     1     |

To satisfy the **all-p-uses** criteria, we must test, for example, the paths in the following pairs:

| **variable** | `strTime` | `p` | `m` | `hours` | `minutes` | `seconds` |
|:------------:|:---------:|:---:|:---:|:-------:|:---------:|:---------:|
| **pair ids** |     -     |  -  | 4,5 |    -    |     -     |     -     |

To satisfy the **all-uses** criteria, we must test, for example, the paths in the following pairs:

| **variable** | `strTime` | `p` |    `m`    | `hours` | `minutes` | `seconds` |
|:------------:|:---------:|:---:|:---------:|:-------:|:---------:|:---------:|
| **pair ids** |     1     |  1  | 1,2,3,4,5 |    1    |     1     |     1     |

#### Unit Tests

The tests implemented for this function can be found in the `ProjectTimeTest.java` file, inside the `test` directory.
We decided to apply the coverage criteria **all-uses**, as it covers the most cases.
Looking at this table, we see that all the required **pair ids** result in paths that are already covered by the tests developed in the previous assignments.
More specifically:
- `strTime` **pair id** 1 is covered by just a single method call, which is present in all these tests
- `p` **pair id** 1 is also covered by just a single method call, which is present in all these tests
- `m` **pair ids** 1,2,3 and 5 are covered when the string `strTime` conforms to the time format (valid input - tested in the method `testParseSeconds_CorrectDateFormat_ShouldReturnSeconds`), and **pair id** 4 is covered when it does not (invalid input - tested in the method `testParseSeconds_IncorrectDateFormat_ShouldThrowException`)
- `hours`, `minutes` and `seconds` **pair ids** 1 are also covered when no exception is raised (valid input - tested in the method `testParseSeconds_CorrectDateFormat_ShouldReturnSeconds`)

Some valid inputs were tested (such as `4:21:16` or `06:09:03`), as well as some invalid (such as `4.21.16` or `24:00:60`).

```java
@Nested
    public class ParseSecondsTest {
        @Test
        @DisplayName("Test with null input throws exception")
        public void testParseSeconds_NullInput_ShouldThrowException() {
            assertThrows(NullPointerException.class, () -> parseSeconds(null));
        }

        @ParameterizedTest(name = "Test #{index} with input {0} results in {1} seconds")
        @CsvSource(value = {"0:0:0,0", "12:15:0,44100", "7:0:9,25209", "0:17:05,1025", "20:02:0,72120", "4:21:16,15676", "100:59:59,363599", "18:59:14,68354", "9:21:59,33719", "06:09:03,22143", "13:21:12,48072"})
        public void testParseSeconds_CorrectDateFormat_ShouldReturnSeconds(String format, int seconds) throws ParseException {
            assertEquals(seconds, parseSeconds(format));
        }

        @ParameterizedTest(name = "Test #{index} with input {arguments} throws exception")
        @ValueSource(strings = {"", "4.21.16", "1:11:11:11", "1:11", "1", "00:60:00", "24:00:60", "-2:13:09", "8:-42:09", "07:5:-15", "aa:19:23", "14:bb:34", "04:25:cc", "14:021:34", "04:25:123"})
        public void testParseSeconds_IncorrectDateFormat_ShouldThrowException(String format) {
            assertThrows(ParseException.class, () -> parseSeconds(format));
        }
    }
```

All the tests above pass successfully, although we think that some cases where the input does not have two `:` separators, like "5:14", should be accepted.

![All tests of the method `parseSeconds` pass successfully](./images/dft_tests1.png)

## 2) `public void adjustSecondsToday(int secondsToday)`

### Description

This method was selected since it contains several useful testing components, such as:
- An argument `secondsToday` to use within the function (def and c-use)
- An if statement to test branching (p-use)
- Local variable definitions and usage (def and c-use)
- Method invocation of a defined variable (c-use)
- Usage of the `this` keyword to access variables within the class' scope (c-use)

#### *Dataflow Testing*

Numbering the lines of the `adjustSecondsToday` method, we get:

```java
1.  public void adjustSecondsToday(int secondsToday) {
2.      if (secondsToday < 0) {
3.          secondsToday = 0;
4.      }
5.  
6.      int secondsDelta = secondsToday - this.secondsToday;
7.  
8.      this.setSecondsOverall(this.getSecondsOverall() + secondsDelta);
9.      this.setSecondsToday(secondsToday);
10. }
```

The *Control-flow Graph* of this method is as follows:

![adjustSecondsToday CFG](./images/cfg_adjustSecondsToday.png)

In this case, the variables of interest are `secondsToday`, `this.secondsToday`, and `secondsDelta`.
After identifying and classifying the occurrences of all variables in the software under test (computing **defs**, **c-uses** and **p-uses** in each block), we are faced with the following *def-use graph*:

![adjustSecondsToday def-use graph](./images/dug_adjustSecondsToday.png)

The **def-use pairs** identified for the variable `secondsToday` are:

| pair id | def |  use  |        path       |
|:-------:|:---:|:-----:|:-----------------:|
|    1    |  1  |   6   |    <1,2,4,5,6>    |
|    2    |  1  |   9   | <1,2,4,5,6,7,8,9> |
|    3    |  1  | (2,T) |      <1,2,3>      |
|    4    |  1  | (2,F) |      <1,2,4>      |
|    5    |  3  |   6   |     <3,4,5,6>     |
|    6    |  3  |   9   |  <3,4,5,6,7,8,9>  |

The **def-use pairs** identified for the variable `this.secondsToday` are:

| pair id |  def  | use |      path     |
|:-------:|:-----:|:---:|:-------------:|
|    1    | entry |  6  | <1,2,3,4,5,6> |
|    2    | entry |  6  |  <1,2,4,5,6>  |

The **def-use pairs** identified for the variable `secondsDelta` are:

| pair id | def | use |   path  |
|:-------:|:---:|:---:|:-------:|
|    1    |  6  |  8  | <6,7,8> |

To satisfy the **all-defs** criteria, we must test, for example, the paths in the following pairs:

| **variable** | `secondsToday` | `this.secondsToday` | `secondsDelta` |
|:------------:|:--------------:|:-------------------:|:--------------:|
| **pair ids** |        1       |          2          |        1       |

To satisfy the **all-c-uses** criteria, we must test, for example, the paths in the following pairs:

| **variable** | `secondsToday` | `this.secondsToday` | `secondsDelta` |
|:------------:|:--------------:|:-------------------:|:--------------:|
| **pair ids** |     1,2,5,6    |          2          |        1       |

To satisfy the **all-p-uses** criteria, we must test, for example, the paths in the following pairs:

| **variable** | `secondsToday` | `this.secondsToday` | `secondsDelta` |
|:------------:|:--------------:|:-------------------:|:--------------:|
| **pair ids** |       3,4      |          -          |        -       |

To satisfy the **all-uses** criteria, we must test, for example, the paths in the following pairs:

| **variable** | `secondsToday` | `this.secondsToday` | `secondsDelta` |
|:------------:|:--------------:|:-------------------:|:--------------:|
| **pair ids** |   1,2,3,4,5,6  |          2          |        1       |

#### Unit Tests

The tests implemented for this function can be found in the `ProjectTest.java` file, inside the `test` directory.
We decided to apply the coverage criteria **all-uses**, as it covers the most cases.
Looking at this table, we see that all the required **pair ids** result in paths that are already covered by the tests developed in the previous assignments, namely:
- `secondsToday` **pair id** 3, 5, and 6 are covered when the `secondsToday` variable is negative (tested in method `testAdjustSecondsToday_NegativeInput_ShouldBecomeZero`)
- `secondsToday` **pair ids** 1, 2, and 4 are covered when the `secondsToday` variable is positive (tested in method `testAdjustSecondsToday_PositiveInput_ShouldReturnOverallTime`) or when it's 0 (tested in `testAdjustSecondsToday_NegativeInput_ShouldBecomeZero`)
- `this.secondsToday` **pair id** 2 is covered when the `secondsToday` variable is positive (tested in method `testAdjustSecondsToday_PositiveInput_ShouldReturnOverallTime`) or when it's 0 (tested in `testAdjustSecondsToday_NegativeInput_ShouldBecomeZero`)
- `secondsDelta` **pair id** 1 is covered in every test case

```java
@Nested
    public class AdjustSecondsTodayTest {
        private Project proj;
        private final int overallTime = 50;
        private final int previousValue = 4;
        
        @BeforeEach
        void setup() {
            proj = new Project();
            proj.setSecondsToday(previousValue);
            proj.setSecondsOverall(overallTime);
        }
        
        @ParameterizedTest(name = "Test #{index} with Positive input {arguments}")
        @ValueSource(ints = {1,2,3,4,5,6})
        public void testAdjustSecondsToday_PositiveInput_ShouldReturnOverallTime(int value) {
            proj.adjustSecondsToday(value);
            assertEquals(value, proj.getSecondsToday());
            assertEquals(overallTime + value - previousValue, proj.getSecondsOverall());
        }

        @ParameterizedTest(name = "Test #{index} with Non-Positive input {arguments}")
        @ValueSource(ints = {-2,0})
        public void testAdjustSecondsToday_NegativeInput_ShouldBecomeZero(int value) {
            proj.adjustSecondsToday(value);
            assertEquals(0, proj.getSecondsToday());
            assertEquals(overallTime - previousValue, proj.getSecondsOverall());
        }
    }
```

All the tests above passed successfully, as expected.

![All tests of the method `adjustSecondsToday` passed successfully](./images/dft_tests2.png)

## 3) `public void handleStartPause(Project prj) throws ParseException`

### Description

This method was selected since it contains several useful testing components, such as:
- An argument `prj` to use within the function (def and c-use)
- An if statement to test branching (p-use)
- A for loop with variable definition and usage, CFG branching, and stop condition (def, c-use, p-use)
- Try Catch statements with CFG branching(def, c-use)
- Local variable definitions and usage (def and c-use)
- Method invocation of a defined variable (c-use)
- Usage of the `this` keyword to define and access variables within the class' scope (def, c-use)

#### *Dataflow Testing*

Numbering the lines of the `handleStartPause` method, we get:

```java
1.  public void handleStartPause(Project prj) throws ParseException {
2.      JTimeSchedApp.getLogger().info(String.format("%s project '%s' (time overall: %s, time today: %s)",
            (prj.isRunning()) ? "Pausing" : "Starting",
            prj.getTitle(),
            ProjectTime.formatSeconds(prj.getSecondsOverall()),
            ProjectTime.formatSeconds(prj.getSecondsToday())));
3. 
4.      try {
5.          if (prj.isRunning()) {
6.              prj.pause();
7.          } else {
8.              // pause all other projects
9.              for (Project p : this.arPrj) {
10.                 if (p.isRunning()) {
11.                     p.pause();
12.                 }
13.             }
14.
15.             // set project to run-state
16.             prj.start();
17.         }
18.
19.         this.currentProject = prj;
20.         this.updateTrayCurrentProject();
21.
22.     } catch (ProjectException ex) {
23.         ex.printStackTrace();
24.     }
25.
26.     // update table
27.     this.updateGUI();
28. }
```

The *Control-flow Graph* of this method is as follows:

![handleStartPause CFG](./images/cfg_handleStartPause.png)

In this case, the variables of interest are `prj`, `p`, `this.arPrj`, `this.currentProject`, and `ex`.
After identifying and classifying the occurrences of all variables in the software under test (computing **defs**, **c-uses** and **p-uses** in each block), we are faced with the following *def-use graph*:

![handleStartPause def-use graph](./images/dug_handleStartPause.png)

The **def-use pairs** identified for the variable `prj` are:

| pair id | def |  use  |                        path                       |
|:-------:|:---:|:-----:|:-------------------------------------------------:|
|    1    |  1  |   2   |                       <1,2>                       |
|    2    |  1  |   6   |                   <1,2,3,4,5,6>                   |
|    3    |  1  |   16  |      <1,2,3,4,5,7,8,9,10,11,12,13,9,14,15,16>     |
|    4    |  1  |   16  |       <1,2,3,4,5,7,8,9,10,12,13,9,14,15,16>       |
|    5    |  1  |   16  |             <1,2,3,4,5,7,8,9,14,15,16>            |
|    6    |  1  |   19  | <1,2,3,4,5,7,8,9,10,11,12,13,9,14,15,16,17,18,19> |
|    7    |  1  |   19  |   <1,2,3,4,5,7,8,9,10,12,13,9,14,15,16,17,18,19>  |
|    8    |  1  |   19  |        <1,2,3,4,5,7,8,9,14,15,16,17,18,19>        |
|    9    |  1  |   19  |                <1,2,3,4,5,6,18,19>                |
|    10   |  1  | (2,T) |                       <1,2>                       |
|    11   |  1  | (2,F) |                       <1,2>                       |
|    12   |  1  | (5,T) |                   <1,2,3,4,5,6>                   |
|    13   |  1  | (5,F) |                   <1,2,3,4,5,7>                   |

The **def-use pairs** identified for the variable `p` are:

| pair id | def |   use  |    path   |
|:-------:|:---:|:------:|:---------:|
|    1    |  9  |   11   | <9,10,11> |
|    2    |  9  | (10,T) | <9,10,11> |
|    3    |  9  | (10,F) | <9,10,12> |

The **def-use pairs** identified for the variable `this.arrPrj` are:

| pair id |  def  | use |        path       |
|:-------:|:-----:|:---:|:-----------------:|
|    1    | entry |  9  | <1,2,3,4,5,7,8,9> |

The **def-use pairs** identified for the variable `this.currentProject` are:

// TODO: perguntar o que acontece neste caso, em que só tem defs e nenhum use

The **def-use pairs** identified for the variable `ex` are:

| pair id | def | use |   path  |
|:-------:|:---:|:---:|:-------:|
|    1    |  22 |  23 | <22,23> |

To satisfy the **all-defs** criteria, we must test, for example, the paths in the following pairs:

| **variable** | `prj` | `p` | `this.arPrj` | `this.currentProject` | `ex` |
|:------------:|:-----:|:---:|:------------:|:---------------------:|:----:|
| **pair ids** |   2   |  1  |       1      |                       |   1  |

To satisfy the **all-c-uses** criteria, we must test, for example, the paths in the following pairs:

| **variable** |  `prj`  | `p` | `this.arPrj` | `this.currentProject` | `ex` |
|:------------:|:-------:|:---:|:------------:|:---------------------:|:----:|
| **pair ids** | 1,2,5,9 |  1  |       1      |                       |   1  |

To satisfy the **all-p-uses** criteria, we must test, for example, the paths in the following pairs:

| **variable** |    `prj`    | `p` | `this.arPrj` | `this.currentProject` | `ex` |
|:------------:|:-----------:|:---:|:------------:|:---------------------:|:----:|
| **pair ids** | 10,11,12,13 | 2,3 |       -      |                       |   -  |

To satisfy the **all-uses** criteria, we must test, for example, the paths in the following pairs:

| **variable** |        `prj`        |  `p`  | `this.arPrj` | `this.currentProject` | `ex` |
|:------------:|:-------------------:|:-----:|:------------:|:---------------------:|:----:|
| **pair ids** | 1,2,5,9,10,11,12,13 | 1,2,3 |       1      |                       |   1  |

#### Unit Tests

// TODO: falar sobre os unit tests implementados e descrever outcome

-----

## Group 10

- Hugo Guimarães, up201806490
- Paulo Ribeiro, up201806505

## Sources

- [Class Slides - Prof. José Campos](https://paginas.fe.up.pt/~jcmc/tvvs/2022-2023/lectures/lecture-7.pdf)
- [Dataflow Testing - Javatpoint](https://www.javatpoint.com/data-flow-testing-in-white-box-testing)