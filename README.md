# miniPython-Compiler-Implementation
This project implements a compiler for the miniPython language, which is a subset of Python. The project involves four main phases: lexical analysis, syntactic analysis, abstract syntax trees (AST), and semantic analysis using symbol tables. The implementation uses the SableCC tool to facilitate the construction of the compiler.

Features:

  Lexical Analysis: Tokenizes the miniPython code using SableCC.
  
  Syntactic Analysis: Parses the miniPython code to ensure it adheres to the grammar rules.
  
  Abstract Syntax Trees (AST): Constructs ASTs to represent the structure of the code.
  
  Symbol Table and Semantic Analysis: Performs semantic analysis to validate variable declarations, scopes, and types.

Implementation Details:

  Supported Commands:
    Assignment of integers, floats, and strings to variables.
    Assignment of lists to variables.
    Function declarations with simple arguments and default values.
    If, print, while, and for statements.
    Function calls.
    Single-line comments.
