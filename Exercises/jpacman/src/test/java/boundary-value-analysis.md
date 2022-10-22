# 1. Boundary Value Analysis

According to the partitions resulting from the application of the *Category Partition* algorithm, I defined for each function the values to be tested, taking into account the *Boundary Value Analysis*.

### 1.1 squareAt in the nl.tudelft.jpacman.board.Board class

1. Regarding `x`, we have three possible partitions, where `board_width` is the width of the game board:
    - **E1**: `x` &lt; 0 (invalid)
    - **E2**: 0 &le; `x` &lt; `board_width` (valid)
    - **E3**: `x` &ge; `board_width` (invalid)

In the cases to be tested, we consider that `board_width` is 3.
The three chosen test cases corresponding to *in-points* of each partition are -1, 2 and 4.
For the first boundary (between **E1** and **E2**), we get the *on-point* 0 and the *off-point* 1.
For the second boundary (between **E2** and **E3**), we get the *on-point* 3 and the *off-point* 2.

2. Regarding `y`, we have three possible partitions, where `board_height` is the height of the game board:
   - **E1**: `y` &lt; 0 (invalid)
   - **E2**: 0 &le; `y` &lt; `board_height` (valid)
   - **E3**: `y` &ge; `board_height` (invalid)

In the cases to be tested, we consider that `board_height` is 3.
The three chosen test cases corresponding to *in-points* of each partition are -1, 2 and 4.
For the first boundary (between **E1** and **E2**), we get the *on-point* 0 and the *off-point* 1.
For the second boundary (between **E2** and **E3**), we get the *on-point* 3 and the *off-point* 2.

### 1.2 createBoard in the nl.tudelft.jpacman.board.BoardFactory class

1. Regarding `grid`, we have two possible partitions:
   - **E1**: `grid` is null
   - **E2**: `grid` is a two-dimensional array

For these two partitions, we get the following *in-points*, *on-points* and *off-points*:
   - **E1**: the null value is both the *in-point* and *on-point*, and the *off-point* belongs to the other partition (might be an empty two-dimensional array, for example)
   - **E2**: both the *in-point* and *on-point* could be any of the examples belonging to this partition, let us choose an empty two-dimensional array, and the *off-point* is the null value

### 1.3 createLevel in the nl.tudelft.jpacman.level.LevelFactory class

1. Regarding `board`, we have two possible partitions:
   - **E1**: `board` is null
   - **E2**: `board` is an object of type `Board`

For these two partitions, we get the following *in-points*, *on-points* and *off-points*:
   - **E1**: the null value is both the *in-point* and *on-point*, and the *off-point* belongs to the other partition (might be a mock object of the `Board` class, for example)
   - **E2**: both the *in-point* and *on-point* could be any of the examples belonging to this partition, let us choose a mock object of the `Board` class, and the *off-point* is the null value

2. Regarding `ghosts`, we have two possible partitions:
   - **E1**: `ghosts` is null
   - **E2**: `ghosts` is a list whose elements are of type `Ghost`

For these two partitions, we get the following *in-points*, *on-points* and *off-points*:
   - **E1**: the null value is both the *in-point* and *on-point*, and the *off-point* belongs to the other partition (might be a list with three mock objects of the `Ghost` class, for example)
   - **E2**: both the *in-point* and *on-point* could be any of the examples belonging to this partition, let us choose a list with three mock objects of the `Ghost` class, and the *off-point* is the null value

3. Regarding `startPositions`, we have two possible partitions:
   - **E1**: `startPositions` is null
   - **E2**: `startPositions` is a list whose elements are of type `Square`

For these two partitions, we get the following *in-points*, *on-points* and *off-points*:
   - **E1**: the null value is both the *in-point* and *on-point*, and the *off-point* belongs to the other partition (might be an empty list, for example)
   - **E2**: both the *in-point* and *on-point* could be any of the examples belonging to this partition, let us choose an empty list, and the *off-point* is the null value

### 1.4 makeGhostSquare in the nl.tudelft.jpacman.level.MapParser class

1. Regarding `ghosts`, we have two possible partitions:
   - **E1**: `ghosts` is null
   - **E2**: `ghosts` is a list whose elements are of type `Ghost`

For these two partitions, we get the following *in-points*, *on-points* and *off-points*:
   - **E1**: the null value is both the *in-point* and *on-point*, and the *off-point* belongs to the other partition (might be a list with three objects of the `Ghost` class, for example)
   - **E2**: both the *in-point* and *on-point* could be any of the examples belonging to this partition, let us choose a list with three objects of the `Ghost` class, and the *off-point* is the null value

2. Regarding `ghost`, we have two possible partitions:
   - **E1**: `ghost` is null
   - **E2**: `ghost` is an object of type `Ghost`

For these two partitions, we get the following *in-points*, *on-points* and *off-points*:
   - **E1**: the null value is both the *in-point* and *on-point*, and the *off-point* belongs to the other partition (might be an object of type `Ghost`, for example)
   - **E2**: both the *in-point* and *on-point* could be any of the examples belonging to this partition, let us choose an object of type `Ghost`, and the *off-point* is the null value

### 1.5 draw in the nl.tudelft.jpacman.sprite.ImageSprite class

1. Regarding `graphics`, we have two possible partitions:
   - **E1**: `graphics` is null
   - **E2**: `graphics` is an object of type `Graphics`

For these two partitions, we get the following *in-points*, *on-points* and *off-points*:
   - **E1**: the null value is both the *in-point* and *on-point*, and the *off-point* belongs to the other partition (might be an object of type `Graphics`, for example)
   - **E2**: both the *in-point* and *on-point* could be any of the examples belonging to this partition, let us choose an object of type `Graphics`, and the *off-point* is the null value

2. Regarding `x`, we have three possible partitions, where `board_width` is the width of the game board:
   - **E1**: `x` &lt; 0 (invalid)
   - **E2**: 0 &le; `x` &lt; `board_width` (valid)
   - **E3**: `x` &ge; `board_width` (invalid)

In the cases to be tested, we consider that `board_width` is 3.
The three chosen test cases corresponding to *in-points* of each partition are -1, 2 and 4.
For the first boundary (between **E1** and **E2**), we get the *on-point* 0 and the *off-point* 1.
For the second boundary (between **E2** and **E3**), we get the *on-point* 3 and the *off-point* 2.

3. Regarding `y`, we have three possible partitions, where `board_height` is the height of the game board:
   - **E1**: `y` &lt; 0 (invalid)
   - **E2**: 0 &le; `y` &lt; `board_height` (valid)
   - **E3**: `y` &ge; `board_height` (invalid)

In the cases to be tested, we consider that `board_height` is 3.
The three chosen test cases corresponding to *in-points* of each partition are -1, 2 and 4.
For the first boundary (between **E1** and **E2**), we get the *on-point* 0 and the *off-point* 1.
For the second boundary (between **E2** and **E3**), we get the *on-point* 3 and the *off-point* 2.

4. Regarding `width`, we have three possible partitions, where `board_width` is the width of the game board:
   - **E1**: `width` &le; 0 (invalid)
   - **E2**: 0 &lt; `width` &le; `board_width` - `x` (valid)
   - **E3**: `width` &gt; `board_width` - `x` (invalid)

In the cases to be tested, we consider that `board_width` is 3.
Considering `x` = 0, the three chosen test cases corresponding to *in-points* of each partition are -1, 2 and 4.
For the first boundary (between **E1** and **E2**), we get the *on-point* 0 and the *off-point* 1.
For the second boundary (between **E2** and **E3**), we get the *on-point* 3 and the *off-point* 2.

5. Regarding `height`, we have three possible partitions, where `board_height` is the height of the game board:
   - **E1**: `height` &le; 0 (invalid)
   - **E2**: 0 &lt; `height` &le; `board_height` - `y` (valid)
   - **E3**: `height` &gt; `board_height` - `y` (invalid)

In the cases to be tested, we consider that `board_height` is 3.
Considering `y` = 0, the three chosen test cases corresponding to *in-points* of each partition are -1, 2 and 4.
For the first boundary (between **E1** and **E2**), we get the *on-point* 0 and the *off-point* 1.
For the second boundary (between **E2** and **E3**), we get the *on-point* 3 and the *off-point* 2.

After doing the unit tests for this last function, I found that they didn't pass the `x`, `y`, `width`, and `height` test cases because there is no control over these values in the code.