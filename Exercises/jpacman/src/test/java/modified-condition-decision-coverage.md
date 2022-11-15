# Structural/Logical Testing (Condition coverage, Path coverage, Modified Condition/Decision Coverage (MC/DC))

## 1. Modified Condition/Decision Coverage (MC/DC)

### 1.1 withinBorders function in the nl.tudelft.jpacman.board.Board class

To test this program, we first use the truth table for `x >= 0 && x < getWidth() && y >= 0 && y < getHeight()` to see all the combinations and their outcomes.
In this case, we have 4 decisions and 2^4 is 16, therefore we have tests that go from 1 to 16:

| Tests | `x >= 0` | `x < getWidth()` | `y >= 0` | `y < getHeight()` | Decision |
|:-----:|:--------:|:----------------:|:--------:|:-----------------:|:--------:|
|   t1  |     T    |         T        |     T    |         T         |     T    |
|   t2  |     T    |         T        |     T    |         F         |     F    |
|   t3  |     T    |         T        |     F    |         T         |     F    |
|   t4  |     T    |         T        |     F    |         F         |     F    |
|   t5  |     T    |         F        |     T    |         T         |     F    |
|   t6  |     T    |         F        |     T    |         F         |     F    |
|   t7  |     T    |         F        |     F    |         T         |     F    |
|   t8  |     T    |         F        |     F    |         F         |     F    |
|   t9  |     F    |         T        |     T    |         T         |     F    |
|  t10  |     F    |         T        |     T    |         F         |     F    |
|  t11  |     F    |         T        |     F    |         T         |     F    |
|  t12  |     F    |         T        |     F    |         F         |     F    |
|  t13  |     F    |         F        |     T    |         T         |     F    |
|  t14  |     F    |         F        |     T    |         F         |     F    |
|  t15  |     F    |         F        |     F    |         T         |     F    |
|  t16  |     F    |         F        |     F    |         F         |     F    |

Our goal will be to apply the **MC/DC** criterion to these test cases and select N+1, in this case, 4+1=5, tests.

The **independence pairs** for each of the conditions are:

- `x >= 0`: {t1, t9}
- `x < getWidth()`: {t1, t5}
- `y >= 0`: {t1, t3}
- `y < getHeight()`: {t1, t2}

Therefore, the tests that we need for 100% **MC/DC** coverage are {t1, t2, t3, t5, t9}.
These are the only 5 tests we need.
This is indeed cheaper when compared to the 16 tests we would need for path coverage.

### 1.2 start function in the nl.tudelft.jpacman.game.Game class

Both `if` statements convert to the condition: `!isInProgress() && getLevel().isAnyPlayerAlive() && getLevel().remainingPellets() > 0`
To test this program, we first use the truth table for that condition to see all the combinations and their outcomes.
In this case, we have 3 decisions and 2^3 is 8, therefore we have tests that go from 1 to 8:

| Tests | `!isInProgress()` | `getLevel().isAnyPlayerAlive()` | `getLevel().remainingPellets() > 0` | Decision |
|:-----:|:----------------:|:-------------------------------:|:-----------------------------------:|:--------:|
|   t1  |         T        |                T                |                  T                  |     T    |
|   t2  |         T        |                T                |                  F                  |     F    |
|   t3  |         T        |                F                |                  T                  |     F    |
|   t4  |         T        |                F                |                  F                  |     F    |
|   t5  |         F        |                T                |                  T                  |     F    |
|   t6  |         F        |                T                |                  F                  |     F    |
|   t7  |         F        |                F                |                  T                  |     F    |
|   t8  |         F        |                F                |                  F                  |     F    |

Our goal will be to apply the **MC/DC** criterion to these test cases and select N+1, in this case, 3+1=4, tests.

The **independence pairs** for each of the conditions are:

- `!isInProgress()`: {t1, t5}
- `getLevel().isAnyPlayerAlive()`: {t1, t3}
- `getLevel().remainingPellets() > 0`: {t1, t2}

Therefore, the tests that we need for 100% **MC/DC** coverage are {t1, t2, t3, t5}.
These are the only 4 tests we need.
This is indeed cheaper when compared to the 8 tests we would need for path coverage.

## 2. Unit Tests

The unit tests developed for these two test sets are in the `BoardTest` and `GameTest` files, respectively.