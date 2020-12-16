import java.util.*;
public class Main {
	static String str; //String str is received from user to be parsed
	static int curIndex; //pointer that iterates over each char in String str
	static char inputToken; //holds the current character of str
	static boolean firstId; //
	static boolean firstLit;
	static String varName = ""; /*holds the variable name from str,eg: if str="z=1+2;x=1;y=2;"+"$"
	var will hold z || x || y */
	static boolean isVariable = false; 
	static String varVal = "";
	static String temp = "";//holds value of the input as a string before coverting it into an integer
	static Map<String, Integer> map = new HashMap<>();	    

	public static void main(String[] args){
		// Please type input without white spaces
		str = "z=1+2;x=1;y=2;"+"$";
		curIndex = 0;
		nextToken();
		do {
		firstId = true; isVariable = true;
		identifier();
		int value = exp();
		semicolon();
		saveData(varName, value);// save each variable to HashMap 
		}while(inputToken != '$');
		printOutput();
	    }
	    static void error(){
	        throw new RuntimeException("syntax error");
	    }
	    static void match(char expectedToken){
	        if (inputToken != expectedToken)
	            error();
	        elses
	            nextToken();
	    }
	    static void nextToken(){
	        if (curIndex >= str.length())
	            error();
	        inputToken = str.charAt(curIndex++);
	    }
	    static int exp(){
	         if (Character.isDigit(inputToken) || inputToken == '(' || inputToken == '-'
	        		|| inputToken == '+' ||inputToken == '_' || Character.isLetter(inputToken)){
	        	 
		             int op1 = term();
		             if(inputToken == '+') {
		             int op2 = op1 + expPrime();
		             return op2;
		             }else if(inputToken == '-'){
		             int op2 = op1 - expPrime();	
		             return op2;
		             }else
		             return op1;
		        } else {
		            error();
		        }
	       return 0;
	    }
	    static int expPrime(){
	    
	        if (inputToken == '+'){	
	            match('+');                
	            return term() + expPrime();
	         
	        } else if (inputToken == '-'){
	            match('-');
	            return term() - expPrime();         
	            
	        } else if (inputToken == ')' || inputToken == '$' || inputToken == ';'){
	        	return 0;
	        } else {
	            error();
	        }
	  		return 0;
	    }
	    static int term(){
	        if (Character.isDigit(inputToken) || inputToken == '(' || inputToken == '-'
	        		|| inputToken == '+' ||inputToken == '_' || Character.isLetter(inputToken)){
	        	 int op = factor();
	           return op * termPrime();
	             
	        } else {
	            error();
	        }
	       return 0;
	    }
	    static int termPrime(){
	        switch (inputToken){
	        case '*':
	            match('*');
	            int op = factor();
	            return op *= termPrime();
	        case '+':
	        case '-':
	        case ')':
	        case ';':
	        case '$':
	            return 1;
	        default:
	            error();
	        }
	        return 0;
	    }
	    static int factor(){
	    	//Fact:
	    	//	( Exp ) | - Fact | + Fact | Literal | Identifier	
	        if  (inputToken == '('){ // (Exp) 
	        	String sign = temp; temp = "";// keep a negative sign before the parenthesis
	            match('(');
	            int op = exp();
	            match(')');
	            if (sign.length() %2 == 1)
	            	return op *-1;
	            else return op;           
	        } else if(inputToken == '-') {    //- Fact
	        	temp += inputToken;
	        	 match('-');
	        	 return factor();
	        	 
	        }else if(inputToken == '+') { // + Fact 
	        	temp += inputToken;
	        	 match('+');
	        	 return factor();
	        }else if(Character.isDigit(inputToken)){// Literal
	        	if(inputToken == '0') {
	        	match(inputToken);	
	        	}else {
	        	firstLit = true;
	        	// set val back to blank    	
	        	literal(); 
	        	int litVal = Integer.parseInt(temp);
	        	temp = "";
	        	return litVal;
	        	}
	        }else if(Character.isLetter(inputToken) || inputToken == '_'){//Identifier    	
	        	firstId = true;
	        	varVal = "";
	        	identifier();
	        	// find value of a variable
	        	if(map.containsKey(varVal)) {
	        		// if it's negative number check in Temp will need to make it negative berfore return
	        		if (temp.length() %2 == 1)
	        		return map.get(varVal) * -1;else return map.get(varVal);
	        	}else{
	        		throw new RuntimeException("variable " + varVal +" not found!!");
	        	}
	        }
	      return 0;
	    }
	    static void literal(){
	    	// Literal:
	    	//0 | NonZeroDigit Digit* we check if it's 0 before going inside this function
	    	if(Character.isDigit(inputToken)) temp += inputToken;
	    	
	    	if(firstLit == true){
	    		// first Literal character needs to be non-zero digit 
	    		if(Character.isDigit(inputToken) && inputToken != '0'){
	    			firstLit = false;
	    			match(inputToken);
	    			literal();
	        	}
	    	}else{
	    		if(Character.isDigit(inputToken)) {
	    			match(inputToken);
	    			literal();
	    		}
	    	}
	    }
	    static void identifier() {		
	    	if(inputToken != '=' && isVariable == true)   
				varName += inputToken;
			if (firstId == true) {
	    		// first character of identifier can only be a letter 
	    		//Identifier:
	         	//		Letter [Letter | Digit]*
	    		
	    		if(Character.isLetter(inputToken) || inputToken == '_') {
 	    			firstId = false;
 	    			varVal += inputToken;
					System.out.println("varVal is "+varVal);
	    			match(inputToken);
	    			identifier();
	    		}else if (inputToken == '$'){
	    			//we have reached the end of string str
	    		}else {
	    			throw new RuntimeException("Error: first id can only be a letter or _ ");
	    		}
	    	}else {
	    		// can only be a letter or digit a|...|z|A|...|Z|_|0|...|9
	    		if(Character.isLetter(inputToken) || inputToken == '_'
	    			|| Character.isDigit(inputToken)) {
	    			varVal += inputToken;
					System.out.println("~~~varVal is "+varVal);
	    			match(inputToken);
	    			identifier();			
	    		}else if(inputToken == '=') { 
	    			isVariable = false;
	    			match(inputToken);		
	    		}else if(inputToken == '$'|| inputToken == '+' || inputToken == '*' || inputToken == '-'
	    				|| inputToken == '(' ||inputToken == ')' || inputToken == ';'	) {
	    		}else {
	    			throw new RuntimeException("Error: Id can be only letter, '_', or digit");
	    		}	
	    	}	
	    }
	    static void semicolon() {
	    	 if(inputToken == ';') {
	    	 match(inputToken);
	    	 }else {
	    		 throw new RuntimeException("Error: Missing ; (Semicolon)");
	    	 }
	    }
	    static void saveData(String v, int value) {
	    	if(map.containsKey(v)) {
	    		map.put(v,map.get(v) + value);
	    	}else{
	    		map.put(v, value);
	    	}
	    	// set var to blank 
	    	varName = "";	 
	     } 
	    static void printOutput() {
	    	// print data from hashmap 
	    	for(Map.Entry<String, Integer> set: map.entrySet()) {
	    		System.out.println(set.getKey() +" = "+set.getValue());
	    	}	
	    }
}