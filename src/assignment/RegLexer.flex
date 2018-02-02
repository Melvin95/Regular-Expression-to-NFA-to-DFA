/*
    JFLex specification file for Assignment 1 2010
    Regular expressions   
*/     

%%

//%debug
%class RegLexer
%line
%column
%type Token


%{
   StringBuffer string = new StringBuffer();
%}   

LineTerminator     = \r | \n | \r\n
InputChar          = [ -~]
WhiteSpace         = [ \t\f] 
Char               = \" {InputChar} \"
CharClass          = "[" {InputChar} "-" {InputChar} "]"

%%


"|"          
   { return new Token(Token.ALTERNATION,     yytext(),yyline,yycolumn);}
"."          
   { return new Token(Token.CONCATENATION,   yytext(),yyline,yycolumn);}
"*"          
   { return new Token(Token.KLEENE_CLOSURE,  yytext(),yyline,yycolumn);}
"+"          
   { return new Token(Token.POSITIVE_CLOSURE,yytext(),yyline,yycolumn);}
"?"          
   { return new Token(Token.OPTION,          yytext(),yyline,yycolumn);}
"("          
   { return new Token(Token.LPAR,            yytext(),yyline,yycolumn);}
")"          
   { return new Token(Token.RPAR,            yytext(),yyline,yycolumn);}
      
   
{CharClass}     
   { String s = (yytext().charAt(1)+"") + (yytext().charAt(3)+"");
     return new Token(Token.CHARACTER_CLASS, s,       yyline,yycolumn);}  
                           
{Char}      
   { String s = yytext().charAt(1) + "";
     return new Token(Token.CHARACTER,       s,       yyline,yycolumn);}
   

{LineTerminator}
   { return new Token(Token.EOL,     yytext(),yyline,yycolumn); }

                                       
{WhiteSpace} 
   { /* ignore */ }


.            { return new Token(Token.ERROR, yytext(),yyline,yycolumn);}
  
<<EOF>>      { return new Token(Token.EOF,   " "     ,yyline,yycolumn);}

