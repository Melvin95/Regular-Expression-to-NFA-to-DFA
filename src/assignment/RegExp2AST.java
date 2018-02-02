package assignment;

import java.io.IOException;
import java.text.ParseException;

public class RegExp2AST {
	
   protected Token curToken;	    // Current token details, returned by scanner
   protected Token nextT;			// Next token details, returned by nextToken 
   protected Lexer lex;			    // Lexical analyser
   protected final int NEXT    = 0, // Current flag
                       CURRENT = 1; // Next flag
   
   public RegExp2AST(String fName) throws IOException  {
   	  lex = new Lexer(fName);
   }

   /** Turns the Lexer class method lookAhead into one which returns the
    *  type code of the token.  The token details are stored in the nextT
    *  variable
    */ 
   protected int nextToken() throws IOException  {
   	
   	 nextT = lex.lookAhead();
   	 return nextT.getType();
   }	 
 
   /*
    *  Displays an error message and the location at which the error occurs.
    *  This is determined from the stored tokens curToken or nextT depending
    *  on whether the error comes from the current or the next token.  Program
    *  then aborts.
    */    
   protected void error(String errMess,
                        int    errType)  throws ParseException {
      
	 switch (errType) {
   	  	case CURRENT : throw new ParseException(errMess,curToken.getColNo());
   	  	                 	  	                
  	  	case NEXT    : throw new ParseException(errMess,nextT.getColNo());
   	  	               
   	 } 	                     	                   	  	                
   }	                   

   /*
    *  Calls the scanner to get a token and compares it against an expected 
    *  token 'tokenType".  If the two match, nothing happens, but if they 
    *  don't, an error message is displayed and the program aborts
    */ 
   protected void match(int tokenType) throws IOException,ParseException {
   	
   	  curToken = lex.scanner();
   	  int curType = curToken.getType();
   	  if (curType != tokenType) {
   	  	String actualToken;
   	  	if (curType == Token.ERROR)
   	  	  actualToken = curToken.getString(); 
   	  	else
   	  	  actualToken = Token.getTypeName(curType);    	  	  
   	    error(Token.getTypeName(tokenType) + " expected but " + actualToken +
   	          " encountered",CURRENT);
   	  }           	          
   }
   
   /*
    *  Returns the built AST
    */
   public RegExp convert() throws IOException,ParseException {
   	
   	  return goal();   	  
   }
   
   /*
    *   Handles the goal production
    *        goal  ->  expression  Eof
    */   
   protected RegExp goal() throws IOException,ParseException   {
   	
   	  RegExp re = expression();
   	  match(Token.EOF);
   	  return re;
   }
   
   /*
    * Handles the expression production
    *       expression  ->  factor { | factor }
    */
   protected RegExp expression()  throws IOException,ParseException {
      	
        RegExp left = factor();
        while (nextToken() == Token.ALTERNATION) {
          match(Token.ALTERNATION);
          left = new RegOpExp(left,RegOpExp.ALTERNATION,factor());         
        }
        return left;
   }
   
    /*
    * Handles the factor production
    *      factor  ->  term { .? term }
    */   
   protected RegExp factor() throws IOException,ParseException {
      	    	    
        RegExp left = term();
        while (nextToken() == Token.CONCATENATION ||
               nextToken() == Token.LPAR ||
               nextToken() == Token.CHARACTER ||
               nextToken() == Token.CHARACTER_CLASS) {
          if (nextToken() == Token.CONCATENATION)
            match(Token.CONCATENATION);
          left = new RegOpExp(left,RegOpExp.CONCATENATION,term());         
        }
        return left;
   }

   /*
    * Handles the term production
    *      term     ->  primary { rightop }
    *      rightop  ->  *
    *               ->  +
    *				->  ?
    */
   protected RegExp term() throws IOException,ParseException {
      
         RegExp left = primary();
         while (nextToken() == Token.KLEENE_CLOSURE ||
                nextToken() == Token.POSITIVE_CLOSURE ||
                nextToken() == Token.OPTION)
           switch (nextToken()) {
             case Token.KLEENE_CLOSURE    : 
                     match(Token.KLEENE_CLOSURE);
                     left = new RegOpExp(left,RegOpExp.KLEENE_CLOSURE);
                     break;
                                           
             case Token.POSITIVE_CLOSURE  : 
                     match(Token.POSITIVE_CLOSURE);
                     left = new RegOpExp(left,RegOpExp.POSITIVE_CLOSURE);
                     break;
                      
             case Token.OPTION  :
                     match(Token.OPTION);
                     left = new RegOpExp(left,RegOpExp.OPTION);
                     break;                            
           }                                                                                           
         return left;    
    }

    /*   Handles the primary production
     *        primary  ->  ( expression )
     *                 ->  character
     *                 ->  character_class
     */
    protected RegExp primary() throws IOException,ParseException {         
          
         RegExp left = null; 
         switch(nextToken()) {
         
           case Token.LPAR :
         	 match(Token.LPAR);
         	 left = expression();
         	 match(Token.RPAR);
         	 break;
                  
           case Token.CHARACTER_CLASS :
             match(Token.CHARACTER_CLASS);
             char lower = curToken.getString().charAt(0);  
             char upper = curToken.getString().charAt(1);         	 
           	 if (lower > upper)
           	   error("Illegal character class limits [" + lower + "-" + upper + "]",
           			 CURRENT);
           	 if (lower == upper)
           	   left = new RegSymbolExp(lower);
           	 else
           	   left = new RegClassExp(lower,upper);
           	 break;
          
           case Token.CHARACTER  :
             match(Token.CHARACTER);
             left = new RegSymbolExp(curToken.getString().charAt(0)); 
             break;
             
           default :
             error("Character, Character class or ( expected",NEXT);
         }
         return left;  
    }

   	
   	public static void main(String[] a) throws IOException,ParseException {
   		
   		/*
   		 *   Illustrates use of convert routine.
   		 *   The example regexp ("x" | "y")* "x" "y" "y" is syntactically correct.
   		 *   To see what happens when there's a syntax error in the regexp, edit it to put in an error
   		 */
   		String re = "(\"x\" | \"y\")* \"x\" \"y\" \"y\"";
   		System.out.println("Converting regular expression " + re + " to RegExp expression tree");
        try {
   		  RegExp r = (new RegExp2AST(re)).convert();
   		  System.out.println("No syntax errors");
   		  System.out.println("Original fully parenthesised regular expression : " + 
   		                     r.decompile());  
        } catch (ParseException ex) {
        	System.out.println("Error at/near position " + ex.getErrorOffset() + " : " + 
                               ex.getMessage());
          }
   	}	                                        	                                             	                    	         	   	         	         	  	         	   		  	     		    	                                         	    	   	  		            
}