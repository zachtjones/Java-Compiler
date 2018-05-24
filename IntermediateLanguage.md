# Java Compiler Intermediate language specification
For this project, I decided to define my own intermediate language.
The intermediate language files will be named using the fully qualified name, with the .jil extension.

## Language details
- Case-sensitive -- varname is not the same as VarName
- standard ASCII text
- one file is generated per class / interface / enum
- there are an unlimited number of registers
- can assume registers will be saved / restored properly

## Tokens in the language

Registers
- ByteReg := "%b" &lt;Integer&gt;
- ShortReg := "%s" &lt;Integer&gt;
- IntegerReg := "%i" &lt;Integer&gt;
- LongReg := "%l" &lt;Integer&gt;
- FloatReg := "%f" &lt;Integer&gt;
- DoubleReg := "%d" &lt;Integer&gt;
- ReferenceReg := "%r" &lt;Integer&gt;

Types
- Void := "void"
- Byte := "byte"
- Short := "short"
- Int := "int"
- Long := "long"
- Float := "float"
- Double := "double"
- Reference := "reference"

Constants
- IntegerLiteral := [0-9]+
- FloatLiteral := *Same as Java*

File structure
- Structure := "structure"
- Static := "static"
- Instance := "instance"
- Function := "function"
- End := "end"
- Call := "call"
- Return := "return"
- Returns := "returns"
- Identifier := \[ a-z \]\[ A-Z \]\[ \_ \]\[ \. \] \*
- Label := &lt;Identifier&gt; ":"
- Semicolon := ";"

Operation tokens
- Assign := "assign"
- Set := "set"
- Assignment := "="
- Add := "+"
- Subtract := "-"
- Multiply := "\*"
- Divide := "/"
- Modulus := "%"
- And := "&"
- Or := "|"
- Xor := "^"
- Not := "!"
- BitInvert := "~"
- Move := "move"
- To := "to"

Control flow
- Branch := "branch"
- Greater := "greater"
- Less := "less"
- GreaterEqual := "greaterEqual"
- LessEqual := "lessEqual"
- EqualsTest := "=="
- NotEqualsTest := "!="
- When := "when"
- Signed := "signed"
- Unsigned := "unsigned"
- Jump := "jump"

Memory
- Store := "store"
- Load := "load"

## Syntax
```
File -> ( StructureDeclaration )* ( FunctionDeclaration )* <EOF>

StructureDeclaration -> "structure" ( "instance" | "static" ) ( Member )* "end" "structure"

Member -> Type <Identifier> ";"

Type -> "byte" | "short" | "int" | "long" | "float" | "double" | "reference"

FunctionDeclaration -> "function" ( "instance" | "static" ) "returns" Type ( Statement )* "end" "function"

Statement -> [ <Label> ] ( SetStatement | AssignStatement | MoveStatement | ControlStatement ) ";"

SetStatement -> "set" Register "=" Constant

AssignStatement -> "assign" Register "=" Expression

MoveStatement -> "move" Register "to" Register

ControlStatement -> JumpStatement | BranchStatement | CallStatement | ReturnStatement | MemoryStatement

MemoryStatement -> LoadStatement | StoreStatement

LoadStatement -> "load" Register "from" ( Label | Register )

StoreStatement -> "store" Register "at" ( Label | Register )

JumpStatement -> "jump" ( Label | Register )

BranchStatement -> "branch" "when" Condition "to" ( Label | Register)

Condition -> ( Register | Constant ) ConditionCode ( Register | Constant )

ConditionCode -> ("greater" | "less" | "greaterEqual" | "lessEqual" | "==" | "!=" ) ("unsigned" | "signed")

CallStatement -> "call" MethodReference Arguments "to" Register

MethodReference -> ("static" | "instance") <Identifier>

ReturnStatement -> "return" (Register | Constant)

Expression -> BinaryExpresssion | UnaryExpression

BinaryExpression -> Register BinaryOperator Register

BinaryOperator -> "+" | "-" | "*" | "/" | "%" | "&" | "|" | "^"

UnaryExpression -> UnaryOperator Register

UnaryOperator -> "-" | "~" | "!"

Register -> <ByteReg> | <ShortReg> | <IntegerReg> | <LongReg> | <FloatReg> | <DoubleReg> | <ReferenceReg>

Constant -> <IntegerLiteral> | <FloatLiteral> | <Identifier>
```
