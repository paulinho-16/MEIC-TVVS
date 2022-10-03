# 1. Category-Partition

### 1.1 squareAt in the nl.tudelft.jpacman.board.Board class

1) This method has two parameters:
    - `x`: the x position (column) of the requested square
    - `y`: the y position (row) of the requested square

2) For each parameter we define the characteristics as:

    - `x`: number within the range [0, board_width[
    - `y`: number within the range [0, board_height[

3) The number of characteristics and parameters is not too large in this case.
   Constraint: negative values or values greater or equal to the width/height of the board are not allowed.

4) Combining the other characteristics, we get the following tests:

    - `x` and `y` within the valid ranges
    - `x` within its valid range, but invalid `y` value
    - `y` within its valid range, but invalid `x` value

### 1.2 createBoard in the nl.tudelft.jpacman.board.BoardFactory class

1) This method has one parameter:

    - `grid`: the square grid of cells, in which grid[x][y] corresponds to the square at position x,y

2) For each parameter we define the characteristics as:

    - `grid`: must be a two-dimensional array, whose elements are of type Square
    
3) The number of characteristics and parameters is not too large in this case.
   Constraint: null grid arrays are not allowed.

4) Combining the other characteristics, we get the following tests:

    - `grid` is a two-dimensional array
    - `grid` is null

### 1.3 createLevel in the nl.tudelft.jpacman.level.LevelFactory class

1) This method has three parameters:

    - `board`: the board with all ghosts and pellets occupying their squares
    - `ghosts`: a list of all ghosts on the board
    - `startPositions`: a list of squares from which players may start the game

2) For each parameter we define the characteristics as:

    - `board`: must be an object of type Board
    - `ghosts`: must be a list whose elements are of type Ghost, can be an empty list in the case of a level with no ghost enemies
    - `startPositions`: must be a list whose elements are of type Square, can't be an empty list since the game must have at least one player, and all the positions must belong to the board

3) The number of characteristics and parameters is not too large in this case.
   Constraint: empty arrays for `startPositions` are not allowed, as well as arrays that contain positions that do not fit in the board. Furthermore, furthermore, none of the positions can be the same as the position of some ghost.

4) Combining the other characteristics, we get the following tests:

    - `startPositions` is not an empty list and every position is within the board and is not the same as the position of some ghost.
    - `startPositions` is an empty list
    - `startPositions` is not an empty list, but at least one of the positions in the array does not fit in the board
    - `startPositions` is not an empty list and every position is within the board, but at least one of the positions is the same as the position of some ghost.

### 1.4 makeGhostSquare in the nl.tudelft.jpacman.level.MapParser class

1) This method has two parameters:

    - `ghosts`: all the ghosts in the level so far, the new ghost will be appended
    - `ghost`: the newly created ghost to be placed

2) For each parameter we define the characteristics as:

    - `ghosts`: must be a list whose elements are of type Ghost, can be an empty list in the case there are no ghosts added to the level so far
    - `ghost`: must be an object of type Ghost

3) The number of characteristics and parameters is not too large in this case.
   Constraint: arrays for `ghosts` whose elements are not of type Ghost are not allowed, as well as objects for `ghost` that are not of this type.

4) Combining the other characteristics, we get the following tests:

    - `ghosts` is an array whose elements respect the array type and `ghost` is of type Ghost
    - `ghosts` is an array whose elements respect the array type, but `ghost` is not of type Ghost
    - `ghost` is of type Ghost, but `ghosts` is an array whose elements do not respect the array type

### 1.5 draw in the nl.tudelft.jpacman.sprite.ImageSprite class

1) This method has five parameters:

    - `graphics`: the graphics context to draw
    - `x`: the destination x coordinate to start drawing
    - `y`: the destination y coordinate to start drawing
    - `width`: the width of the destination draw area
    - `height`: the height of the destination draw area

2) For each parameter we define the characteristics as:

    - `graphics`: must be an object of type Graphics
    - `x`: number within the range [0, board_width[
    - `y`: number within the range [0, board_height[
    - `width`: positive number (width > 0), the sum `x` + `width` must not surpass the board width
    - `height`: positive number (height > 0), the sum `y` + `height` must not surpass the board height

3) The number of characteristics and parameters is not too large in this case.
   Constraint: `x` and `y` values that do not fit in the valid ranges are not allowed, as well as negative/zero `width` and `height` values.
                `width` and `height` values so that the sums `x` + `width` and `y` + `height` surpass the board width or height, respectively, are not allowed.

4) Combining the other characteristics, we get the following tests:

    - `x` and `y` are within the valid ranges and `width` and `height` are positive numbers so that the sums `x` + `width` and `y` + `height` do not surpass the board width or height, respectively
    - `x` is not within the range [0, board_width[
    - `y` is not within the range [0, board_height[
    - `width` is a negative/zero value
    - `height` is a negative/zero value
    - `x`, `y`, `width` and `height` are within their ranges, but at least one of the sums `x` + `width` and `y` + `height` surpasses the board width or height, respectively