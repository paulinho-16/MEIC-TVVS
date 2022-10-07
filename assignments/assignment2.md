# Assignment 2 - Equivalence Class Partitioning / Category Partition (Black-box Testing)

**Equivalence Class Partitioning** is a black-box testing technique in which we group the input data into logical partitions called equivalence classes.
All the data items lying in an equivalence class are assumed to be processed in the same way by the software application to be tested when passed as input.
So, instead of testing all the combinations of input test data, we can pick and pass any of the test data from a particular equivalence class to the application and assume that the application will behave in the same way as the other test data of that class.
This process greatly reduces the number of test cases maintaining the same test coverage, being, therefore, perfectly suitable for software projects with time and resource constraints.

The **Category Partition** method consists of a systematic way of deriving these partitions, based on the characteristics of the input parameters.

To try out these techniques, we selected five different functions from the package `de.dominik_geyer.jtimesched.project` of the *jTimeSched* project.
For each one, we present the reason we decided to test it and its purpose.
Next, we apply the **Category Partition** algorithm and describe the unit tests we generated based on this method, as well as their outcomes.

We tried our best to follow a black-box testing approach, even though the [documentation](https://paginas.fe.up.pt/~jcmc/tvvs/2022-2023/assignments/jtimesched-javadoc/index.html) of the project at hand is non-existent, limited only to the signature of the functions.
For this reason, we were forced to resort to source code to understand the purpose of some functions, but even in these cases, we tried to just focus on their purpose and not on their functioning, to avoid being influenced when writing the tests.

As for the naming of test methods, we follow a *MethodName_StateUnderTest_ExpectedBehaviour* approach.

### 1) `public void start() throws ProjectException` 

#### Description

This function of the `Project` class is called whenever the user starts running an existing project.

This action can be performed by clicking the *Play* button in the project's final column, when the project is paused, resuming it.

When the project starts running, the `Time Overall` and `Time Today` fields start counting up.

#### *Category-Partition* algorithm

1. This method has no parameters.
1. Given there are no parameters, no characteristics can be derived.
1. There are no parameters or characteristics, so we don't need to be defining testable combinations of features.
   *Constraints*: A project that is already running cannot be started.
1. Considering these two situations, we get the following tests:
   - attempting to start a paused project;
   - attempting to start a running project;

#### Unit Tests

The tests implemented for this function can be found in the `ProjectTest.java` file, inside the `test` directory.

We decided to create two test methods. The first one for ensuring a created project that is initially paused starts running after the `start()` method is called. The second one for ensuring an exception is raised when the `start` method is called upon a project that is already running.

In the first case, we start by creating a new `Project` variable, which is paused by default, and we verify if the value of the `running` variable is false.

Afterward, we call the `start` method and we check if the `running` variable's value has changed to true.

Besides, we also verify if the `timeStart` variable is similar to the time of the project's creation, meaning that its value has changed during the test execution.

```java
@Test
public void testStart_IdleProject_ShouldStart() throws ProjectException {
   // Create new Project
   Project prj = new Project("Test project");
   // Project should not be running before it is started
   Assertions.assertFalse(prj.isRunning());
   Date beforeStart = new Date();
   // Starting project
   prj.start();
   // Project should be running after it is started
   Assertions.assertTrue(prj.isRunning());
   // Checking that the project start date has been set during the running test
   Assertions.assertTrue(Math.abs(beforeStart.getTime() - prj.getTimeStart().getTime()) < 1000, "Times aren't close enough to each other!");
}
```

As for the second case, we started by creating a `Project`, and attempted to call the `start` method twice consecutively, verifying if a `ProjectException` is raised on the second method call.

```java
@Test
public void testStart_RunningProject_ShouldReturnException() throws ProjectException {
   // Create new Project
   Project prj = new Project("Test project");
   // Starting project
   prj.start();
   // Starting project again
   assertThrows(ProjectException.class, prj::start);
}
```

All the tests above passed successfully, as expected.

![All tests of the method `start` passed successfully](./images/cp_tests1.png)

### 2) `public void adjustSecondsToday(int secondsToday)`

#### Description

This function of the `Project` class is called whenever the user edits the `Time Today` table field, arising the need to adjust the value from the `Time Overall` variable accordingly.
This method evaluates the variations from the input values in comparison to the previously stored ones and makes the necessary adjustments to update the time variables.

#### *Category-Partition* algorithm

1. This method has only one parameter:
   - `secondsToday`: an int representing the provided seconds in the table field.
1. For each parameter we define the characteristics as:
   - `secondsToday`: corresponds to the number of seconds of the user input variable
1. The number of characteristics and parameters is not too large in this case, so we don't need to be defining testable combinations of features.
   *Constraints*: negative values for the variable `secondsToday` are not allowed.
   Several combinations of values must be tested, such as a negative input from the seconds and different combinations of the `secondsToday` parameter being smaller or larger than the previous value of the `secondsToday` variable.
1. After thinking about the possible categories of inputs, we get the following tests:
   - `secondsToday` input is larger than its previous value
   - `secondsToday` input is smaller than its previous value 
   - `secondsToday` input is negative

#### Unit Tests

The tests implemented for this function can be found in the `ProjectTest.java` file, inside the test directory. We decided to create three test methods for different input values of the `secondsToday` parameter.

All tests have the following steps:
1. Creating a new `Project` variable
1. Setting the project's `secondsToday` and `secondsOverall` variables to a specific value
1. Calling the `adjustSecondsToday` method with a specific value in its argument
1. Verifying the final values for the `secondsToday` and `secondsOverall` variables after the method has been called, expecting a predetermined output using the `assertEquals` method

The tests only differ on the values provided for steps 2 and 3.

In the first case, the value initially set on the `secondsToday` variable before the method call is smaller than the one afterward. This results in an increased expected output of the `secondsOverall` variable after the method call.

```java
@Test
public void testAdjustSecondsToday_LargerInput_ShouldIncreaseOverallTime() {
   Project proj = new Project("Test Project");
   proj.setSecondsToday(10);
   proj.setSecondsOverall(50);
   proj.adjustSecondsToday(20);

   assertEquals(20,proj.getSecondsToday());
   assertEquals(60,proj.getSecondsOverall());
}
```

In the second case, the value initially set on the `secondsToday` variable before the method call is larger than the one afterward. This results in a lowered expected output of the `secondsOverall` variable after the method call.
```java
@Test
public void testAdjustSecondsToday_SmallerInput_ShouldReduceOverallTime() {
   Project proj = new Project("Test Project");
   proj.setSecondsToday(20);
   proj.setSecondsOverall(50);
   proj.adjustSecondsToday(10);

   assertEquals(10,proj.getSecondsToday());
   assertEquals(40,proj.getSecondsOverall());
}
```

In the third and last case, the value initially set on the `secondsToday` variable is 10 and the one provided in the method call is negative. Given the provided parameter is negative, it is parsed as zero and the method updates the `secondsToday` and `secondsOverall` considering this value and not the negative one.

```java
@Test
public void testAdjustSecondsToday_NegativeInput_ShouldBecomeZero() {
   Project proj = new Project("Test Project");
   proj.setSecondsToday(10);
   proj.setSecondsOverall(50);
   proj.adjustSecondsToday(-10);

   assertEquals(0,proj.getSecondsToday());
   assertEquals(40,proj.getSecondsOverall());
}
```

All the tests above passed successfully, as expected.

![All tests of the method `adjustSecondsToday` passed successfully](./images/cp_tests2.png)

### 3) `public static int parseSeconds(String strTime) throws ParseException`

#### Description

This function of the `ProjectTime` class is called when the user edits the value of the "Time Overall" column, or the "Time Today" column of a given task/project, which correspond to the total time spent on that item, and the time spent on the current day, respectively.
This action can be performed directly on the table by double-clicking with the left mouse button on the respective field, or via an input window that opens after a right mouse click on the field.

Looking at its signature, we immediately deduced that it receives as input a string, coming from the user, and returns an int, which we believe is the total number of seconds taken by the task, due to the name of the function.
Therefore, its purpose would be to receive a time in a given string format and return the corresponding total number of seconds, to be able to update the table values accordingly.

That said, it is very important to test functions that receive user input, which we can never trust.
They can result in values in formats that are not the ones expected by the application, leading to its downfall.

After checking the format expected by the function (for this we had to resort to the source code, due to the lack of documentation), we thought of countless possibilities of inputs that could be categorized.
This possibility, together with the importance of robustness concerning user inputs, were the reasons why we chose this function.

#### *Category-Partition* algorithm

1. This method has only one parameter:
    - `strTime`: a string representing a given time
    
1. For each parameter we define the characteristics as:
    - `strTime`: corresponds to a time representation, in the format `h:m:s`, where `h`, `m` and `s` are the hours, minutes, and seconds of that time duration, respectively

1. The number of characteristics and parameters is not too large in this case, so we don't need to be defining testable combinations of features.
   *Constraints*: the string `strTime` must conform to the time format `(\d+):([0-5]?\d):([0-5]?\d)`, other variations are not allowed.
   Even in the case of following this format, it will be necessary to test certain values for the time units, such as invalid unit times like seconds exceeding the value 59 (must not be accepted) or units with leading zeros (should be accepted).

1. After thinking about the possible categories of inputs, we get the following tests:
    - `strTime` conforms to the time format
        - `strTime` duration is zero
        - `strTime` only has a zero value in the seconds time unit
        - `strTime` only has a zero value in the minutes time unit
        - `strTime` only has a zero value in the hours time unit
        - `strTime` duration is less than 24 hours
        - `strTime` duration exceeds 24 hours
        - `strTime` time units have leading zeros
    - `strTime` doesn't conform to the time format
        - `strTime` is an empty string
        - `strTime` uses a separator character other than `:`
        - `strTime` uses more than two separators, to reference days, for example
        - `strTime` uses only one separator, when the time does not exceed one hour, for example
        - `strTime` doesn't use any separator, when the time does not exceed one minute, for example
        - the time unit minutes of `strTime` exceeds the value 59
        - the time unit seconds of `strTime` exceeds the value 59
        - some time unit of `strTime` contains a negative number
        - some time unit of `strTime` contains non-numeric characters

#### Unit Tests

The tests implemented for this function can be found in the `ProjectTimeTest.java` file, inside the `test` directory.
We decided to create two test methods, one for each super-category: the cases where `strTime` conforms to the required format, and the cases where it does not.

In the first case, the test just checks if the evaluated function returns the correct number of seconds for each input.
As we want to execute a single test method multiple times with different parameters, we must resort to a parameterized test.
We feed the function the various input-output pairs using the `@CsvSource` annotation.

```java
@ParameterizedTest(name = "Test #{index} with input {0} results in {1} seconds")
@CsvSource(value = {"0:0:0,0", "12:15:0,44100", "7:0:9,25209", "20:02:0,72120", "4:21:16,15676", "59:59:59,215999", "06:09:03,22143"})
public void testParseSeconds_CorrectDateFormat_ShouldReturnSeconds(String format, int value) throws ParseException {
   assertEquals(value, parseSeconds(format));
}
```

As for the second case, the test must check whether the execution of the evaluated function throws an exception of type *ParseException*, as suggested by the function signature.
Since we still need to execute a single test method multiple times with different parameters, we resorted to a parameterized test as well.
Bearing in mind that now we only need to pass a single value to the test function (there is no output as in the first case), we use the `@ValueSource` annotation to feed the function the invalid values of `strTime`.

```java
@ParameterizedTest(name = "Test #{index} with input {arguments} throws exception")
@ValueSource(strings = {"", "4.21.16", "1:11:11:11","1:11","1","00:60:00","24:00:60", "8:-42:09", "aa:bb:cc"})
public void testParseSeconds_IncorrectDateFormat_ShouldThrowException(String format) {
    assertThrows(ParseException.class, () -> parseSeconds(format));
}
```

Additionally, we changed the text displayed during the tests' execution using the `name` attribute of the `@ParameterizedTest` annotation, to make it more readable.
Note that we created an input-output pair for each subcategory numbered in the previous subsection.

All the tests above pass successfully, although we think that some cases where the input does not have two `:` separators, like "5:14", should be accepted.

![All tests of the method `parseSeconds` pass successfully](./images/cp_tests3.png)

### 4) `public boolean isCellEditable(int row, int column)`

#### Description

This function of the `isCellEditable` class is called when the user edits any column field from any project in the table, verifying if the user is allowed to edit the selected table cell.

#### *Category-Partition* algorithm

1. This method has two parameters:
   - `row`: an integer value to select the table row to edit. This effectively selects which project the user wants to edit.
   - `column`: an integer value to select the table column to edit. This effectively selects which value of the project the user wishes to edit.
1. For each parameter we define the characteristics as:
   - `row`: must be an integer value lower or equal to the number of existing projects. 
   - `column`: must be an integer value from 0 to 7, representing the project column the user wishes to edit.
   
   It represents the following constants:
   ```java
   public static final int COLUMN_ACTION_DELETE = 0;
   public static final int COLUMN_CHECK = 1;
   public static final int COLUMN_TITLE = 2;
   public static final int COLUMN_COLOR = 3;
   public static final int COLUMN_CREATED = 4;
   public static final int COLUMN_TIMEOVERALL = 5;
   public static final int COLUMN_TIMETODAY = 6;
   public static final int COLUMN_ACTION_STARTPAUSE = 7;
   ``` 
1. The number of characteristics and parameters is not too large in this case, so we don't need to be defining testable combinations of features.
   *Constraints*: 
   - the integer `row` must be a non-negative value lower than the number of existing projects
   - the integer `column` must be a value between 0 and 7, according to the clickable columns in the interface
   
1. After thinking about the possible categories of inputs, we get the following tests:
   - valid `row` value (corresponding to a given project) and `column` value that corresponds to an editable column
   - valid `row` value (corresponding to a given project), but `column` value corresponds to a non-editable column
   - invalid `row` value (negative or higher or equal to the number of existing projects)
   - invalid `column` value (negative or higher than 7, which is the last column)

#### Unit Tests

The tests implemented for this function can be found in the `ProjectTableModelTest.java` file, inside the `test` directory.
We decided to create three test methods, the first two related to allowed and prohibited cells to edit, respectively, and the last one to exceptions raised for out-of-bounds and illegal inputs.

The first two tests use `@ParameterizedTest` with a `@ValueSource` list of values for the `columns` variable. In these cases, the `row` value is equal to zero, as we are creating a single project and checking the fields for that single project as an example.

The third case uses `@ParameterizedTest` with `@CsvSource` lists of `row` and `columns` combinations of values to check for invalid inputs.

In the first case, the test just instantiates a `Project` and checks if all fields allowed to be edited return true when the `isEditable` method is called upon the project.

```java
@ParameterizedTest(name = "Test #{index} with input {arguments} returns true")
@ValueSource (ints = {
   ProjectTableModel.COLUMN_CHECK,
   ProjectTableModel.COLUMN_TITLE,
   ProjectTableModel.COLUMN_COLOR,
   ProjectTableModel.COLUMN_CREATED,
   ProjectTableModel.COLUMN_TIMEOVERALL,
   ProjectTableModel.COLUMN_TIMETODAY
})

public void testIsCellEditable_EditableCell_ShouldReturnTrue(int column) {
   Project proj1 = new Project("Test Project");
   ArrayList<Project> projects = new ArrayList<>();

   projects.add(proj1);
   ProjectTableModel tableModel = new ProjectTableModel(projects);

   assertTrue(tableModel.isCellEditable(0, column));
}
```

The second case, as an opposite, instantiates a `Project` and checks if all cells that are prohibited from being edited return false when the `isEditable` method is called. 

```java
@ParameterizedTest(name = "Test #{index} with input {arguments} returns false")
@ValueSource (ints = {
   ProjectTableModel.COLUMN_ACTION_DELETE,
   ProjectTableModel.COLUMN_TIMEOVERALL,
   ProjectTableModel.COLUMN_TIMETODAY,
   ProjectTableModel.COLUMN_ACTION_STARTPAUSE,
})
public void testIsCellEditable_NonEditableCell_ShouldReturnFalse(int column) throws ProjectException {
   Project proj1 = new Project("Test Project");
   proj1.start();

   ArrayList<Project> projects = new ArrayList<>();

   projects.add(proj1);
   ProjectTableModel tableModel = new ProjectTableModel(projects);

   assertFalse(tableModel.isCellEditable(0, column));
}
```

The third and last case includes the situations in which exceptions are expected to be thrown. There are several combinations explored, such as negative `rows` and `columns` as well as values out of the allowed bounds, such as a `row` value for a non-existent project and a `column` value for a field that doesn't exist.

```java
@ParameterizedTest(name = "Test #{index} with input ({arguments}) throws exception")
@CsvSource(value = {"-1,2", "2,3", "0,-2", "0,9"})
public void testIsCellEditable_InvalidCell_ShouldThrowException(int row, int column) {
   Project proj1 = new Project("Test Project");
   ArrayList<Project> projects = new ArrayList<>();

   projects.add(proj1);
   ProjectTableModel tableModel = new ProjectTableModel(projects);

   assertThrows(IndexOutOfBoundsException.class, () -> tableModel.isCellEditable(row, column));
}
```

The first two cases' tests succeed, but there are two tests in the final case that fail. This occurs because there is no verification for the `column` value being out of bounds, given the author just assumes that this case never happens since there are only seven clickable columns. However, if a future feature allows the manual insertion of a `column` value in the terminal, or a new column is added, an error might occur. Therefore, this test was made to prevent future errors from being created, and we recommend handling these cases in the source code.

![12 tests from `isCellEditable` pass successfully, 2 fail](./images/cp_tests4.png)

### 5) `public void writeXml(List<Project> projects) throws TransformerConfigurationException, SAXException, IOException`

#### Description

Looking at the signature of this function of the `ProjectSerializer` class, we easily deduced that its purpose was to store information about existing tasks/projects in an XML file.
Therefore, it receives as input a list of existing projects, and its objective will be to write all the data to the XML file so that, in the next execution of the program, it can restore the state of the application upon reading this file.
It is called as soon as the user clicks the *Exit* button, which ends the application. In addition, it is also called periodically through a timer with each passing minute, to avoid losing information after unexpected program terminations.

That said, it is a fundamental function, which prevents the loss of user information between different executions of the program, hence it is important to test it.
Furthermore, it is a function that can be tested via Black-box techniques despite the lack of documentation, since we could only look at an XML file resulting from our execution of the program and estimate which aspects of the writing should be evaluated, completely refraining from analyzing the source code.
This convenience, together with the importance of the function, were the reasons why we decided to test it.

#### *Category-Partition* algorithm

1. This method has only one parameter:
   - `projects`: a list of the existing tasks/projects

1. For each parameter we define the characteristics as:
   - `projects`: the list must contain objects of type `Project`, which gather all the information of a given project, and can be empty if there are no projects registered.

1. The number of characteristics and parameters is not too large in this case, so we don't need to be defining testable combinations of features.
   *Constraints*: the `projects` parameter cannot be null.

1. After thinking about the possible categories of inputs, we get the following tests:
   - `projects` parameter is null
   - `projects` is an empty list
   - `projects` contains at least one project

#### Unit Tests

The tests implemented for this function can be found in the `ProjectSerializerTest.java` file, inside the `test` directory.
We created three test methods, one for each category, and created a helper function that just parses the XML file and retrieves a `Document` object, to avoid redundant code in the tests.
The tests will be applied to a file called `WriteXmlTest.xml`, in the `test/resources` directory.

```java
public Document readXml() throws ParserConfigurationException, IOException, SAXException {
   DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
   DocumentBuilder builder = factory.newDocumentBuilder();
   return builder.parse(new File(filename));
}
```

In the first case, the test just checks whether the execution of the evaluated function throws an exception of type *NullPointerException*.

```java
@Test
public void testWriteXml_NullParameter_ShouldThrowException() {
    assertThrows(NullPointerException.class, () -> ps.writeXml(null));
}
```

As for the second case, where `projects` is an empty list, the test must verify that the method does not write project information to the file.
We did this by collapsing all document nodes with the "project" tag into a list, and checking that the size of that list is zero.

```java
@Test
public void testWriteXml_EmptyList_ShouldWriteNoProjects() throws TransformerConfigurationException, IOException, SAXException, ParserConfigurationException {
   List<Project> projects = new ArrayList<>();
   ps.writeXml(projects);
   
   Document document = readXml();
   
   Element root = document.getDocumentElement();
   NodeList nl = root.getElementsByTagName("project");
   assertEquals(0, nl.getLength());
}
```

Finally, for the third case, we created three projects and changed their attributes, namely:
   - For project 1:
      - started the project (changes its *running* state)
      - changed its notes
   - For project 2:
      - set the project as checked
      - set a new color
   - For project 3:
      - set the *SecondsToday* time
      - set the *SecondsOverall* time
      - set the *TimeCreated* time

We gathered these projects into a list and invoked the `writeXml` method.
After that, we manually parse the XML file, comparing the attributes written with the ones that were initially created.

```java
public void testWriteXml_ProjectList_ShouldWriteAllProjects() throws TransformerConfigurationException, IOException, SAXException, ParserConfigurationException, ProjectException {
    (...)
    
    // There is actually three projects in the XML file
    assertEquals(3, nl.getLength());
    
    // Project 1 (running)
    Element e = (Element) nl.item(0);
    String running = e.getElementsByTagName("running").item(0).getFirstChild().getNodeValue();
    // Even though project has started, the program writes "no" to the XML file, because in a new execution every project must be paused
    assertEquals("no", running);

    String notes = e.getElementsByTagName("notes").item(0).getFirstChild().getNodeValue();
    assertEquals("A quick note", notes);

    // Project 2 (checked)
    e = (Element) nl.item(1);

    String checked = e.getElementsByTagName("checked").item(0).getFirstChild().getNodeValue();
    assertEquals("yes", checked);

    NodeList pnl = e.getElementsByTagName("color");
    if (pnl.getLength() != 0) {
    e = (Element) pnl.item(0);
    int r = Integer.parseInt(e.getAttribute("red"));
    int g = Integer.parseInt(e.getAttribute("green"));
    int b = Integer.parseInt(e.getAttribute("blue"));
    int a = Integer.parseInt(e.getAttribute("alpha"));

    assertEquals(new Color(r, g, b, a), new Color(3, 145, 255));
    }

    // Project 3 (times)
    e = (Element) nl.item(2);

    int secondsToday = Integer.parseInt(((Element) e.getElementsByTagName("time").item(0)).getAttribute("today"));
    int secondsOverall = Integer.parseInt(((Element) e.getElementsByTagName("time").item(0)).getAttribute("overall"));

    assertEquals(440, secondsToday);
    assertEquals(3600, secondsOverall);

    long created = Long.parseLong(e.getElementsByTagName("created").item(0).getFirstChild().getNodeValue());
    assertEquals(Date.from(Instant.parse("2018-10-16T00:00:00.000Z")), new Date(created));
}
```

After writing this code for the first time, we found that one of the situations failed: for the first project, which we started, the program wrote "no" in the "running" tag.
The reason for this came from the source code, namely this line, in which the author had commented on the functioning that we consider normal, and always considered the value "no" for this tag.

```java
addXmlElement(hd, "running", null, "no" /*p.isRunning() ? "yes" : "no"*/);
```

Upon thinking a little about why he would do this, we concluded that it would be so that, in a new execution of the program, all projects would start in a paused state, even if the end of the previous execution had occurred while one of them was running.
With that in mind, we changed our test to check for a "no" on this tag, so at this point, all tests pass.
However, we recommend removing the "running" tag from the XML logic, taking into account that the program, when reading the projects from the file, may simply consider its state as paused.

![All tests of the method `writeXml` pass successfully](./images/cp_tests5.png)

-----

#### Group 10

- Hugo Guimarães, up201806490
- Paulo Ribeiro, up201806505

#### Sources

- [Class Slides - Prof. José Campos](https://paginas.fe.up.pt/~jcmc/tvvs/2022-2023/lectures/lecture-2.pdf)
- [Equivalence Class Partitioning - ArtOfTesting](https://artoftesting.com/equivalence-class-partitioning)
- [Unit Test Naming Conventions](https://dzone.com/articles/7-popular-unit-test-naming)
- [Parametrized Tests - Baeldung](https://www.baeldung.com/parameterized-tests-junit-5)