package assignment;

import java.util.*;

/*
 * Class representing a NFA
 */
public class Nfa {
	
	protected NfaState start;             // NFA start state
	protected NfaState accept;            // NFA final (accept) state
	protected int numStates = 0;		 //number of states
	
	protected static int GnumStates = 0;  //Static for unique state number
	
	//All states in the NFA
	private HashSet<NfaState> c     = new HashSet<NfaState>();
	
	//Characters that NFA transitions on excluding epsilon
	protected static TreeSet<Character> alphabet = new TreeSet<Character>();
	
	//Characters that NFA transitions on but in an array for indexing in the DFA 
	private static ArrayList<Character> alphaArray = new ArrayList<Character>();

	/*
	 * Construct a new empty Nfa
	 */
	public Nfa() {	
	
	   this(null,null,0);	
	}
	
	/*
	 * Construct a new NFA with specified start and final states and number of states
	 */
	public Nfa(NfaState start,
	           NfaState accept,
	           int      numStates) {
	             	
	     this.start  = start;
	     this.accept = accept; 
	     this.numStates = numStates;        	
	}
	
	/* For makeNfa() in RegSymbolExp()
	 * Construct a NFA for a single symbol using Thompson's construction
	 * A single symbol is represented by 2 states:
	 * Start state and an Accept State
	 * Single transition from Start-->Accept on the symbol only
	 */
	public Nfa(char symbol){
		this.accept =  new NfaState(null,null,'1',Nfa.GnumStates++);
		this.start =  new NfaState(this.accept,null,symbol,GnumStates++);
		Nfa.alphabet.add(symbol);
		numStates += 2;

	}

	public NfaState getStart() {
		
		return start;
	}
	
	/*
	 * Getters and setters for instance variables
	 */
	public NfaState getAccept() {
		
		return accept;
	}
	
	public void setStart(NfaState start) {
		
		this.start = start;
		//start.stateNo = numStates;
	}
	
	public void setAccept(NfaState accept) {
		
		this.accept = accept;
		//numStates++;
	}
	
	public int getNumStates() {
		
		return numStates;
	}
	
	public void setNumStates(int num) {
		
		numStates = num;
	}
	
	public HashSet<NfaState> getAllStates(){
		return c;
	}
	
	public TreeSet<Character> getAlphabet(){
		return alphabet;
	}
	
	public ArrayList<Character> getArrayAlpha(){
		for(Character c:alphabet){
			alphaArray.add(c);
		}
		return alphaArray;
	}
	/*
	 * Return a string representation of this Nfa in a form of a list of the 
	 * nodes comprising the Nfa and the transitions from each node.
	 */
	public String toString() {
		
		Stack<NfaState>   stack = new Stack<NfaState>();
		
		String result = "Number of states : " + numStates + "\n";
		result += "Start state : " + start.getStateNo() + "\n";
		result += "Final state : " + accept.getStateNo() + "\n";
		result += "States :\n";
		stack.push(start);
		c.add(start);
		while (!stack.empty()) {
		   NfaState t = stack.pop();
		   result += t + "\n";

		   if (t.next1 != null && ! c.contains(t.next1)) {
		     c.add(t.next1);
		     stack.push(t.next1);
		   }
		   if (t.next2 != null && ! c.contains(t.next2)) {
		     stack.push(t.next2); 
		     c.add(t.next2);
		   } 
		}		
		return result;
	}	 	             	     	     
}