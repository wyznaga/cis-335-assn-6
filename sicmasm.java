import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.File;

public class sicmasm extends OpTab {

    static int maxLine;
    static int progLength;
    static int[] ni, xbpe;
    static String[] location, label, mnemonic, operand, TA, PC, objCode,opCode, Format;

    public static void main(String args[]) throws IOException {
        //change to question formating
        if (args.length>2) {
            System.out.print("Please Input a file.");
        } else {
            SplitToCol("/Users/ryanreynolds/IdeaProjects/SIC XE Assembler/src/main.asm");
            pass1.pass1Calc();
            pass1.writePass1();
            intermediate.intermediateCalc();
            intermediate.writeIntermediate();
        }

    }



    protected static void SplitToCol(String filename) throws IOException {
        FileReader file = new FileReader(filename);
        BufferedReader reader = new BufferedReader(file);

        int count = 0;
        String readWord = "";
        
        //gets number of lines
        while ((reader.readLine()) != null) {
            count++;
        }
        reader.close();
        String source[][] = new String[count][3];

        maxLine = count;
        count = 0;

        FileReader file1 = new FileReader(filename);
        BufferedReader reader1 = new BufferedReader(file1);
        while ((readWord = reader1.readLine()) != null) {
            source[count++] = readWord.split(" ", 3);

        }
        label = new String[count];
        mnemonic = new String[count];
        operand = new String[count];
        location= new String[count];
        TA= new String[count];
        PC= new String[count];
        objCode= new String[count];
        opCode=new String[count];
        Format=new String[count];
        ni=new int[count];
        xbpe=new int[count];



        for(int i=0; i<count;i++){
            label[i]=source[i][0];
            mnemonic[i]=source[i][1];
            operand[i]=source[i][2];
        }
        reader1.close();
    }
}