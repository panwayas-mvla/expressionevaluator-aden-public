// TODO: Auto-generated Javadoc
public class ExpressionEvaluator {
	// These are the required error strings for that MUST be returned on the appropriate error 
	// for the JUnit tests to pass. DO NOT CHANGE!!!!!
	private static final String PAREN_ERROR = "Paren Error: ";
	private static final String OP_ERROR = "Op Error: ";
	private static final String DATA_ERROR = "Data Error: ";
	private static final String DIV0_ERROR = "Div0 Error: ";

	private static final int LPAREN_PREC = 0;
	private static final int RPAREN_PREC = 0;
	private static final int MULT_PREC = 2;
	private static final int DIV_PREC = 2;
	private static final int PLUS_PREC = 1;
	private static final int MINUS_PREC = 1;


	// The placeholder for the two stacks
	private GenericStack<Double> dataStack;
	private GenericStack<String>  operStack;

	/**
	 * Convert to tokens. Takes a string and splits it into tokens that
	 * are either operators or data. This is where you should convert 
	 * implicit multiplication to explicit multiplication. It is also a candidate
	 * for recognizing negative numbers, and then including that negative sign
	 * as part of the appropriate data token.
	 *
	 * @param str the str
	 * @return the string[] of tokens
	 * @throws Exception 
	 */
	private String[] convertToTokens(String expression) throws Exception {	
		expression = expression.replaceAll("\\/\\-\\(([0-9-+*/]*)\\)", "\\/\\(\\-\\($1\\)\\)");
		expression = expression.replaceAll("\\-\\(", "\\-\\1\\*\\(");
		expression = expression.replaceAll("^\\s*\\-([\\d\\.])","NEG$1");
		expression = expression.replaceAll("([\\+\\-\\*\\/\\(])\\s*\\-([\\d\\.])", "$1 NEG$2");
		String expressionWithSpaces = padTokensWithSpaces(expression);


		expressionWithSpaces = expressionWithSpaces.replaceAll("\\)\\s+([\\d\\.\\(])",") * $1");
		expressionWithSpaces = expressionWithSpaces.replaceAll("([\\d\\.])\\s+\\(", "$1 * (");
		expressionWithSpaces = expressionWithSpaces.replaceAll("NEG", "-");
		//should be final thing
		if(expressionWithSpaces.matches("\\s*\\(\\s*\\)\\s*"))
			throw new Exception(PAREN_ERROR);

		String[] tokens = expressionWithSpaces.split("\\s");		
		checkParenCountBalanced(tokens);
		checkTokens(tokens);
		checkFirstAndLastToken(tokens);
		return tokens;
	}
	
	
	/**
	 * Goes and counts the amount of left parens vs right parens, if 0 nothing happens
	 * @param tokens
	 * @throws Exception
	 */
	private void checkParenCountBalanced(String[] tokens) throws Exception {
		int paren = 0;
		for(String token: tokens) {
			if(token.equals("("))
				paren++;
			if(token.equals(")"))
				paren--;
		}
		if(paren != 0) 
			throw new Exception(PAREN_ERROR);
	}

	/**
	 * Generates errors involving multiple operations
	 * @param tokens
	 * @throws Exception
	 */
	private void checkTokens(String[] tokens) throws Exception {
		for(int i = 0; i < tokens.length -1; i++) {
			if(isData(tokens[i]) && isData(tokens[i + 1]))
				throw new Exception(DATA_ERROR);
			if(isTokenOp(tokens[i]) && isTokenOp(tokens[i+1]))
				throw new Exception(OP_ERROR);
			if(tokens[i].equals("(") && isTokenOp(tokens[i + 1]))
				throw new Exception(OP_ERROR);
			if(isTokenOp(tokens[i]) && tokens[i + 1].equals(")"))
				throw new Exception(OP_ERROR);
		}
		for(int i = 0; i < tokens.length - 2; i ++) {
			if(isTokenOp(tokens[i]) && isTokenOp(tokens[i + 1]) && isTokenOp(tokens[i + 2]))
				throw new Exception(OP_ERROR);
		}
	}
	
	/**
	 * Basic test to check if there are two operators in a row
	 * Checks if the final token is an operator
	 * @param tokens
	 * @throws Exception
	 */
	private void checkFirstAndLastToken(String[] tokens) throws Exception{
		if(isTokenOp(tokens[0]) && !(tokens[0].equals("-")))
			throw new Exception(OP_ERROR);
		if(isTokenOp(tokens[tokens.length - 1]))
			throw new Exception(OP_ERROR);
	}
	

	/**
	 * Pad tokens with spaces. Takes a string with a numeric expression,
	 * and pads all arithmetic tokens ('-', '+', '*', '/','(', and ')' ) 
	 * with spaces on either side. After replacement, any leading spaces at 
	 * the start of the string should be removed (ie, the first character of the 
	 * string cannot be a space). You MUST use regex grouping to implement the padding.
	 *
	 * @param in the numeric expression as an input string
	 * @return the formatted string with spaces added to either side of the numeric
	 *         operators.
	 */
	private String padTokensWithSpaces(String in) {
		String words =  in.replaceAll("([-+\\\\*/\\(\\)])", " $1 ");
		//return words.replaceAll("^\\s+", "");
		return  words.replaceAll("\\s+", " ").replaceAll("^\\s+", "");

	}

	/**
	 * Calls the static variables to keep organized
	 * @param op
	 * @return corresponding value to the operator
	 */
	private int opPrec(String op) {
		if(op.equals("(")) return LPAREN_PREC;
		if(op.equals(")")) return RPAREN_PREC;
		if(op.equals("+")) return PLUS_PREC;
		if(op.equals("-")) return MINUS_PREC;
		if(op.equals("*")) return MULT_PREC;
		if(op.equals("/")) return DIV_PREC;
		return -1;
	}

	/**
	 * Returns true if operator on stack is of higher precedence than the operator
	 * top of the stack
	 * @param opOnStack
	 * @param topOp
	 * @return the bigger of the two
	 */
	private boolean isHigherPrecedence(String currToken, String topOp) {
		return opPrec(currToken) >= opPrec(topOp);
	}


	/**
	 * 
	 * @param op
	 * @param num1
	 * @param num2
	 * @return the numbers given the operation
	 */
	private double evaluateNums(String op, double num2, double num1) throws Exception{ 

		if(op.equals("+")) return num1+num2;
		if(op.equals("-")) return num1-num2;
		if(op.equals("*")) return num1*num2;

		if(op.equals("/")) {
			if(num2 == 0.0) 
				throw new Exception(DIV0_ERROR);
			else 
				return num1/num2;
		} 

		return 0;
	}


	/**
	 * 
	 * @param token
	 * @return whether the token is a data
	 * @throws Exception 
	 */
	private boolean isData(String token) throws Exception {
		if(token.equals("+") || token.equals("-") || 
				token.equals("*") || token.equals("/") || token.equals("(") || (token.equals(")"))) 
			return false;
		if(!token.matches("\\-?\\d*\\.?\\d*")) 
			throw new Exception(DATA_ERROR);
		return true;
	}

	/**
	 * 
	 * @param token
	 * @return whether the token is an operator not including paren
	 */
	private boolean isTokenOp(String token) {
		if(token.equals("+") || token.equals("-") || 
				token.equals("*") || token.equals("/")) 
			return true;
		return false;
	}

	
	/**
	 * Evaluate expression. This is it, the big Kahuna....
	 * It is going to be called by the GUI (or the JUnit tester),
	 * and:
	 * a) convert the string to tokens. This should detect and handle
	 *    implicit multiplication:
	 *    -- examples: ")   (" or "9(" or ")3" -- 
	 *    and negation:
	 *    -- examples: "7 + -9" or "-.9" or "-3(" but not ")-3"
	 * b) if conversion successful, perform static error checking
	 *    - Paren Errors
	 *    - Op Errors 
	 *    - Data Errors
	 * c) if static error checking is successful:
	 *    - evaluate the expression, catching any runtime errors.
	 *      For the purpose of this project, the only runtime errors are 
	 *      divide-by-0 errors.
	 *
	 * @param str the str
	 * @return the string
	 * @throws Exception 
	 */
	protected String evaluateExpression(String expression) {
		dataStack =  new GenericStack<Double>();
		operStack =  new GenericStack<String>();
		try {
			String[] tokens = convertToTokens(expression);
			for(String token: tokens) {
				if(isData(token)) dataStack.push(Double.parseDouble(token));
				else if (token.equals("(")) 
					operStack.push(token);
				else if(token.equals(")")){
					while(!operStack.empty() && !operStack.peek().equals("(")) 
						dataStack.push(evaluateNums(operStack.pop(), dataStack.pop(), dataStack.pop()));
					operStack.pop();
				} else if(isTokenOp(token)) {
					while(!operStack.empty() && isHigherPrecedence(operStack.peek(), token)) 
						dataStack.push(evaluateNums(operStack.pop(), dataStack.pop(), dataStack.pop()));
					operStack.push(token);
				}
			}
			while(!(operStack.empty() || dataStack.empty())) 
				dataStack.push(evaluateNums(operStack.pop(), dataStack.pop(), dataStack.pop()));
			return ("=" + dataStack.pop());
		} 
		catch (NumberFormatException ne) {
			return DATA_ERROR;
		}
		catch(Exception e) {
			return e.getMessage();
		}
	}

}
