import java.util.*;
public class OpTab {
    protected static HashMap<String,String> op = new HashMap<String, String>(){
        {
            //Format 2: Reg to Reg
            put("ADDR","90");
            put("COMPR","A0");
            put("SUBR","94");
            put("TIXR","B8");

            //format 3 Mnemonics
            put("ADD","18");
            put("COMP","28");
            put("SUB","1C");
            put("MUL","20");
            put("DIV","24");
            put("J","3C");
            put("JEQ","30");
            put("JGT","34");
            put("JLT","38");
            put("JSUB","48");
            put("LDCH","50");
            put("RSUB","4C");
            put("TIX","2C");
            put("RD","D8");
            put("TD","E0");
            put("WD","DC");
            put("STCH","54");
            put("CLEAR","B4");

            //Format 4 Mnemonics
            put("+ADD","18");
            put("+COMP","28");
            put("+SUB","1C");
            put("+MUL","20");
            put("+DIV","24");
            put("+J","3C");
            put("+JEQ","30");
            put("+JGT","34");
            put("+JLT","38");
            put("+JSUB","48");
            put("+LDCH","50");
            put("+RSUB","4C");
            put("+TIX","2C");
            put("+RD","D8");
            put("+TD","E0");
            put("+WD","DC");
            put("+STCH","54");

            //Loading Regs
            put("%RA,","00");
            put("%RB,","68");
            put("%RL,","08");
            put("%RS,","6C");
            put("%RT,","74");
            put("%RX,","04");

            //Storing From Regs
            put("%RA","0C");
            put("%RB","78");
            put("%RL","14");
            put("%RS","7C");
            put("%RT","84");
            put("%RX","10");

            //Add an index handler

            //format 2 register to register values
            put("%RA,%RB","03");
            put("%RA,%RL","02");
            put("%RA,%RS","04");
            put("%RA,%RT","05");
            put("%RA,%RX","01");
            put("%RB,%RA","30");
            put("%RB,%RL","32");
            put("%RB,%RS","34");
            put("%RB,%RT","35");
            put("%RB,%RX","31");
            put("%RL,%RA","20");
            put("%RL,%RB","23");
            put("%RL,%RS","24");
            put("%RL,%RT","25");
            put("%RL,%RX","21");
            put("%RS,%RA","40");
            put("%RS,%RB","43");
            put("%RS,%RL","42");
            put("%RS,%RT","45");
            put("%RS,%RX","41");
            put("%RX,%RA","10");
            put("%RX,%RB","13");
            put("%RX,%RL","12");
            put("%RX,%RS","14");
            put("%RX,%RT","15");
            put("%RT,%RA","50");
            put("%RT,%RB","53");
            put("%RT,%RL","52");
            put("%RT,%RS","54");
            put("%RT,%RX","51");



        }
    };
    protected String getOp(String n){
        return op.get(n);
    }

}
