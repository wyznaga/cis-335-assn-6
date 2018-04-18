

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class Sicmasm {
	static String mnemonic;	// mnemonic string

	static int op; // variable to store op code
	static int first2; //Integer to store first two digits of Object Code

	private static File main,obj,lst; //files to read in and out too

	static int address=0000;static int nxtloc=0000; //starting address and PC address

	// hash table for storing label and corresponding address
	static Pass1Table symTab=new Pass1Table();
	static Pass1Table addressTable=new Pass1Table(); //
	
	static OpTable ObjectCodeTable=new OpTable();

	static ArrayList<String[]> mainAsString = new ArrayList<String[]>(); //array list for storing each string in the main.asm in a two D Array list

	static OpTable OpTab=new OpTable(); //Optable created to get the op code for each instruction

	static StringBuilder OBJcode = new StringBuilder(); //String builder declared to easily piece together each lines objectcode

	static RegOpCode register=new RegOpCode(); //used to get the opcode for the special cases where the program uses a register

	public static void main( String[]args) throws Exception {
		/*if (argc != 2) {
			System.out.print("Error: Invalid syntax entered");
			System.exit(1);
		} else { */
			ArrayList<String> list = new ArrayList<String>();
			main = new File("/Users/ryanreynolds/Desktop/Assignment 6/src/src/main.asm");
			obj = new File(main.getName().substring(0, main.getName().lastIndexOf('.')) + ".obj");
			lst = new File(main.getName().substring(0, main.getName().lastIndexOf('.')) + ".lst");

			BufferedReader mainTmp = new BufferedReader(new FileReader(new File("/Users/ryanreynolds/Desktop/Assignment 6/src/src/main.asm")));
			String mainInLine;
			int count = 1;
			while ((mainInLine = mainTmp.readLine()) != null) { //looping through each line in main.asm and add to the a String array until we reach the end of file

				list.add(mainInLine);
				//split each line by a space
				String[] str1 = mainInLine.trim().split("\\s+");
				mainAsString.add(str1);


				//PASS 1 is generated within this loop the location counter is stored within the addressTable object
				for (int i = 0; i < str1.length; i++) {
					if (str1[i].equals(";")) {
						if (str1[0].equals(";")) {
							str1 = null;
							break;
						} else {
							str1 = Arrays.copyOfRange(str1, 0, i);
							break;
						}
						//handles the MOV instruction by changing it from MOV %RA,ALPHA to LDA ALPHA
					} else if (str1[i].equals("MOV") || str1[i].equals("+MOV")) {
						if ((str1[i + 1].charAt(0)) == ('%')) {
							if (str1[i + 1].charAt(2) == 'L') {
								str1[i] = "STL";
								str1[i + 1] = str1[i + 1].substring(5);
							} else if (str1[i + 1].charAt(2) == 'A') {
								str1[i] = "STA";
								str1[i + 1] = str1[i + 1].substring(5);
							} else {
								str1[i] = "STX";
								str1[i + 1] = str1[i + 1].substring(5);
							}
						} else if (str1[i].equals("+MOV")) {
							str1[i] = "+STT";
							str1[i + 1] = str1[i + 1].substring(0, str1[i + 1].indexOf(','));
						} else {
							if ((str1[i + 1].substring(str1[i + 1].length() - 4)).charAt(2) == 'B') {
								str1[i] = "LDB";
								str1[i + 1] = str1[i + 1].substring(0, str1[i + 1].indexOf(','));

							} else if ((str1[i + 1].substring(str1[i + 1].length() - 4)).charAt(2) == 'A') {
								str1[i] = "LDA";
								str1[i + 1] = str1[i + 1].substring(0, str1[i + 1].indexOf(','));
							} else if ((str1[i + 1].substring(str1[i + 1].length() - 4)).charAt(2) == 'D') {
								str1[i] = "LDX";
								str1[i + 1] = str1[i + 1].substring(0, str1[i + 1].indexOf(','));
							} else {
								str1[i] = "LDT";
								str1[i + 1] = str1[i + 1].substring(0, str1[i + 1].indexOf(','));
							}
						}
					}
				}
				if (str1 != null) {
					if (str1.length == 3) {            //locate the Mnemonic in each line
						mnemonic = str1[1];
					} else if (str1.length == 2) {
						mnemonic = str1[0];
					} else {
						mnemonic = str1[0];
					}
					Pass1Calculation loc = new Pass1Calculation(mnemonic);
					if (count == 5) {    //Calculate address for each line
						address = 6;
						nxtloc = address + 4;
					} else {
						if (mnemonic.equals("BYTE") && str1[0].equals("EOF")) {
							address = nxtloc;
							nxtloc = address + 3;
						} else {
							address = nxtloc;
							nxtloc = address + loc.getInstructionSize(mnemonic);
						}
					}

					String loc1 = (Integer.toHexString(address)).toUpperCase();
					if (!str1[0].equals("END"))
						addressTable.instAddress(count, loc1);    //storing address for each line in the hash table
					count++;

					if (str1 != null) {
						if (str1.length == 3) {                        // all lines with length of 3 contain the label
							symTab.pass1(str1[0], loc1);        //storing Labels and their address address in the hash table
						}
					}
				}
			}
			mainTmp.close();

			// The Following code calculates Pass 2 for the
			int count1 = 2;
			int count2;
			for (int i = 2; i < mainAsString.size() - 1; i++) {
				//uses the file stored as strings to determine which arguement is the mneumonic and what string is the PC pointing to
				String Mnemonic;
				String destination;
				if (mainAsString.get(i).length == 3) {
					Mnemonic = mainAsString.get(i)[1];
					destination = mainAsString.get(i)[2];
				} else if (mainAsString.get(i).length == 2) {
					Mnemonic = mainAsString.get(i)[0];
					destination = mainAsString.get(i)[1];
				} else {
					Mnemonic = mainAsString.get(i)[0];
					destination = Mnemonic;
				}
				//Calculates the first 2 digits of the Object Code (Op code + ni flags)
				if (Mnemonic.equals("CLEAR") || Mnemonic.equals("COMPR") || Mnemonic.equals("TIXR")) {
					first2 = OpTab.OPcode(Mnemonic);
				} else if (destination.charAt(0) == '#') {
					first2 = OpTab.OPcode(Mnemonic) + 0x01;
				} else if (destination.charAt(0) == '@') {
					first2 = OpTab.OPcode(Mnemonic) + 0x02;
				} else {
					first2 = OpTab.OPcode(Mnemonic) + 0x03;
				}

				//Sets the flags for each case
				int flag;
				if (Mnemonic.charAt(0) == '+') {//extended mode
					flag = 0x01;
				} else if (destination.equals("LENGTH") && Long.parseLong(addressTable.getAddress1(count1), 16) >= Long.parseLong(symTab.getAddress("LENGTH"), 16)) {
					flag = 0x04;
				} else if (destination.equals("BUFFER[%EXX]")) {
					flag = 0x0C;
				} else if (destination.charAt(0) == '#' || Mnemonic.equals("RSUB")) {
					flag = 0x00;
				} else {
					flag = 0x02;
				}

				count2 = count1; //used as hashtable keys to keep track of object code values
				if (count1 < mainAsString.size() - 1) {
					count1++;
				} else {
					count1 = count2;
				}
				//Calculation of Displacement given the different types
				long displacement;
				if (destination.charAt(0) == ('#') || destination.charAt(0) == '@') {
					destination = destination.substring(1);
				}
				//get the address of Labels from Pass1 table to calculate displacement
				if (Character.isDigit((destination.charAt(0)))) {
					displacement = (Long.parseLong(destination));
				} else if (flag == 0x04) {    //extended mode
					displacement = 000;
				} else if (Mnemonic.charAt(0) == '+') {
					displacement = (Long.parseLong(symTab.getAddress(destination), 16));
				} else if (Mnemonic.equals("STCH") || Mnemonic.equals("LDCH")) {
					displacement = (Long.parseLong(symTab.getAddress("BUFFER"), 16) - Long.parseLong(symTab.getAddress("LENGTH"), 16));
				} else if (symTab.containsKey(destination)) {
					displacement = (Long.parseLong(symTab.getAddress(destination), 16) - Long.parseLong(addressTable.getAddress1(count1), 16));
				} else if (Mnemonic.equals("CLEAR") || Mnemonic.equals("TIXR")) {
					displacement = register.getNumber(destination);
				} else {
					displacement = 0;
				}
				//Places the op code, flag and displacement into Object code
				if (Mnemonic.equals("CLEAR") || Mnemonic.equals("TIXR")) {
					OBJcode.append((Integer.toHexString(first2))).append(Long.toHexString(displacement));// use String builder to easily manipulate strings
				} else if (Mnemonic.equals("BYTE") && destination.equals("C'EOF'")) {
					OBJcode.append("454F46");
				} else if (destination.equals("X'F3'")) {
					OBJcode.append("F3");
				} else if (destination.equals("X'05'")) {
					OBJcode.append("05");
				} else if (destination.charAt(0) == '%' && destination.length() > 4) {    //Handles Register to Register Operations
					String[] registers = destination.split(",");
					String register1 = registers[0];
					String register2 = registers[1];
					OBJcode.append((Integer.toHexString(first2))).append((Integer.toHexString(register.getNumber(register1)))).append((Integer.toHexString(register.getNumber(register2))));
				} else if (Mnemonic.charAt(0) == '+') {
					OBJcode.append((Integer.toHexString(first2))).append(Integer.toHexString(flag)).append('0').append(Long.toHexString(displacement));
				} else if (Long.toHexString(displacement).length() == 1) {
					OBJcode.append((Integer.toHexString(first2))).append(Integer.toHexString(flag)).append("00").append(Long.toHexString(displacement));
				} else if (Long.toHexString(displacement).charAt(0) == 'f') {
					OBJcode.append((Integer.toHexString(first2))).append(Integer.toHexString(flag)).append((Long.toHexString(displacement).substring((Long.toHexString(displacement)).length() - 3)));
				} else {
					OBJcode.append((Integer.toHexString(first2))).append(Integer.toHexString(flag)).append('0').append(Long.toHexString(displacement));
				}
				String objcode;
				if (Mnemonic.equals("RESW") || Mnemonic.equals("RESB")) {//case for directives
					objcode = null;
				} else {
					objcode = OBJcode.toString().toUpperCase();
				}
				OBJcode.delete(0, OBJcode.length());
				ObjectCodeTable.OBJcodeinput(count2, objcode);
			}

			//The following code writes Pass 2 in a listing file
			PrintWriter lstout = new PrintWriter(lst, "UTF-8");
			for (int i = 0; i < list.size(); i++) { //use loop to write line with address, instruction and object code in a lst file
				if (addressTable.getAddress1(i) == null && ObjectCodeTable.getObjectCode(i) == null) {
					lstout.write("\t" + list.get(i) + "\n");
				} else if (i == 4) {
					lstout.write("\t" + list.get(i) + "\n");
				} else if (ObjectCodeTable.getObjectCode(i) == null) {
					lstout.write(addressTable.getAddress1(i) + "\t" + list.get(i) + "\t\t\t\n");
				} else {
					lstout.write(addressTable.getAddress1(i) + "\t" + list.get(i) + "\t\t\t" + ObjectCodeTable.getObjectCode(i) + "\n");
				}
			}
			lstout.close();

			//THE FOLLOWING WRITES THE OBJECT CODE TO THE OBJ FILE
			/* *********************************************
			 * *********************************************
			 ********* STILL NEED TO DEBUG HERE***********
			 * *********************************************
			 * *********************************************
			 */
			PrintWriter objout = new PrintWriter(obj, "UTF-8");
			int lineCount = 1;
			int first, last;
			int k = 0;
			String obj;
			String[][] text = new String[list.size()][14];
			String[] end = new String[2];
			String[] head = new String[4];
			boolean check = true;

			//populate the head text
			head[0] = "H";
			head[1] = mainAsString.get(1)[0] + "  ";
			head[2] = addressTable.getAddress1(1);
			head[3] = addressTable.getAddress1(list.size() - 2);

			for (int i = 0, j = 0; i < list.size(); i++) {

				if (addressTable.getAddress1(i) != null && ObjectCodeTable.getObjectCode(i) != null) {


					if (i + 1 < list.size() && mainAsString.get(i + 1)[0].equals("END")) {
						end[0] = "E";
						end[1] = addressTable.getAddress1(2);

					} else {
						for (; j < 14; j++) {

							if (text[k][0] == (null)) {

								first = Integer.parseInt(addressTable.getAddress1(i), 16);
								if ((addressTable.getAddress1(i + 9) != null)) {
									last = Integer.parseInt(addressTable.getAddress1(i + 9), 16);
								} else {
									last = Integer.parseInt(addressTable.getAddress1(list.size() - 2), 16);
								}
								last = last - first;
								text[k][j] = "\nT";
								text[k][1] = addressTable.getAddress1(i);
								text[k][2] = Integer.toHexString(last);
								text[k][3] = ObjectCodeTable.getObjectCode(i);


							} else {
								text[k][j] = ObjectCodeTable.getObjectCode(i);
								i++;
							}
						}
						k++;
					}


				}
			}
			objout.close();
		//}
	}
}
