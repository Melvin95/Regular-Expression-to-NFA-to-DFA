package assignment;


/*
 *  Class representing a operator tree in a RegExp expression tree.
 */
 public class RegOpExp extends RegExp{
	
	// Codes for different operator types
	public static final int ALTERNATION      = 0,
	                        CONCATENATION    = 1,
	                        KLEENE_CLOSURE   = 2,
	                        POSITIVE_CLOSURE = 3,
	                        OPTION           = 4;
	protected RegExp left;    // Left operand
	protected int    op;      // Operator
	protected RegExp right;   // Right operand (null if Operator is unary)
	
	/*
	 * Construct a unary operator expression tree
	 */
	public RegOpExp(RegExp left,
	                int    op) {
		
		this(left,op,null);
	}
	
	/*
	 * Construct a binary operator expression tree
	 */
	public RegOpExp(RegExp left,
	                int    op,
	                RegExp right) {
	              	
	    this.left  = left;
	    this.op    = op;
	    this.right = right;      	
	}

	/*
	 * Make and return a Nfa for this operator expression tree
	 */	
	public Nfa makeNfa() {
		
		//Both left and right and in that order
		if(op==CONCATENATION){
			Nfa lft = this.left.makeNfa();		//Make left-hand NFA
			Nfa rgt = this.right.makeNfa();		//Make right-hand NFA
			
			//Combine the two for concatenation operator
			Nfa concatNfa = new Nfa();
			
			//Adds left to concatNfa
			concatNfa.setStart(lft.start);
			concatNfa.setAccept(lft.accept);
			
			//Now to add the right to concatNfa
			if(rgt.start.characterClass==false)		//If right state has a character transition then slightly different implementation
			{
				//Merges left accept and start of right
				concatNfa.accept.next1 = rgt.start.next1;
				concatNfa.accept.symbol = rgt.start.symbol;
				concatNfa.accept.next2 = rgt.start.next2;
				concatNfa.accept.symbol2 = rgt.start.symbol2;
			}
			else{
				//Merges left accept and start of right
				concatNfa.accept.next1 = rgt.start.next1;
				concatNfa.accept.symbol = rgt.start.symbol;
				concatNfa.accept.symbol2 = rgt.start.symbol2;
				concatNfa.accept.characterClass = true;
				concatNfa.accept.setCharacterClassData();
			}
			//Accept state is now rights accept state
			concatNfa.setAccept(rgt.accept);
			
			//Just connected, need to set the number of states
			concatNfa.setNumStates(lft.getNumStates()+rgt.getNumStates()-1);
			return concatNfa;
		}
		
		/*Zero or more*/
		if(op == KLEENE_CLOSURE){
			Nfa lft = this.left.makeNfa();
			//System.out.println(lft.start.symbol);
			Nfa kleenNfa = new Nfa();
			
			NfaState accept = new NfaState(null,null,'1',Nfa.GnumStates++);
			NfaState start = new NfaState(lft.start,accept,'0','0',Nfa.GnumStates++);
			
			kleenNfa.setStart(start);
			kleenNfa.numStates++;
			
			lft.accept.next1 = lft.start;
			lft.accept.symbol = '0';
			lft.accept.next2 = accept;
			lft.accept.symbol2 = '0';
			kleenNfa.numStates += lft.getNumStates();
			
			kleenNfa.setAccept(accept);
			kleenNfa.numStates++;
			
			return kleenNfa;
		}
		
		//At least one or more of left-hand expression
		if(op == POSITIVE_CLOSURE){
			Nfa lft = this.left.makeNfa();
			Nfa pClosureNfa = new Nfa();
			
			//Add a new START and ACCEPT state... 
			NfaState accept = new NfaState(null,null,'1',Nfa.GnumStates++);
			NfaState start = new NfaState(lft.start,null,'0',Nfa.GnumStates++);
			//...with epsilon transitions to left-hand expression
			lft.accept.next1 = lft.getStart();
			lft.accept.symbol = '0';
			lft.accept.next2 = accept;
			lft.accept.symbol2 = '0';
			
			pClosureNfa.setStart(start);;
			pClosureNfa.setAccept(accept);
			pClosureNfa.setNumStates(lft.getNumStates()+2); //Adding accept state and start state
			return pClosureNfa;
		}
		
		//Left or Right but not both
		if(op == ALTERNATION){
			Nfa lft = this.left.makeNfa();
			Nfa rgt = this.right.makeNfa();
			Nfa altNfa = new Nfa();
			//Add new START and ACCEPT states...
			NfaState accept = new NfaState(null,null,'1',Nfa.GnumStates++);
			NfaState start = new NfaState(lft.start,rgt.start,'0','0',Nfa.GnumStates++);
			//With epsilon transitions to START of left and right expressions
			altNfa.setStart(start);
			lft.accept.next1 = accept;
			lft.accept.symbol = '0';
			rgt.accept.next1 = accept;
			rgt.accept.symbol = '0';
			altNfa.setAccept(accept);
			//2 new states added(START and ACCEPT)
			altNfa.setNumStates(lft.getNumStates()+rgt.getNumStates()+2);
			return altNfa;
			
		}
		//Optional: Zero or One
		if(op == OPTION){
			Nfa lft = this.left.makeNfa();
			Nfa optionalNfa = new Nfa();
			
			NfaState accept = new NfaState(null,null,'1',Nfa.GnumStates++);
			NfaState start = new NfaState(lft.start,accept,'0','0',Nfa.GnumStates++);
			
			optionalNfa.setStart(start);
			lft.accept.next1 = accept;
			lft.accept.symbol = '0';
			optionalNfa.setAccept(accept);
			optionalNfa.setNumStates(lft.getNumStates()+2);
			return optionalNfa;
		}
		
		
		return null;
	}
	
	/*
	 *  Decompile this operator expression tree back to its original
	 *  form as a string
	 */	
	public String decompile() {
		
		String leftString  = "(" + left.decompile();
		String rightString = ")";
		if (right != null)
		  rightString = right.decompile() + rightString;
		
		switch (op)  {
			case ALTERNATION      : return leftString + "|" + rightString;
			                        
		    case CONCATENATION    : return leftString + "." + rightString;
		    
		    case OPTION			  : return leftString + rightString + "?";
		    
		    case KLEENE_CLOSURE   : return leftString + rightString + "*";
		    
		    case POSITIVE_CLOSURE : return leftString + rightString + "+";
		    
		    default               : throw new RuntimeException
		                                ("Illegal operator in RegOpExp");
		}
		  
	}

}