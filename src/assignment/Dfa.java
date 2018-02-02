package assignment;

import java.util.*;

/*
 * Represents a DFA
 * Converts a NFA into a DFA using the classic subset construction
 */
public class Dfa {
	
	
    public static final int SIGMA_LOWER = 32;
    public static final int SIGMA_UPPER = 127;
    public static final int MAX_STATES  = 256;
    public static final int ACCEPT      = -1;
    public static final int START       = 1;
    public static final int TRAP        = 0;
    
    protected int[][]                        transTable; //Transition table, representation of DFA
    protected ArrayList<HashSet<NfaState>>   states;     //DFA states, essentially a bunch of NFA states	
    protected int                            size;
    

    protected ArrayList<Character> alphabetArray;	//Alphabet of the NFA and thus the DFA
    
    protected ArrayList<Integer> acceptStatesPos;	//Index of those states that are accept states in DFA
 
    
    /*Converts NFA to DFA using subset construction*/
    public Dfa(Nfa nfa){
    	
    	alphabetArray = nfa.getArrayAlpha();		//Gets alphabet of NFA, same alphabet used for DFA
    	
    	NfaState acceptStateNFA = nfa.getAccept();  //Gets accept state of NFA, only one since NFA constructed using Thompson's construction
    	
    	//Initialization of various lists
    	acceptStatesPos = new ArrayList<Integer>();			      //Index of those states that are accept states in the DFA
    	states = new ArrayList<HashSet<NfaState>>();		     //Stores states of DFA
    	transTable = new int[MAX_STATES][alphabetArray.size()];	//DFA transition table, actual representation of DFA
    	HashSet<NfaState> helper = new HashSet<NfaState>();    //Just a helper set for subset construction and stuff
    	
    	//Start of subset construction
    	helper.add(nfa.getStart());
    	
    	//eClosure on a set of states, in this case just the set containing start state
    	HashSet<NfaState> eClosure = GetEClosure(helper);	//START STATE in DFA
    	states.add(eClosure);
    	if(checkIfAcceptState(eClosure,acceptStateNFA))    //Checks if start state of DFA is an accepting state
    		acceptStatesPos.add(START);
    	
    	//Queue of DFA states to explore
    	Queue<HashSet<NfaState>> queue = new LinkedList<HashSet<NfaState>>();
    	queue.offer(eClosure);		//Currently only one DFA state(the start state)
    	
    	
    	int stateNum = START;     //Current state we're looking to transition from
    	
    	//Subset construction from START state
    	while(!queue.isEmpty()){	  //Loop 'till no DFA states left to explore
    		
    		eClosure = queue.poll();  //Get a DFA state to explore
    		
    		//Data structure for results of Move(eClosure,c)
    		Queue<HashSet<NfaState>> moveQueue = new LinkedList<HashSet<NfaState>>();
    		
    		//Perform the Move(eClosure,c) algorithm on all characters of the NFA's alphabet
        	for(Character c:alphabetArray){
        		helper = Move(eClosure,c);	//Returns a set of NFA states
        		moveQueue.offer(helper);   
        	}
        	
        	//eClosure algorithm on each element in moveQueue will give DFA states to transition into from state {stateNum}
        	int alphaNum = 0;			//For populating columns transTable
        	while(!moveQueue.isEmpty()){
        		helper = GetEClosure(moveQueue.poll());	//STATE IN DFA
        		
        		//Populating transTable here...
        		if(helper.isEmpty()){			//TRAP STATE
        			transTable[stateNum][alphaNum] = TRAP;	//Go to trap state
        		}
        		
        		else if((!states.contains(helper))){  //NEW STATE DFA
        			states.add(helper);			//add to states list
        			queue.offer(helper);		//need to explore moves from this new state
        			
        			transTable[stateNum][alphaNum] = states.size(); //Go to new state(just added)
        			
        			//Check if new state is an accept state
        	    	if(checkIfAcceptState(helper,acceptStateNFA))
        	    		acceptStatesPos.add(states.size());
        		}
        		else{							//ELSE EXISTING STATE, just set transTable
        			transTable[stateNum][alphaNum] = states.indexOf(helper)+1;	
        		}
        		alphaNum++;  //Next transition(next column)
        	}
        	stateNum++;     //Next state(next row)
    	}
    }
    
    /*Performs the E-Closure on a set of NFA states*/
    private HashSet<NfaState> GetEClosure(HashSet<NfaState> set){
    	HashSet<NfaState> eClosure = new HashSet<NfaState>();
    	Stack<NfaState> stack = new Stack<NfaState>();
    	for(NfaState element:set){
        	stack.push(element);
        	eClosure.add(element);
        	while(!stack.isEmpty()){
        		NfaState s = stack.pop();
        		if(s!=null){
            		if(s.symbol=='0'){
            			stack.push(s.next1);
            			eClosure.add(s.next1);
            		}
            		if(s.symbol2=='0'){
            			stack.push(s.next2);
            			eClosure.add(s.next2);
            		}
        		}
        	}
    	}
    	return eClosure;
    }
    
    /*Performs the Move(T,x) algorithm. T is a set of states, x is a character*/
    private HashSet<NfaState> Move(HashSet<NfaState> eC, char character){
    	HashSet<NfaState> moveSet = new HashSet<NfaState>();
    	for(NfaState state:eC){
    		if(state!=null){
    			if(state.characterClass==true){
    				if(state.symbol<=character && character<=state.symbol2){
    					moveSet.add(state.next1);
    				}
    			}
    			else{
            		if(state.symbol==character)
            			moveSet.add(state.next1);
            		if(state.symbol2==character)
            			moveSet.add(state.next2);
    			}
    		}
    	}
    	return moveSet;
    }
    

    /*Check if a DFA state(just a bunch of NFA states) is an accept state*/
    private boolean checkIfAcceptState(HashSet<NfaState> DFAstate,NfaState accept){
    	//It's an accept state if the NFA accept state is in the DFAstate set
    	for(NfaState state:DFAstate){
    		if(state==accept)
    			return true;
    	}
    	return false;
    }
    
    public Dfa() {
    }
     
    public Dfa(int[][]                      transTable,
               ArrayList<HashSet<NfaState>> states,
               int                          size) {
               	
       this.transTable = transTable;
       this.states     = states;
       this.size       = size;        	
   }
    
   int[][] getTransTable() {
	   
	   return transTable;
   }
   
   int getSize()  {
	   
	   return size;
   }
   
   ArrayList<HashSet<NfaState>> getStates() {
      return states;
   }
   
   /*Prints out transTable*/
   public String toString(){
	   String s = "States |  \t";
	   for(char alpha:alphabetArray){
		   s+= alpha+"  ";
	   } 
	   s+="\n-------|-----------------------------------------------------------------------"+"\n";
	   for(int i=0;i<=states.size();i++){
		   if(i<10)
			   s+=i+"      |\t";
		   else
			   s+=i+"     |\t";
		   
		   for(int j=0; j<alphabetArray.size();j++){
			   s+=transTable[i][j]+"  ";
		   }
		   if(acceptStatesPos.contains(i))
			   s+= "\t ACCEPT STATE";
		   if(i==1)
			   s+="\t START";
		   else if(i==0)
			   s+="\t TRAP";
		   s+="\n";
	   }
	   return s;
   }
   
   /*Check if a particular string is accepted by the DFA*/
   public boolean acceptString(String s) {

	   //Use the transTable, going through each character at a time
	   int state = START;
	   for(char c:s.toCharArray()) {
		   try{
			   state = transTable[state][alphabetArray.indexOf(c)];  //Get next state to go to after reading a character 
		   }
		   catch(Exception e){
			   return false;  //Alphabet in string not in alphabet array thus can't be accepted by DFA
		   }
		   //No coming out of a TRAP state, return false
		   if(state==TRAP)
			   return false;
		   
	   }
	   //At this point, string has finished reading in and we're not in a TRAP state
	   if(acceptStatesPos.contains(state))		//Check if we're in an ACCEPT state		
		   return true;
	   else
		   return false;						//Else we're in a non-ACCEPT state(not the TRAP state)
   }
}