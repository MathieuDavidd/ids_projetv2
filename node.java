import java.io.*;
import java.util.ArrayList;


public class node
{
    int id;
    ArrayList<node> neighboor;

    public node(int ind)
    {
        this.id = ind;
        this.neighboor = new ArrayList<node>();
    }

    public void setId(int id_)
    {
        this.id = id_;
    }

    public int getId()
    {
        return this.id;
    }

    public ArrayList<Integer> getNeighboorIds()
    {
        ArrayList<Integer> temp = new ArrayList<Integer>();
        for(int i = 0; i<this.neighboor.size(); i++)
        {
            temp.add(neighboor.get(i).getId());
        }
        return temp;
    }

    public void addNeighboor(node a)
    {
        this.neighboor.add(a);
    }

    public node getNeighboorById(int ida)
    {
        node r = null;
        for(int i = 0; i<this.neighboor.size(); i++)
        {
            if(this.neighboor.get(i).getId() == ida)
            {
                r = this.neighboor.get(i);   
            }
        }
        if(r == null)
        {
            System.err.println("/!| wrong id givent to the get method /!| ");
        }
        return r;
    }

    public void printNode()
    {
        System.out.println(" === Node class === ");
        System.out.println(" id = " + Integer.toString(this.id));
        System.out.println(" neighboor (id) = "); 
        for(int i = 0; i<this.neighboor.size(); i++)
        {
            System.out.print(Integer.toString(this.neighboor.get(i).getId()) + " - ");
        }
    }

}