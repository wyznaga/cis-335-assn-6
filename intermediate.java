import java.io.*;
import java.util.Iterator;

//takes pass 1 and calcs each lines op code, address, flags, format number
public class intermediate extends pass1{

//Assumes only PC no base. Ignoring indirect our program won't use it.
    protected static void intermediateCalc(){

        //get op code of instruction
        for(int i =0; i<maxLine; i++){

            //get PC
            if(!(maxLine==i+1)){
                PC[i]=location[i+1];
            }
            else
                PC[i]=Integer.toHexString(progLength);

            //label declarations
            if(mnemonic[i].equals("RESB")||mnemonic[i].equals("RESW")||mnemonic[i].equals("WORD")||mnemonic[i].equals("BYTE")){
                TA[i]=sym.get(mnemonic);
                opCode[i]=" ";
                ni[i]=0;
                xbpe[i]=0;
                continue;

            }
            //REGISTER TO REGISTER
            if(mnemonic[i].contains("ADDR")||mnemonic[i].contains("COMPR")||mnemonic[i].contains("SUBR")||mnemonic[i].contains("TIXR")){
                Format[i]="2";
                opCode[i]=op.get(mnemonic[i]);
                TA[i]=op.get(operand[i]);
                ni[i]=0;
                xbpe[i]=0;
                continue;
            }



            //get target address and set flags Formats 3 and 4

            // TO DO: ADD MOV handler for loading and Storing within these functions
            Iterator<String> keySetIterator = sym.keySet().iterator();
            while(keySetIterator.hasNext()) {
                String key = keySetIterator.next();
                //Needs to handles Simple:+op m, op m, +op m,X, and op m,X & Immediate: +op #m and op #m
                if (operand[i].contains(key)) {
                    //+op m and +op m,X
                    if (mnemonic[i].contains("+")) {
                        //Immediate: +op #m
                        if (operand[i].contains("#")) {
                            Format[i] = "addr";
                            ni[i] = 0b01;
                            xbpe[i] = 0b0001;
                            if (!mnemonic[i].contains("MOV")) {
                                opCode[i] = Integer.toHexString(Integer.valueOf(op.get(mnemonic[i]), 16) + ni[i]);
                                break;
                            } else {

                                opCode[i] = op.get(movCalc(operand[i]));
                                break;
                            }
                        }
                        //+op m,X
                        else if (operand[i].contains("[%RX]")) {
                            Format[i] = "addr+(X)";
                            ni[i] = 0b11;
                            xbpe[i] = 0b1001;
                            if (!mnemonic[i].contains("MOV")) {
                                opCode[i] = Integer.toHexString(Integer.valueOf(op.get(mnemonic[i]), 16) + ni[i]);
                                break;
                            } else {

                                opCode[i] = op.get(movCalc(operand[i]));
                                break;
                            }
                        }
                        //+op m
                        else {
                            Format[i] = "addr";
                            ni[i] = 0b11;
                            xbpe[i] = 0b0001;
                            if (!mnemonic[i].contains("MOV")) {
                                opCode[i] = Integer.toHexString(Integer.valueOf(op.get(mnemonic[i]), 16) + ni[i]);
                                break;
                            } else {

                                opCode[i] = op.get(movCalc(operand[i]));
                                break;
                            }
                        }
                    } else {
                        //op #m
                        if (operand[i].contains("#")) {
                            Format[i] = "(PC)+disp";
                            ni[i] = 0b10;
                            xbpe[i] = 0b0010;
                            if (!mnemonic[i].contains("MOV")) {
                                opCode[i] = Integer.toHexString(Integer.valueOf(op.get(mnemonic[i]), 16) + ni[i]);
                                break;
                            } else {

                                opCode[i] = op.get(movCalc(operand[i]));
                                break;
                            }
                        }
                        //op m,X
                        else if (operand[i].contains("[%RX]")) {
                            Format[i] = "(PC)+disp+(X)";
                            ni[i] = 0b11;
                            xbpe[i] = 0b1010;
                            if (!mnemonic[i].contains("MOV")) {
                                opCode[i] = Integer.toHexString(Integer.valueOf(op.get(mnemonic[i]), 16) + ni[i]);
                                break;
                            } else {

                                opCode[i] = op.get(movCalc(operand[i]));
                                break;
                            }
                        }
                        //op m
                        else {
                            Format[i] = "(PC)+disp";
                            ni[i] = 0b11;
                            xbpe[i] = 0b0010;
                            if (!mnemonic[i].contains("MOV")) {
                                if (!mnemonic[i].equals("END")) {
                                    opCode[i] = Integer.toHexString(Integer.valueOf(op.get(mnemonic[i]), 16) + ni[i]);
                                } else
                                    opCode[i] = "";
                                break;
                            } else {

                                opCode[i] = op.get(movCalc(operand[i]));
                                break;
                            }
                        }

                    }


                }
            }
                //immediate op #c
            if(operand[i].contains("#")){



                    Format[i]="disp";
                    ni[i]=0b01;
                    xbpe[i]=0b0000;
                    String [] value= operand[i].split("#");
                    TA[i]=value[value.length-1];
                    if(!mnemonic[i].contains("MOV")) {
                        opCode[i] = Integer.toHexString(Integer.valueOf(op.get(mnemonic[i]), 16) + ni[i]);
                    }
                    else{

                        opCode[i]=op.get(movCalc(operand[i]));

                    }
                }



        }

    }
        //calculates the op code for mov statements
        protected static String movCalc(String operand){
            String [] opSub=operand.split(",");
            if(opSub[0].contains("%R"))
                return ","+opSub[0];
            else if(opSub[1].contains("%R"))
                return opSub[1]+",";

            return "RYAN SOMETHING'S WRONG";
        }
        protected static void writeIntermediate() throws IOException{
        Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("intermediate.txt"), "utf-8"));
        for(int i=0,l=1;i<maxLine;i++,l++){
            writer.write(l+"\t"+location[i]+"\tlabel:"+label[i]+"\tmnemonic:"+mnemonic[i]+"\tOP:"+opCode[i]+"\tnixbpe"+ni[i]+" "+xbpe[i]+"\tOperand:"+operand[i]+"\n");
        }
        writer.close();
    }
}

