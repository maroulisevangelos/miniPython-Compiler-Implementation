import java.io.*;
import minipython.lexer.Lexer;
import minipython.parser.Parser;
import minipython.node.*;
import java.util.*;

@SuppressWarnings("unchecked")
/*
	Commands:
	sablecc minipython.grammar
	javac ParserTest1.java
	java ParserTest1 minipythonexample.py
*/
public class ParserTest1
{
  public static void main(String[] args)
  {
    try
    {
      Parser parser =
        new Parser(
        new Lexer(
        new PushbackReader(
        new FileReader(args[0].toString()), 1024)));

      Hashtable symtable =  new Hashtable();	
      Start ast = parser.parse();
	  myvisitor1 test1 = new myvisitor1(symtable);
      ast.apply(test1);	//run the first test
	  int errors1 =  test1.errors;	//save errors
	  System.out.println("First Test --> Errors = " + errors1);	//print result
	  if (errors1==0){
		  myvisitor2 test2 = new myvisitor2(symtable);
		  ast.apply(test2);	//run the second test
		  int errors2 = test2.errors;	//save errors
		  System.out.println("Second Test --> Errors = " + errors2);	//print result
		  if (errors1==0 && errors2==0){	//print message for the user
			  System.out.println("Congratulations! There isn't any mistake!");
		  }else {
			  System.out.println("Correct your mistakes!");
		  }
	  }else {
		  System.out.println("Correct your mistakes in order to run also the second test!");
	  } 
	  
    }
    catch (Exception e)
    {
      System.err.println(e);
    }
  }
}