package edu.sbcc.cs107;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Jayden Metz CS 107: Disassembler Project
 *
 *         This code implements working with a Hex file. The hex file format is
 *         documented at http://www.keil.com/support/docs/1584/
 */
public class HexFile {
	/**
	 * This is where you load the hex file. By making it an ArrayList you can easily
	 * traverse it in order.
	 */
	private ArrayList<String> hexFile = null;
	private ArrayList<Halfword> halfwordList = null;

	/* Add other variables here. */

	private int index = 0;
	private int dataBytes = 0;
	private int address = 0;
	private int recordType = 0;
	private int fileType = 10;

	Halfword last = null;

	/**
	 * Constructor that loads the .hex file.
	 * 
	 * @param hexFileName
	 * @throws FileNotFoundException
	 */
	public HexFile(String hexFileName) throws FileNotFoundException {

		hexFile = new ArrayList<String>();
		halfwordList = new ArrayList<Halfword>();

		Scanner sc = new Scanner(new File(hexFileName));

		String lineOfCode = "";

		// Loading the Hexfile arraylist and getting the halfwords one line at a time
		while (sc.hasNextLine()) {
			lineOfCode = sc.nextLine();

			fileType = getRecordType(lineOfCode);

			int addr = getAddressOfRecord(lineOfCode);

			if (fileType == 0) {
				getHalfwords(addr, lineOfCode);
			}
		}
		sc.close();

	}

	/**
	 * Pulls the length of the data bytes from an individual record.
	 * 
	 * This extracts the length of the data byte field from an individual hex
	 * record. This is referred to as LL->Record Length in the documentation.
	 * 
	 * @param Hex file record (one line).
	 * @return record length.
	 */
	public int getDataBytesOfRecord(String record) {
		dataBytes = Integer.valueOf(record.substring(1, 3), 16);

		return dataBytes;
	}

	/**
	 * Get the starting address of the data bytes.
	 * 
	 * Extracts the starting address for the data. This tells you where the data
	 * bytes start and are referred to as AAAA->Address in the documentation.
	 * 
	 * @param Hex file record (one line).
	 * @return Starting address of where the data bytes go.
	 */
	public int getAddressOfRecord(String record) {

		address = Integer.valueOf(record.substring(3, 7), 16);

		return address;
	}

	/**
	 * Gets the record type.
	 * 
	 * The record type tells you what the record can do and determines what happens
	 * to the data in the data field. This is referred to as DD->Data in the
	 * documentation.
	 * 
	 * @param Hex file record (one line).
	 * @return Record type.
	 */
	public int getRecordType(String record) {
		recordType = Integer.valueOf(record.substring(7, 9), 16);

		return recordType;
	}

	// Getting halfwords out of one line of the file
	public void getHalfwords(int addr, String record) {

		String first, last = "";
		for (int i = 9; i + 4 < record.length(); i += 4) {

			// switching order because it's little-endian
			last = record.substring(i, i + 2);
			first = record.substring(i + 2, i + 4);

			Halfword h = new Halfword(addr, String.format("%2s%2s", first, last));
			halfwordList.add(h);

			addr += 2;
		}

	}

	/**
	 * Returns the next halfword data byte.
	 * 
	 * This function will extract the next halfword from the Hex file. By repeatedly
	 * calling this function it will look like we are getting a series of halfwords.
	 * Behind the scenes we must parse the HEX file so that we are extracting the
	 * data from the data files as well as indicating the correct address. This
	 * requires us to handle the various record types. Some record types can effect
	 * the address only. These need to be processed and skipped. Only data from
	 * recordType 0 will result in something returned. When finished processing null
	 * is returned.
	 * 
	 * @return Next halfword.
	 */
	public Halfword getNextHalfword() {
		Halfword toReturn = null;

		toReturn = halfwordList.get(index);
		index++;

		return toReturn;
	}

}
