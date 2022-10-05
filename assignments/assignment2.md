# Assignment 2 - Equivalence Class Partitioning / Category Partition (Black-box Testing)

**Equivalence Class Partitioning** is a black-box testing technique in which we group the input data into logical partitions called equivalence classes.
All the data items lying in an equivalence class are assumed to be processed in the same way by the software application to be tested when passed as input.
So, instead of testing all the combinations of input test data, we can pick and pass any of the test data from a particular equivalence class to the application and assume that the application will behave in the same way for the other test data of that class.
This process greatly reduces the number of test cases maintaining the same test coverage, being, therefore, perfectly suitable for software projects with time and resource constraints.

The **Category Partition** method consists of a systematic way of deriving these partitions, based on the characteristics of the input parameters.

To try out these techniques, we selected five different functions from the package `de.dominik_geyer.jtimesched.project` of the *jTimeSched* project.
For each one, we present the reason we decided to test it and its purpose.
Next, we apply the **Category Partition** algorithm and describe the unit tests we generated based on this method, as well as their outcomes.

We tried our best to follow a black-box testing approach, even though the [documentation](https://paginas.fe.up.pt/~jcmc/tvvs/2022-2023/assignments/jtimesched-javadoc/index.html) of the project at hand is non-existent, limited only to the signature of the functions.
For this reason, we were forced to resort to source code to understand the purpose of some functions, but even in these cases, we tried to just focus on their purpose and not on their functioning, to avoid being influenced when writing the tests.

// TODO: apagar isto:
1. Which functions have you selected for testing and why.
1. What is the purpose of each function.
1. Step-by-step of the ‘Category-Partition’ algorithm for each function.
1. Brief description of the unit tests generated for each category.
1. Brief description of the outcome of each unit test and whether any test results in a failure (and why)

### Function 1

start()

#### Description

// TODO: why this function and description

#### *Category-Partition* algorithm

// TODO: apply algorithm

#### Unit Tests

// TODO: description of unit test and outcome

### Function 2

adjustSecondsToday(int secondsToday)

#### Description

// TODO: why this function and description

#### *Category-Partition* algorithm

// TODO: apply algorithm

#### Unit Tests

// TODO: description of unit test and outcome

### Function 3

parseSeconds(String strTime)

#### Description

// TODO: why this function and description

#### *Category-Partition* algorithm

// TODO: apply algorithm

#### Unit Tests

// TODO: description of unit test and outcome

### Function 4

setValueAt(Object value, int row, int column)

#### Description

// TODO: why this function and description

#### *Category-Partition* algorithm

// TODO: apply algorithm

#### Unit Tests

// TODO: description of unit test and outcome

### Function 5

writeXml(List<Project> projects)

#### Description

// TODO: why this function and description

#### *Category-Partition* algorithm

// TODO: apply algorithm

#### Unit Tests

// TODO: description of unit test and outcome

-----

#### Group 10

- Hugo Guimarães, up201806490
- Paulo Ribeiro, up201806505

#### Sources

- [Class Slides - Prof. José Campos](https://paginas.fe.up.pt/~jcmc/tvvs/2022-2023/lectures/lecture-2.pdf)
- [Equivalence Class Partitioning - ArtOfTesting](https://artoftesting.com/equivalence-class-partitioning)