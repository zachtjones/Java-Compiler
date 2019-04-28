
## Optimizations (done)
The key to most of these optimizations is to keep the IL in SSA form.
SSA - Single static assignment - in function, only assign to register once.
This makes some optimizations work in linear time that wouldn't work otherwise.
- None yet

## Optimizations (planned)
- optimize out multiple reads to final variables
  - try to put final static fields in instructions
- Jumps to labels with jump statements
- Dead code elimination (use control graphs)
- Common subexpression elimination
  - involves copy and constant propagation
  - if functions/expressions are used, need to know if they are pure (no side effects)
- Return statement Optimizations
  - put return value into the correct register from the start
  - eliminates a bunch of copy register
- Constant folding
- Register allocation (Assembly - minimize save / restore operations needed)
- Register allocation (IL - use smaller numbers)
- RISC -> CISC Optimizations
  - grouping several instructions into one
- remove useless statements
  - code that assigns to a value not used & expression is side-effect free
- replace instructions with faster ones
  - x \*= 2 -> x << 1
  - x = 0 with x ^= x
  - and many more

## Optimizations (potential)
- Tail recursion -> iterative
- maybe even tail sibling calls
  - (calls to functions which take and return the same types as the caller)
- Loop Optimizations
  - loop unrolling for small # iteration loops
  - move parts of a condition out
    - (ex if going through string and length doesn't change)
  - move constant parts of expressions out of the loop
- instruction scheduling to avoid pipeline stalls
- minimize cache misses (might need to be done on high level code & involve benchmarks)

