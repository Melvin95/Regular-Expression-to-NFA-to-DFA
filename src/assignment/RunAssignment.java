package assignment;

import java.io.IOException;
import java.text.ParseException;
import java.util.Scanner;

/*Test class*/
public class RunAssignment {

	public static void main(String[] args) throws IOException {
		
		RegExp r = null;	//Regular Expression
		Nfa nfa = null;		//NFA of regular expression
		Dfa dfa = null;		//DFA constructed from NFA
		
		Scanner sc = new Scanner(System.in);
		
		//Regular expression must be entered in a certain form
		System.out.println("Regular expression entry: (x+y)*xyy should be entered as \n"
				+ "(\"x\"|\"y\")*\"x\"\"y\"\"y\" \n");
		
		//Get regular expression input from user
		System.out.println("===================================");
		System.out.println("1.Enter a regular expression: ");
		System.out.println("===================================");
		String regExp = sc.next();

		//Convert regular expression to expression tree, check for syntax errors first
        try 
        {
        	r = (new RegExp2AST(regExp)).convert(); 
   		  
   		  	System.out.println("\nNo syntax errors, regular expression converted to expression tree");
   		  	System.out.println("Fully decomposed regular expression : " + r.decompile()+"\n");  
        
        }catch (ParseException e) 
        {
        	System.out.println("Error at position " + e.getErrorOffset() + " : " + e.getMessage());
        }
        
        
        System.out.println("===================================");
        System.out.println("2.Regular Expression to NFA");
        System.out.println("===================================");
        //Convert RE to NFA
        nfa = r.makeNfa();
        System.out.println(nfa.toString());
        System.out.println("===================================\n\n");
        
        
        System.out.println("===================================");
        System.out.println("3.NFA to DFA");
        System.out.println("===================================");
        //Convert NFA to DFA
        dfa = new Dfa(nfa);
        System.out.println(dfa);
        System.out.println("===================================\n");
        
        //Test a string using the DFA above
        System.out.println("Enter a string to test, 0 to exit: \n");
        String testString = sc.next();
        if(!testString.equals("0")){
            do{
                if(dfa.acceptString(testString)){
                	System.out.println("STRING: '"+testString+"' IS ACCEPTED!!\n");
                }
                else{
                	System.out.println("STRING: '"+testString+"' IS REJECTED!!\n");
                }  
                System.out.println("Enter a string to test, 0 to exit: ");
                testString = sc.next();
            }
            while(!testString.equals("0"));
        }
        sc.close();
	}
}
