//Implements the Pass 1 Algorithm described in the book to calculate the location counter of each line of the program
public class Pass1Calculation {
	String mnemonic;

	Pass1Calculation(String mnemonic){
		this.mnemonic=mnemonic;
	}

	public int getInstructionSize(String mnemonic) {
		if(mnemonic!=null){
			if (mnemonic.charAt(0)=='+'){
				return 4;
			}else if((mnemonic.substring(mnemonic.length()-1).charAt(0))=='R'){
				return 2;
			}else if(mnemonic.equals("BASE")||mnemonic.equals("START")){
				return 0;
			}else if(mnemonic.equals("RESB")){
				return 4096;
			}else if(mnemonic.equals("BYTE")){
				return 1;
			}else{
				return 3;
			}
		}else{
			return 0;
		}
	}
}
