package assignment;


/**
 *  Class Token defines the Token object returned by the 
 *  scanner class Lexer for regular expressions.
 */
public class Token implements Cloneable {
 
   // Token types
   public static final int EOF              = 0,
                           CHARACTER_CLASS  = 1,
                           CHARACTER        = 2,
                           ALTERNATION      = 3,
                           CONCATENATION    = 4,
                           KLEENE_CLOSURE   = 5,
                           EOL              = 6,  
                           POSITIVE_CLOSURE = 7,
                           OPTION           = 8,
                           ERROR            = 9,
                           LPAR             = 10,
                           RPAR             = 11;
   
   // Names of token types
   private static final String[] tokenNames = {
      "Eof","[]","Character","|",".","*","Eol","+",
      "?","Error","(",")"
   };	
 
   // Instance variables
   private int		       tokenType;     // Token type
   private String          tokenString;   // Characters comprising token
   private int             tokenLine;     // Line number in source file where
                                          //   token occurs
   private int             tokenCol;      // Column number in source file where
                                          //   token occurs
   
   
 
 
   /** 
    * Constructs a new Token object. 
    */
   public Token(int    tokenType,
                String tokenString,
                int    tokenLine,
                int    tokenCol)
   {
   	  
   	     this.tokenType   = tokenType;
         this.tokenString = tokenString;
         this.tokenLine   = tokenLine;   
         this.tokenCol    = tokenCol;  
   }
   
   /**
    *  Returns a copy of this Token object
    */
   public Object clone() 
   {
     return new Token(tokenType,tokenString,tokenLine,tokenCol);
   }
    
   /**
    * "Getter" methods for each of the instance variables
    */ 
   public int getType()
   {
      return tokenType;
   }
    
   public String getString()
   {
     return tokenString;
   }  
   
   public int getLineNo()
   {
     return tokenLine + 1;
   }
   
   public int getColNo()
   {
     return tokenCol + 1;
   }    

   public static String getTypeName(int tokenType)
   {
     return tokenNames[tokenType];
   }
   
   /**
    * Returns a string representation of a token
    */     
   public String toString()
   {      
       return tokenType + " " + "\t" + getTypeName(tokenType) + "\t\t" +
              getString() +  "\t\t" + 
              getLineNo() + "\t" + getColNo();
   }           
} 
