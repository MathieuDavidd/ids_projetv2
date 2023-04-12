import java.io.*;
import java.util.ArrayList;

/*
    reading from a file and checking if the matrix graph is correct or not
*/

// ne pas oublier le try 
public class input_reader
{
    public ArrayList<node> returnFromFile(String fileName) throws IOException
    {
        // on part du principe que l'input est bonv
        BufferedReader rd = new BufferedReader(new FileReader(fileName));
        String input;
        ArrayList<node> graph = new ArrayList<node>();
        int len = 0;
        int curr_len;
        int iterator = 0;

        int t;
        input = rd.readLine();
        int leng = Integer.parseInt(input); //parser en int
        while(((input = rd.readLine())!=null) && iterator <leng)
        {
            if(iterator == 0)
            {
                len = input.length(); // faire  gaffe au \n
                for(int k = 0; k<len; k++)
                {
                    graph.add(new node(k+1));
                }
                
            }
            else
            {
                curr_len = input.length();
                if(curr_len != len)
                {
                    System.err.println("wrong input file format");
                }
            }

            for(int i = 0; i<len; i++)
            {
                t = Character.getNumericValue(input.charAt(i));
                System.out.println(t);
                if(t == 1)
                {
                    System.out.println("adding a neighboor");
                    graph.get(iterator).addNeighboor(graph.get(i));
                }
            }
            iterator += 1;
        }
        rd.close();

/*
        System.out.println("after reading the input file, it gives the following graph");
        for(int j=0; j<len; j++)
        {
            graph.get(j).printNode();
        }
*/
        return graph;
    }

    // return the graph of the ring
    public ArrayList<node> returnLogicalGFromFile(String fileName) throws IOException
    {
        // on part du principe que l'input est bonv
        BufferedReader rd = new BufferedReader(new FileReader(fileName));
        String input;
        ArrayList<node> graph = new ArrayList<node>();
        int len = 0;
        int curr_len;
        int iterator = 0;

        int t;
        input = rd.readLine();
        int leng = Integer.parseInt(input);
        //skipping the n first lines of the adjacency matrix
        while((input = rd.readLine())!=null && (iterator<leng -1))
            iterator++;
        iterator = 0;
        while((input = rd.readLine())!=null)
        {
            if(iterator == 0)
            {
                len = input.length(); // faire  gaffe au \n
                for(int k = 0; k<len; k++)
                {
                    graph.add(new node(k+1));
                }
                
            }
            else
            {
                curr_len = input.length();
                if(curr_len != len)
                {
                    System.err.println("wrong input file format");
                }
            }

            for(int i = 0; i<len; i++)
            {
                t = Character.getNumericValue(input.charAt(i));
                System.out.println(t);
                if(t == 1)
                {
                    System.out.println("adding a neighboor");
                    graph.get(iterator).addNeighboor(graph.get(i));
                }
            }
            iterator += 1;
                
        }
        rd.close();

        /*
        System.out.println("after reading the input file, it gives the following graph");
        for(int j=0; j<len; j++)
        {
            graph.get(j).printNode();
        }
        */

        return graph;
    }
}
