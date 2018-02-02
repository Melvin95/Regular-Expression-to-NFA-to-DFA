package assignment;

/*
 * Class to represent a regular expression in the form of an expression
 * tree.  
 *
 * There are various types of nodes in the tree. E.g.
 *    OpExp nodes to represent expressions involving operators like | * etc
 *	  Char  nodes to represent individual characters
 *    Class nodes to represent character classes
 *
 *  Each node is a subtype of this class amd must implement the abstract 
 *  methods defined here
 */
public abstract class RegExp {
	
	protected static int nextStateNum = 0;  // Current unique state number
	
	/*
	 * Generate and return a new state number by incrementing the previous one
	 * by 1.
	 */
	public static int getNextStateNum() {
		
		return nextStateNum++;
	}
	
    /*
     * Set the next state number to a specific value
     */
	public static void setNextStateNum(int nextNum) {
		
		nextStateNum = nextNum;
	}
	
	/*
	 * Make amd return a new Nfa for this RegExp
	 */
	public abstract Nfa makeNfa();
	
	/*
	 * Decompile this RegExp as a treeback to its original 
	 * form as a string
	 */
	public abstract String decompile();
	
	
}