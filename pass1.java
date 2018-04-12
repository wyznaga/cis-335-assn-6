import java.io.*;
import java.nio.file.Path;
import java.util.*;


public class pass1 extends sicmasm{
    protected static HashMap<String,String> sym = new HashMap<String, String>(){{

    }


    };


    protected static void pass1Calc(){

        boolean start=false;

       int loc=0;
        for(int i = 0; i<maxLine; i++){

            if(mnemonic[i].equals("START")){
                loc=Integer.valueOf(operand[i],16);
                location[i]=Integer.toHexString(loc);
                location[i+1]=Integer.toHexString(loc);

                start=true;
                continue;
            }
            if(start==false)
                location[i]="";
            else if(start==true){
                if(i+1==maxLine){
                    progLength=Integer.valueOf(location[i],16)-Integer.valueOf(location[0],16);
                    location[i]="-";
                    break;
                }
                if(mnemonic.equals("")) {
                    location[i + 1] = location[i];
                    continue;
                }
                //Create Sym table within here
                //sets the location
                if(label[i].equals("")==true && sym.get(label[i])==null && label[i].equals("BYTE")==false && label[i].equals("RESW")==false && label[i].equals("RESB")==false && label[i].equals("WORD")==false){
                    loc+=3;
                    location[i+1]=Integer.toHexString(loc);
                }
                //sets the location of the variable labels and stores them into the sym table
                else if(label[i]!="" && sym.get(label[i])==null){

                    if (mnemonic[i].equals("BYTE")) {
                        //generate calc function
                    }
                    else if (mnemonic[i].equals("RESW")) {
                        loc+=3*(Integer.parseInt(operand[i]));
                        location[i+1]=Integer.toHexString(loc);
                    }
                    else if (mnemonic[i].equals("RESB")) {
                        loc += (Integer.parseInt(operand[i]));
                        location[i + 1] = Integer.toHexString(loc);
                    }
                    else if(mnemonic[i].equals("WORD")){
                        loc+=3;
                        location[i+1]=Integer.toHexString(loc);
                    }
                    else {
                        loc += 3;
                        location[i + 1] = Integer.toHexString(loc);
                    }
                    sym.put(label[i], location[i]);

                }
                //set the location of instructions without a label
                else{
                    loc+=3;
                    location[i+1]=Integer.toHexString(loc);
                }
            }
        }

   }

    protected static void writePass1()throws IOException{
       Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("pass1.txt"), "utf-8"));
        for(int i=0,l=1;i<maxLine;i++,l++){
            writer.write(l+"\t"+location[i]+"\t"+label[i]+"\t"+mnemonic[i]+"\t"+operand[i]+"\n");
        }
        writer.close();
    }
}
