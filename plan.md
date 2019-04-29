
# Project Plan

As there are many of the features of java that are already implemented,
time will be split going forward between optimizations, code refactoring, and implementing
java language functionality.

The content within these sections is ordered, with first items being
more useful, and/or easy to implement.


## Java Features that still need implemented
 - unary operators (~, !, -)
 - integer math (bitwise and multiplicative), 
   addition and subtraction are already implemented.
 - array literals (`new int[]{1, 4, 2, 6 }`)
 - underscores in numerical literals
 - floating point math
 - For in loops
 - boolean logic
 - class field hierarchies
 - virtual function tables
 - switch statements (jump tables)
 - access modifiers (everything's treated as public)
 - synchronized modifier
 - final modifier
 - native modifier
 - generics
 - exception handling
 - lambdas
 - method references
 - nested classes
 - annotations

## Code cleanup
 - refactor intermediate type checking to return the list
   of new instructions to perform type conversion 
   (reduce potential problems with data filled in during type checking, used later)
 - find a better naming convention of the 3 different Register classes

## Optimizations

These will probably only be done at the intermediate level and pseudo levels.
These two levels should cover almost all of the possible optimizations.

At the tree level, there's not enough information to perform many optimizations easily,
and at the allocated instructions level, optimizations would be really complicated.

Due to some parts implemented in x64 that are not part of the intermediate language 
and the extra information at the intermediate level, some optimizations will be easier at
different spots. Also, intermediate level optimizations should be possible on all platforms,
while instruction level ones are specific to x64.



These are some optimizations that are left to be implemented:
  - jumps/branches to labels with jump statements
  - dead code removal
    - variables assigned but not used
    - functions called which don't have side effects and aren't used
  - common subexpression elimination
  - copy propagation - both intermediate and x64 levels
  - RISC -> CISC optimizations
    - group instructions, ex: array indexing operations
    - also can use lea (load effective address) for math
  - constant folding
    - requires both copy propagation and dead code removal
    - plus the contraction of operations on constants
  - final static fields as immediates in instructions
  - boolean expression optimization
    - with || and && expressions reducing the branches and jumps
    - conditional moves in the x64
    - again depends on side effects of the boolean expressions
  - mathematical identities
    - focus on 0, 1, and also reductions based on associativity (x + 1 + 2x => 3x + 1)
  - loop optimizations
    - completely unroll small loops with fixed size (like 10 iterations)
    - move constant parts of conditions (list size if unchanging, array length, ...)
    - move constant parts of loop bodies out: loop { x\[i\] = i * a + b * c, move b*c out.
    - unroll partially to take advantage of SIMD operations if possible
      - requires one iteration not dependent on previous ones
      - only works for loops like (for int i = 0; i < C; i++), where C doesn't change in loop body
  - instruction scheduling
    - re-order instructions where possible to avoid pipeline stalls
    - the x64 processor pipelines are deep
    - dependent on exact CPU type (i5 vs i7 vs K8 vs K10) as to the specifics
    - might require benchmarking
  - tail recursion -> iteration
    - reduces stack space requirements and less push/pop of return addresses
  - inline functions when results are likely to perform better
    - overhead of calling a function is reduced
    - increases code size = cache misses = slower though
    - result is only to inline small enough methods
    - very useful for functions that just call another one with slightly different args
    


These ones are already performed:
  - (None yet)
