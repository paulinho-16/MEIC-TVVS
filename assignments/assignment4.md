# Assignment 4 - Model-based Testing (Black-box Testing)

*Model-based Testing* is a black-box testing technique where the run time behaviour of the software under test is checked against predictions made by a model, which is a description of a system's behavior. The model can then help systematically deriving tests for that system.

To try out this technique, we thought of three different use cases of the *jTimeSched* project.
For each one, we present the reason we decided to test it and its purpose.
Afterwards, we apply *Model-based Testing*, by presenting their:
- *State Machine*: to display all possible states of the system, as well as the available actions that result into a change of state. 
- *Transition Tree*: to display all possible paths of execution in the system
- *Transition Table*: an alternative tabular way to display a State Machine, which allows a better visualization of the sneaky paths.

After these steps, we can derive the tests based on the existing paths and their expected behaviour. Based on the transition tree of each use case, we derive the regular paths to be tested, where each test case corresponds to a path from the root of the tree to one of the leaves.
Besides this, we can also test the sneak paths, which are related to the unspecified behaviour, as we need to evaluate how the system behaves in unexpected scenarios.

After these steps, **QF-Test** was used as a software tool to test the behaviour of each available path the system.
This testing tool is able to simulate a specific chain of actions on the assignment's Graphical User Interface, as well as asserting conditions for expected outputs.

Even though the [documentation](https://paginas.fe.up.pt/~jcmc/tvvs/2022-2023/assignments/jtimesched-javadoc/index.html) of the project at hand is non-existent, limited only to the signature of the functions, we were fully capable of following a black-box approach.
To develop the tests, we only needed to know how the program works and its main functionalities, which we could derive after some time of experimenting, therefore we avoided being influenced when writing the tests.

In order to start the **QF-Test** tests from an initial state with some existing projects, it was necessary to select the working directory for the program to find the configuration file containing some projects.

For each use case, we tested every regular path and we additionally tested three sneak paths, all from the second use case.

### 1) Use Case 1: Add and Delete Projects

#### Description

One of the main functionalities of the *jTimeSched* project is to add and delete projects. Therefore, it is important to create a model for testing this crucial functionality, since the mal-functioning of these functionalities could compromise the entire application.

#### *Model-based Testing*

1. **State Machine**

The *State Machine* diagram has the following states:
- **Start**: the initial state of the application, before any user interacion, which contains a table with some predefined projects
- **Edit**: the currently selected project title is being edited, this state happens by default when the user creates a new project
- **Created**: the state after a new project has been added to the table
- **Deleted** - the state after a project has been deleted from the table

It also contains the following events:
- **add**: the *Add Project* button has been pressed, and the user is prompted to enter a name for the project
- **save**: the project's name is saved after its edition, finishing the process of its creation after pressing *Enter*
- **delete** - a project is deleted by double-clicking the *Delete Project* icon

![Use Case 1 State Machine](./images/state_machine1.png)

The self-transition of **add** in the **Edit** state exists because the user can add a new project even before ending the title edition of the current project (without pressing *Enter*). When this happens, the previous project title is saved with the input right before this event, which, in the case of no edition from the user, is "New Project" by default, and we enter the **Edit** state of the new project. The other transitions are simple to understand and correspond to the expected program behaviour.

1. **Transition Tree**

The following figure contains a *Transition Tree* that represents all the *six* regular paths for the use case. As all of them were tested, they are detailed in the following subsection.

![Use Case 1 Transition Tree](./images/transition_tree1.png)

// TODO: trocar Edit_3 e Edit_2 e Deleted tb

1. **Transition Table**

Through the following *Transition Table*, it can be concluded that there are:
- 9 normal paths
- 3 sneak paths, even though in this case they are not worthy of testing as the **save** event can only happen on the **Edit** state.

![Use Case 1 Transition Table](./images/transition_table1.png)

#### ***QF-Test*** tests

// TODO: enumerar os testes derivados e falar da sua implementação no QF-Test, e também falar do outcome e explicá-lo

// TODO: falar do sneak path deste use case e da sua implementação

// TODO: talvez usar como sneaky a self-transition de add, mas se o fizermos mudar texto após o diagrama da state machine

### 2) Use Case 2: Edit Project attributes

#### Description

In this project, it is essential to be able to edit the many fields of a project, namely its checked state, title, colour, date of creation, time overall and time today. Additionaly, we discovered other edition functionalities: the user can add notes to a projet and enter time quotas for both the overall and today times.
This edition possibilities ensures more flexibility to the user, who may fix some mistakes in the times of the projects, for example. As such, we decided to test this use case, to guarantee consistency upon these edition actions.
This section aims to test a model associated with all the possible editions.

#### *Model-based Testing*

1. **State Machine**

The *State Machine* diagram has the following states:
- **Updated**: indicates that the table of projects is currently updated, with existing changes to a specific field of a project saved
- **Edit Text**: represents the state where a text field is being edited, such as the title of the project or the times
- **Edit Notes**: represents the state where the project's notes are being edited
- **New Quota**: represents the state where a *Time Quota* for a time field of the project, which works as an objective time to reach in the respective project, is being edited; this edition happens on a pop-up window
- **Edit Colour**: represents the state where the project's colour is being edited; consists of some predefined colour for quick selection and one option for accessing an advanced menu, moving to the following state
- **Custom Colour**: represents a specific state where an advanced menu for colour selection is being used, as a pop-up window; the available formats are Swatches (colour pallete), HSV, HSL, RGB and CMYK

It also contains the following events:
- **edit_checked**: pressing the checkbox that marks a project as checked/unchecked (finished/unfinished)
- **edit_title**: double clicking the title of the project to edit it 
- **edit_created**: double clicking project's creation date to edit it
- **edit_overall**: double clicking project's overall time to edit it
- **edit_today**: double clicking project's today time to edit it
- **edit_notes**: right click project's title cell to edit its notes
- **quota_overall**: right click project's overall time to edit its quota
- **quota_today**: right click project's today time to edit its quota
- **edit_colour**: click project's colour to edit it
- **custom_colour**: press the *custom colour* icon in the *edit_colour* bar to select a custom colour
- **ok**: confirming the edition of the field mentioned in *edit_notes*, *quota_overall*, *quota_today* and *custom_colour*, closing the respective pop-up windows
- **reset**: reset button for the *custom_colour* advanced menu, discarding the user picks and restauring the default colour
- **pick**: select a quick colour in the *edit_colour* bar
- **save**: saving changes after editing the text fields, done by pressing the *Enter* key

![Use Case 2 State Machine](./images/state_machine2.png)

// TODO: seta do edit_title está invertida e a do save também

The initial state is **Updated**, as the application starts with the predefined projects saved upon the last execution of the program. In the diagram we can see, for example, the possibility of choosing a quick colour (**pick**) or opening the custom colour menu.

1. **Transition Tree**

The following figure contains a *Transition Tree* which contains *seven* regular paths. As all of them were tested, they are detailed in the following subsection.

![Use Case 2 Transition Tree](./images/transition_tree2.png)

1. **Transition Table**

Through the following *Transition Table*, it can be concluded that there are:
- 16 normal paths
- 68 sneaky paths (we test three of them which we consider relevant in our context)

![Use Case 2 Transition Table](./images/transition_table2.png)

#### ***QF-Test*** tests

// TODO: enumerar os testes derivados e falar da sua implementação no QF-Test, e também falar do outcome e explicá-lo

// TODO: falar do sneak path deste use case e da sua implementação
// TODO: sneak paths:
 - editar tempos num projeto a correr
 - de edit text para new quota
 - um dos cancel?

### 3) Use Case 3: Start and Stop Projects

#### Description

Finally, we must test the purpose of the entire application: the capability of counting the time spent in each of the projects. The extreme importance of this functionality led us to choose testing this use case, despite its model being quite trivial. For example, we must ensure that only a project that is not already running can be started.

#### *Model-based Testing*

1. **State Machine**

The *State Machine* diagram has the following states:
- **Start**: the initial state of the application, in which all projects are paused
- **Running**: indicates that a project is running
- **Paused**: indicates that a running project has been paused

It also contains the following events:
- **start**: a project's start/stop icon has been pressed on a paused project
- **stop**: a project's start/stop icon has been pressed on a running project

![Use Case 3 State Machine](./images/state_machine3.png)

The diagram is simple but represents this entire functionality.

1. **Transition Tree**

The following figure contains a *Transition Tree* which contains only a single regular path, detailed in the following subsection.

![Use Case 3 Transition Tree](./images/transition_tree3.png)

1. **Transition Table**

Through the following table, it can be concluded that there are:
- 3 normal paths
- 3 sneak paths

![Use Case 3 Transition Table](./images/transition_table3.png)

#### ***QF-Test*** tests

// TODO: enumerar os testes derivados e falar da sua implementação no QF-Test, e também falar do outcome e explicá-lo

// TODO: falar do sneak path deste use case e da sua implementação

// TODO: falar que verificamos se as cells não são editáveis

// TODO: falar do "delay before" do clique do pause

## ***QF-Test*** tool feedback

// TODO: meter feedback do QF-Test (opinião, coisas a melhorar)

-----

#### Group 10

- Hugo Guimarães, up201806490
- Paulo Ribeiro, up201806505

#### Sources

- [Class Slides - Prof. José Campos](https://paginas.fe.up.pt/~jcmc/tvvs/2022-2023/lectures/lecture-4.pdf)
- [Model-based Testing - Guru99](https://www.guru99.com/model-based-testing-tutorial.html)
- [Online Search - QF-Test](https://www.qfs.de/en/search-results.html)