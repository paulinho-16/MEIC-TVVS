# Assignment 4 - Model-based Testing (Black-box Testing)

// TODO: introdução -> explicar Model-based Testing e conceitos relacionados (state machine, transition tree, etc) + dizer coisas gerais do report (explicar QF-Test, etc)


Model Base Testing is a black-box testing technique in which a model of a software system is used to help systematically deriving tests for that system

(Falar de models em si?)
Models are simplifications of the real system that allow testing specific properties, making it easier to fragment the tests and evaluate small individual instances of the project.

The behaviour of each model was analyzed using:
- State Machines - To display all possible states of each model, as well as the available actions that result into a change of state. 
- Transition Trees - To display all possible paths of exucution in the system
- Transition Tables - As an alternative tabular way to display a State Machine.

Afterwards, QF-Test was used as a software tool to test the behaviour of each available path in each chosen model.
This testing tool is able to simulate a specific chain of actions on the assignment's Graphical User Interface, as well as asserting conditions for expected outputs.

// TODO: falar da config do QF_Test, metemos working directory para ele ir buscar os projetos já existentes

To try out these techniques, three use cases were selected and tested following a black-box approach.

### 1) Use Case 1: Add and Delete Projects

#### Description

// TODO: explicar use case e dizer o pq de o escolhermos

#### *Model-based Testing*

1. **State Machine**

// TODO: Meter diagrama e explicá-lo

![Use Case 1 State Machine](./images/state_machine1.png)

2. **Transition Tree**

// TODO: Meter diagrama e explicá-lo

![Use Case 1 Transition Tree](./images/transition_tree1.png)

3. **Transition Table**

// TODO: Meter tabela e explicá-la

![Use Case 1 Transition Table](./images/transition_table1.png)

#### ***QF-Test*** tests

// TODO: enumerar os testes derivados e falar da sua implementação no QF-Test, e também falar do outcome e explicá-lo

// TODO: falar do sneak path deste use case e da sua implementação

Neste caso ->>>> sneak path pode ser dar add project em edit mode, que funciona

### 2) Use Case 2: Edit project attributes

#### Description

// TODO: explicar use case e dizer o pq de o escolhermos

#### *Model-based Testing*

1. **State Machine**

// TODO: Meter diagrama e explicá-lo

![Use Case 2 State Machine](./images/state_machine2.png)

2. **Transition Tree**

// TODO: Meter diagrama e explicá-lo

![Use Case 2 Transition Tree](./images/transition_tree2.png)

3. **Transition Table**

// TODO: Meter tabela e explicá-la

![Use Case 2 Transition Table](./images/transition_table2.png)

#### ***QF-Test*** tests

// TODO: enumerar os testes derivados e falar da sua implementação no QF-Test, e também falar do outcome e explicá-lo

// TODO: falar do sneak path deste use case e da sua implementação

### 3) Use Case 3: Start and stop a project

#### Description

// TODO: explicar use case e dizer o pq de o escolhermos

#### *Model-based Testing*

1. **State Machine**

// TODO: Meter diagrama e explicá-lo

![Use Case 1 State Machine](./images/state_machine3.png)

2. **Transition Tree**

// TODO: Meter diagrama e explicá-lo

![Use Case 3 Transition Tree](./images/transition_tree3.png)

3. **Transition Table**

// TODO: Meter tabela e explicá-la

![Use Case 3 Transition Table](./images/transition_table3.png)

#### ***QF-Test*** tests

// TODO: enumerar os testes derivados e falar da sua implementação no QF-Test, e também falar do outcome e explicá-lo

// TODO: falar do sneak path deste use case e da sua implementação

## ***QF-Test*** tool feedback

// TODO: meter feedback do QF-Test (opinião, coisas a melhorar)

-----

#### Group 10

- Hugo Guimarães, up201806490
- Paulo Ribeiro, up201806505

#### Sources

- [Class Slides - Prof. José Campos](https://paginas.fe.up.pt/~jcmc/tvvs/2022-2023/lectures/lecture-4.pdf)
- [Online Search - QF-Test](https://www.qfs.de/en/search-results.html)