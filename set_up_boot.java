import java.io.*;
import java.util.ArrayList;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import java.nio.charset.StandardCharsets;

public class set_up_boot
{
    public static ArrayList<node> gg = new ArrayList<node>(); // pas sur 
    public static ArrayList<node> lg = new ArrayList<node>();
    public static int nb_client = 1;

    public static void main(String args[]) throws Exception
    {
        // getting the input and the linked graph
        if(args.length < 1)
        {
            System.err.println("bad number of argument");
        }
        else
        {
            System.out.println(args[0]);
        }
        input_reader rd = new input_reader();
        try
        {
            gg = rd.returnFromFile(args[0]);
            lg = rd.returnLogicalGFromFile(args[0]);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        // rabbitmq code
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
         try (Connection connection = factory.newConnection();
            Channel channel = connection.createChannel()) {
            // déclaration des queues rabbit mq
            // déclaration de la queue de démarrage
            channel.queueDeclare(("START"), false, false, false, null);
            channel.queueDeclare(("SENDING_ID"), false, false, false, null);
            for(int i = 0; i<gg.size();i++)
            {
                channel.queueDeclare(("QUEUE_"+Integer.toString(gg.get(i).getId())), false, false, false, null);
            }
            
            // connection des clients/nodes

            DeliverCallback startingCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println(" [x] asking to connect '" + message + "'");

                if(message.equals("CONNECT"))
                {
                    // sending the id  # lorsqu'on renvoie les id et les voisins on a : "id voisin1 voisin2 ... / voisins_logique1 voisin_logique2..."
                    System.out.println("client : "+Integer.toString(nb_client)+" trying to connect");
                    ArrayList<Integer> temp = gg.get(nb_client-1).getNeighboorIds();
                    String voisin = Integer.toString(nb_client) + " ";
                    for(int i = 0; i<temp.size(); i++)
                    {
                        voisin+= Integer.toString(temp.get(i))+" ";
                    }
                    voisin+= "/ ";
                    temp = lg.get(nb_client-1).getNeighboorIds();
                    for(int i = 0; i<temp.size(); i++)
                    {
                        voisin+= Integer.toString(temp.get(i))+" ";
                    }
                    System.out.println("sending : "+voisin);
                    channel.basicPublish("", "SENDING_ID", null, (voisin).getBytes(StandardCharsets.UTF_8));
                    nb_client++;
                }
                else
                {
                    // pas sûr de l'utilité
                    System.out.println("[x] Failed to connect : wrong codeword ");
                }
            };


            
            while(nb_client <= gg.size())
            {
                channel.basicConsume("START", true, startingCallback, consumerTag -> { });
            }
            
            

            System.out.println("End of the boot step... Exiting");
            // doit faire la partie ou je donne la topologie du ring
            channel.close();
            connection.close();
        }

    }
}