package assignment;

/*
 *  Class representing a character class tree in a RegExp expression tree.
 */
public class RegClassExp extends RegExp {
	
	protected char lower,  // lower limit of class
	               upper;  // upper limit of class
	
	/*
	 * Construct a new character node with specified lower
	 * and upper limits for the class
	 */
	public RegClassExp(char lower,
	                   char upper) {
		
		this.lower = lower;
		this.upper = upper;				
	}
	
	/*
	 * Make and return a Nfa for this character class expression tree
	 */
	public Nfa makeNfa() {
        Nfa nfa = new Nfa();
        NfaState accept = new NfaState(null,null,'1',Nfa.GnumStates++);
        NfaState start = new NfaState(accept,null,lower,upper,Nfa.GnumStates++,true);
        nfa.setStart(start);
        nfa.setAccept(accept);
        nfa.setNumStates(2);
        return nfa;
	}
	
	/*
	 *  Decompile this character class expression tree back to its original
	 *  form as a string
	 */
	public String decompile() {
		
		return "[" + lower + "-" + upper + "]";
	}
}