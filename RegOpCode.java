//class that store the value of each register used to calculate object code
public class RegOpCode {
	String reg;

	RegOpCode(String reg){
		this.reg=reg;
	}

	public RegOpCode() {

	}

	public int getNumber(String reg){
		if(reg.equals("%RA")){
			return 00;
		}
		else if(reg.equals("%RL")) {
			return 02;
		}
		else if(reg.equals("%RB")) {
			return 03;
		}
		else if(reg.equals("%RS")){
			return 04;
		}else if(reg.equals("%RT")){
			return 05;
		}else{
			return 01;
		}
	}
}
