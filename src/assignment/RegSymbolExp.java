package assignment;

/*
 *  Class representing a single character tree in a RegExp expression tree.
 */
public class RegSymbolExp extends RegExp {
	
	protected char symbol;    // character represented
	
	
	public RegSymbolExp(char symbol) {
		
		this.symbol = symbol;				
	}

     /*
	 * Make and return a Nfa for this character expression tree
	 * Done in NFA constructor
	 */	
	public Nfa makeNfa() {
		Nfa symbolNfa = new Nfa(symbol);
		return symbolNfa;
	}

	/*
	 *  Decompile this character expression tree back to its original
	 *  form as a string
	 */	
	public String decompile() {
		
		return "\"" + symbol + "\"";
	}
}