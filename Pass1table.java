import java.util.HashMap;

//Named pass one table really used to calculate the information needed for to generate the intermediate file

class Pass1Table {
	private String label1;
	private  String location1;
	private int count;

	//Hash Maps to store the label name and address. Address Table is for the line number and address
	private  HashMap<String,String> symTab=new HashMap<>();
	private  HashMap<Integer,String> addressTable=new HashMap<>();
	Pass1Table(){
	}

	public String getLabel1() {
		return label1;
	}

	public void setLabel1(String label) {
		this.label1 = label;
	}

	public String getLocation1() {
		return location1;
	}

	public void setLocation1(String location) {
		this.location1 = location;
	}

	public String getAddress(String label1){
		return symTab.get(label1);
	}
	public String getAddress1(int count1){
		return addressTable.get(count1);
	}

	//adds a symbol and the corresponding location to the symbol table
	public void pass1(String label, String location){
		symTab.put(label,location);
	}

	//adds the line number and the address to the hash table
	public void instAddress(int count, String location) {
		addressTable.put(count,location);
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	//checks if the the symbol is in the sym table
	public boolean containsKey(String destination) {
		if( symTab.containsKey(destination)){
			return true;
		}else{
			return false;
		}
	}



}
