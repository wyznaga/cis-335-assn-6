import java.util.HashMap;
//this class is used to aid calculating Object code and store the values into a table
public class OpTable {
	int count;
	String Mnemonic;
	
	private  HashMap<Integer,String> ObjectCodeTable=new HashMap<>();

	OpTable(){

	}

	OpTable(String Mnemonic){
		this.Mnemonic=Mnemonic;

	}

	//Op code used for each mnemonic we are supposed to handle
	public int OPcode(String Mnemonic){
		if(Mnemonic.equals("STL")||Mnemonic.equals("+STL")){
			return 0x14;
		}else if(Mnemonic.equals("LDB")||Mnemonic.equals("+LDB")){
			return 0x68;
		}else if(Mnemonic.equals("+JSUB")||Mnemonic.equals("JSUB")){
			return 0x48;
		}else if(Mnemonic.equals("LDA")||Mnemonic.equals("+LDA")){
			return 0x00;
		}else if(Mnemonic.equals("COMP")||Mnemonic.equals("+COMP")){
			return 0x28;
		}else if(Mnemonic.equals("JEQ")||Mnemonic.equals("+JEQ")){
			return 0x30;
		}else if(Mnemonic.equals("J")||Mnemonic.equals("+J")){
			return 0x3C;
		}else if(Mnemonic.equals("STA")||Mnemonic.equals("+STA")){
			return 0x0C;
		}else if(Mnemonic.equals("CLEAR")){
			return 0xB4;
		}else if(Mnemonic.equals("+STX")||Mnemonic.equals("STX")){
			return 0x10;
		}else if(Mnemonic.equals("TD")){
			return 0xE0;
		}else if(Mnemonic.equals("RD")){
			return 0xD8;
		}else if(Mnemonic.equals("COMPR")){
			return 0xA0;
		}else if(Mnemonic.equals("STCH")){
			return 0x54;
		}else if(Mnemonic.equals("TIXR")){
			return 0xB8;
		}else if(Mnemonic.equals("JLT")||Mnemonic.equals("+JLT")){
			return 0x38;
		}else if(Mnemonic.equals("LDX")||Mnemonic.equals("+LDX")){
			return 0x04;
		}else if(Mnemonic.equals("RSUB")){
			return 0x4C;
		}else if(Mnemonic.equals("LDCH")){
			return 0x50;
		}else if(Mnemonic.equals("WD")){
			return 0xDC;
		}else if(Mnemonic.equals("+STT")||Mnemonic.equals("STT")){
			return 0x84;
		}else if(Mnemonic.equals("LDT")||Mnemonic.equals("+LDT")){
			return 0x74;
		}else if(Mnemonic.equals("LDL")||Mnemonic.equals("+LDL")){
			return 0x08;
		}else if(Mnemonic.equals("LDS")||Mnemonic.equals("+LDS")){
			return 0x6C;
		}else if(Mnemonic.equals("STB")||Mnemonic.equals("+STB")){
			return 0x78;
		}else if(Mnemonic.equals("STS")||Mnemonic.equals("+STS")){
			return 0x7C;
		}
		else{
			return 0x00;
		}
	}
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	public String getObjectCode(int count){//method to access the object code
		return ObjectCodeTable.get(count);
	}
	public void OBJcodeinput(int count, String objcode) {//method to store object code into a hash table
		ObjectCodeTable.put(count,objcode);
	}

}
