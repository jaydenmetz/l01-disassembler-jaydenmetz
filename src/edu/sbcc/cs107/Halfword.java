package edu.sbcc.cs107;

/**
 * @author Jayden Metz CS 107: Disassembler Project
 *
 *         This class is used to model a half-word of an object file. Each
 *         half-word must have an address as well as a data value that can be
 *         disassembled into mnemonics and optional operands.
 * 
 *         Note that the half-word is 16 bits but we are using a Java int which
 *         is typically 32 bits. Be sure to take that into account when working
 *         with it.
 *
 */
public class Halfword {
	private int address = 0;
	private int data = 0;
	private String hexData = "";
	private Boolean inHex = false;

	/**
	 * Constructor for a halfword.
	 * 
	 * @param address
	 * @param data
	 */
	public Halfword(int address, int data) {
		this.address = address;
		this.data = data;
	}

	// If Hex data is too big for an int
	public Halfword(int address, String data) {
		this.address = address;
		this.hexData = data;
		inHex = true;
	}

	/**
	 * toString method.
	 * 
	 * The format for the halfword is a hex value 8 characters wide (address), a
	 * single space, and a hex value four characters wide (data).
	 * 
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (!inHex) {
			return makeAHexNum(address, 8) + " " + makeAHexNum(data, 4);
		} else {
			return makeAHexNum(address, 8) + " " + hexData;
		}
	}

	/**
	 * Get the address of the half-word.
	 * 
	 * @return
	 */
	public int getAddress() {
		return address;
	}

	/**
	 * .
	 * 
	 * @return
	 */
	public int getData() {
		if (inHex) {
			return Integer.valueOf(hexData, 16);
		}
		return data;

	}
	
	//To find if Halfword has an int or String for data
	public Boolean isInHex() {
		return inHex;
	}

	public String makeAHexNum(int num, int numDigits) {

		String result = "";
		int value = 0;

		while (num != 0) {
			if (num > 15) {
				value = num % 16;
			} else {
				value = num;
			}

			if (value < 10) {
				result = value + result.substring(0);
			} else {
				switch (value) {
				case 10:
					result = "A" + result.substring(0);
					break;
				case 11:
					result = "B" + result.substring(0);
					break;
				case 12:
					result = "C" + result.substring(0);
					break;
				case 13:
					result = "D" + result.substring(0);
					break;
				case 14:
					result = "E" + result.substring(0);
					break;
				case 15:
					result = "F" + result.substring(0);
					break;

				}

			}
			num = num / 16;
		}
		
		//
		while (result.length() < numDigits) {
			result = "0" + result;
		}

		return result;

	}

}
