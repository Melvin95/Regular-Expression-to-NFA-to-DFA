package assignment;

import java.io.*;

/**
 * Class Lexer provides a wrapper class for the lexer class ASMLexer that
 * JFlex writes for the 8085 assembler.
 *
 * Lexer provides three methods
 *      scanner        - gets and consumes the next token, returning it as
 *                       a Token object
 *
 *      lookAhead      - gets but does not consume the next token, returning it
 *						 as a Token object
 *
 *		nextToken      - gets but does not consume the next token, returning 
 *                       only the token type
 */
 
public class  Lexer {

   // Instance variables	
   private RegLexer lexer;           // JFlex lexer
   private String   fName;           // Name of source file
   private Token    nextToken;       // Next token
   
   /**
    *  Constructs a new Lexer object to scan source file "fName" and gets the 
    *  first token.
    *
    *  If the file cannot be opened, the caller may be able to recover from 
    *  it,so the FileNotFoundException is thrown for the caller to deal with. 
    *
    *  If a file access error occurs in getting the first token, it is 
    *  unlikely that the caller can recover from it.  The exception is thus 
    *  caught here and the program is aborted
    *  
    */
   public Lexer(String fName) throws FileNotFoundException
   {   	 
   	    this.fName = fName;
   	    //lexer = new RegLexer(new FileReader(fName));  // Open source file 
   	    lexer = new RegLexer(new StringReader(fName));
        try {
          nextToken = lexer.yylex();                  // Get first token
 	    }
 	    catch (IOException e) {
 	  	   System.out.println("Access error while scanning file " + fName);
 	  	   System.out.println("Program aborted\n");
 	  	   e.printStackTrace();
 	  	   System.exit(1);
 	    }    	    
   }     
   
   /**
    *  Returns and consumes the next token, returning it as a Token object.
    *  If a file access error occurs, it is unlikely that the caller can 
    *  recover from it.  The exception is thus caught here and the 
    *  program is aborted
    */
   public Token scanner()    
   {  
      try {
        Token t = (Token) nextToken.clone();  // Save existing next token
        nextToken = lexer.yylex();            // Get new next token
        return t;                             // Return existing next token
 	  }
 	  catch (IOException e) {
 	  	System.out.println("Access error while scanning file " + fName);
 	  	System.out.println("Program aborted\n");
 	  	e.printStackTrace();
 	  	System.exit(1);
 	  	return null;  // Dummy return
 	  } 	  
   }	   
          
   /**
    *  Returns but does not consume the next token, returning it as a Token 
    *  object.
    */    
   public Token lookAhead()
   {
       return nextToken;
   } 
    
   /**
    *  Gets but does not consume the next token, returning the type of the 
    *  Token only. 
    */    
   public int nextToken() 
   {
     return nextToken.getType();
   } 
}	 
