package edu.sbcc.cs107;

/**
 * @author Jayden Metz CS 107: Disassembler Project
 * 
 *         This code implements the disassembler as well as pulling apart the
 *         Hex file. The hex file format is documented at
 *         http://www.keil.com/support/docs/1584/
 */
public class Disassembler {

	/**
	 * Extracts the register operand from a halfword.
	 * 
	 * The register operand (e.g. r0) is used by many mnemonics and is embedded in
	 * the data halfword. It position is specified by the least significant bit and
	 * most significant bit. This value is extracted and concatenated with "r" to
	 * give us the desired register.
	 * 
	 * @param hw            Halfword that contains the machine code data.
	 * @param lsBitPosition Encoded register value (LSB)
	 * @param msBitPosition Encoded register value (MSB)
	 * @return Register field designation (e.g. r1)
	 */
	public String getRegister(Halfword hw, int lsBitPosition, int msBitPosition) {

		String binary = Integer.toBinaryString(hw.getData());

		// fill with leading zeros
		while (binary.length() < 16) {
			binary = "0" + binary;
		}
		String s = binary.substring(binary.length() - msBitPosition - 1, binary.length() - lsBitPosition);

		return "r" + Integer.valueOf(s, 2);
	}

	/**
	 * Extracts the immediate operand from a halfword.
	 * 
	 * Same as the getRegister function but returns the embedded immediate value
	 * (e.g. #4).
	 * 
	 * @param hw            Halfword that contains the machine code data.
	 * @param lsBitPosition Encoded immediate value (LSB)
	 * @param msBitPosition Encoded immediate value (MSB)
	 * @return Immediate field designation (e.g. #12)
	 */

	public String getImmediate(Halfword hw, int lsBitPosition, int msBitPosition) {
		String binary = Integer.toBinaryString(hw.getData());

		// fill with leading zeros
		while (binary.length() < 16) {
			binary = "0" + binary;
		}
		String s = binary.substring(binary.length() - msBitPosition - 1, binary.length() - lsBitPosition);

		return "#" + Integer.valueOf(s, 2);
	}

	/**
	 * Returns a formatted string consisting of the Mnemonic and Operands for the
	 * given halfword.
	 * 
	 * The halfword is decoded into its corresponding mnemonic and any optional
	 * operands. The return value is a formatted string with an 8 character wide
	 * field for the mnemonic (left justified) a single space and then any operands.
	 * 
	 * @param hw Halfword that contains the machine code data.
	 * @return Formatted string containing the mnemonic and any operands.
	 */
	public String dissassembleToString(Halfword hw) {
		String operation = "";
		String firstCommand = "";
		String secondCommand = "";

		String[] nameArr = { "ADCS", "ADDS", "CMN", "LDRSB", "MOVS", "MOVS", "REV", "B" };
		String[] valArr = { "0100000101", "0001110", "0100001011", "0101011", "00100", "0000000000", "1011101000",
				"1110011111111110" };

		String binary = Integer.toBinaryString(hw.getData());

		// fill with leading zeros
		while (binary.length() < 16) {
			binary = "0" + binary;
		}

		for (int i = 0; i < nameArr.length; i++) {

			if (binary.substring(0, valArr[i].length()).equals(valArr[i])) {

				// if ADCS, CMN, MOVS(0000000000), or REV
				if (i == 0 || i == 2 || i == 5 || i == 6) {
					operation = nameArr[i];
					firstCommand = getRegister(hw, 0, 2) + ", ";
					secondCommand = getRegister(hw, 3, 5);

				// if ADDS
				} else if (i == 1) {
					operation = nameArr[i];
					firstCommand = getRegister(hw, 0, 2) + ", ";
					secondCommand = getRegister(hw, 3, 5) + ", " + getImmediate(hw, 6, 8);

				// if LDRSB
				} else if (i == 3) {
					operation = nameArr[i];
					firstCommand = getRegister(hw, 0, 2) + ", ";
					secondCommand = "[" + getRegister(hw, 3, 5) + ", " + getRegister(hw, 6, 8) + "]";

				// if MOVS(00100)
				} else if (i == 4) {
					operation = nameArr[i];
					firstCommand = getRegister(hw, 8, 10) + ", ";
					secondCommand = getImmediate(hw, 0, 7);

				// if End Of File
				} else if (i == 7) {
					operation = nameArr[i];
					firstCommand = ".";
					
					return String.format("%-5s    %-1s", operation, firstCommand);
				}

				break;
			}
		}

		return String.format("%-5s    %-4s", operation, firstCommand + secondCommand);
	}

}
