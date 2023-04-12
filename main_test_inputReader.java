import java.io.*;
import java.util.ArrayList;

public class main_test_inputReader
{
    public static void main(String args[])
    {
        ArrayList<node> lg;
        ArrayList<node> gg;

        input_reader rd = new input_reader();

        try
        {
            gg = rd.returnFromFile("input.txt");
            lg = rd.returnLogicalGFromFile("input.txt");
            System.out.println("=== test ===");
            System.out.println("= grpahe physique =");
            for(int i = 0; i<gg.size();i++)
            {
                gg.get(i).printNode();
            }
            System.out.println("= grpahe logique =");
            for(int i = 0; i<lg.size();i++)
            {
                lg.get(i).printNode();
            }
            System.out.println("Fin de test :)");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        
    }
}
