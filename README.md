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

Phase A: Lexical and Syntactic Analysis

  Lexical Analysis:

    Implemented using SableCC.
    
    Grammar defined in minipython.grammar.
    
    Execution Commands:

      sablecc minipython.grammar
      javac LexerTest1.java
      java LexerTest1 minipythonexample.py
      
    Generates tokens separated by spaces.

  Syntactic Analysis:

    Checks the syntax of the miniPython code against the defined grammar.

    Execution Commands (ParserTest2.java can also be used):
  
      javac ParserTest1.java
      java ParserTest1 minipythonexample.py

      
Phase B: AST and Semantic Analysis
  Abstract Syntax Trees (AST):
  
    Constructs ASTs to represent the structure of the code.
    
    Execution Commands:
    
    javac ASTTest1.java
    java ASTTest1 example.py
    
  Symbol Table and Semantic Analysis:
  
    Uses visitors (DepthFirstAdapter) to traverse the AST and build the symbol table.
    
    Performs checks for undeclared variables, function calls, incorrect argument definitions, type mismatches, and other semantic errors.
    
    Execution Commands (myvisitor2.java can also be used):
    
      javac myvisitor1.java
      java myvisitor1 example.py


BNF for miniPython: The grammar for miniPython is defined using BNF and includes rules for functions, statements, expressions, comparisons, function calls, and values.

Execution Instructions:

  Ensure java and javac are in the PATH environment variable.
  
  Execute from the working directory of the project.

Files Included:

  SableCC Grammar File (minipython.grammar): Defines the grammar for lexical and syntactic analysis.
  
  Java Test Files: Includes LexerTest1.java, ParserTest1.java (ParserTest2.java), and ASTTest1.java to test lexical analysis, syntactic analysis, and AST construction.
  
  Symbol Table Implementation (myvisitor1.java, myvisitor2.java): Visitor class for semantic analysis and symbol table construction.
  
  Generated Directories:

    analysis: Contains analysis classes.
    
    lexer: Contains lexical analysis classes.
    
    node: Contains AST node classes.
    
    parser: Contains parsing classes.


All necessary code and assets required for this project are included in this repository.
