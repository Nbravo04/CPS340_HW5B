import java.util.HashMap;

/**
 * 
 * 
 * @author 
 *
 */
public class ShiftTable {

	// Use a hash map for the shift table, mapping characters to a shift
	private HashMap<Character, Integer> ShiftTable;
	private char[] alphabet;


	/**
	 * Constructs a Bad Match Table(Shift Table) given parameters.
	 * 
	 * @param patter, a char array that is used for the shift table.
	 * @param alphabet, a char array that is saved to the shift table object.
	 */
	public ShiftTable(char[] pattern, char[] alphabet) {
		this.alphabet = alphabet;
		int length = pattern.length;
		int index = 0;
		ShiftTable = new HashMap<Character, Integer>();
		
		for(char c: pattern) {
			if(index + 1 == length) {
				if(ShiftTable.containsKey(c)) {
					//Do nothing
				}
				else {
					ShiftTable.put(c,length);
				}
			}
			else if (ShiftTable.containsKey(c)) {
				ShiftTable.replace(c, ShiftTable.get(c) - 1);
			}
			else {
				ShiftTable.put(c,length - index - 1);
			}
			index++;
		}
		
		//Add at the end for all remaining char shifts.
		ShiftTable.put('*', length);
	}

	/**
	 * Get the shift for a particular character.
	 * 
	 * @param c A character in the shift table 
	 * @return The shift associated with the given character.
	 */
	public int getShiftByChar(char c) {
		//In pattern
		if(ShiftTable.containsKey(c)) {
			return ShiftTable.get(c);			
		}
		//Not in pattern
		else {
			return ShiftTable.get('*');
		}
	}


	public void dumpShiftTable() {
		System.out.println("-----");
		for(int j = 0; j < alphabet.length; j++) {
			System.out.println(alphabet[j] + ": " + ShiftTable.get(alphabet[j]));
		}
		System.out.println("-----\n");
	}
	
}
