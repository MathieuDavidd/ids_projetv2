import java.io.*;
import java.util.ArrayList;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import java.nio.charset.StandardCharsets;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class client_node
{

    public static String id;
    public static ArrayList<String> voisins = new ArrayList<String>(); // arraylist des queues ici
    public static ArrayList<String> logicalVoisins = new ArrayList<String>(); // que deux en réalité (voisin de droite et voisin de gauche)

    public static void main(String args[]) throws Exception
    {
        


        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        // création des queues rabbit mq
           
        try (Connection connection = factory.newConnection();
        Channel channel = connection.createChannel()) {
            // déclaration des queues rabbit mq
            // déclaration de la queue de démarrage
            channel.queueDeclare(("START"), false, false, false, null);
            channel.queueDeclare(("SENDING_ID"), false, false, false, null);

            DeliverCallback startingCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                // parser le message : faire les test sur le split => est ce que ya un tableau avec une string vide ?
                String spliting_info[] = message.split(" / ");
                String tokens[] = spliting_info[0].split(" ");
                id = tokens[0];
                for(int i = 1; i<tokens.length; i++)
                {
                    voisins.add("QUEUE_"+tokens[i]);
                }
                
                tokens = spliting_info[1].split(" ");
                for(int i = 0; i<2; i++) //only two neighboor
                {
                    logicalVoisins.add(tokens[i]);
                }
            };


            // when launching the client : connecting to the set up node 
            String message = "CONNECT";
            channel.basicPublish("", "START", null, message.getBytes(StandardCharsets.UTF_8));
            
            System.out.println("Trying to setting up the instance...");

            //channel.basicConsume("SENDING_ID", true, startingCallback, consumerTag -> { });

            // partie avec basicGet simple
            GetResponse response = channel.basicGet("SENDING_ID", true);
            if (response != null) {
                String message_ = new String(response.getBody(), "UTF-8");
                System.out.println("Reçu : " + message_);
                // Traiter le message ici
                String spliting_info[] = message_.split(" / ");
                String tokens[] = spliting_info[0].split(" ");
                id = tokens[0];
                for(int i = 1; i<tokens.length; i++)
                {
                    voisins.add("QUEUE_"+tokens[i]);
                }
                
                tokens = spliting_info[1].split(" ");
                for(int i = 0; i<2; i++) //only two neighboor
                {
                    logicalVoisins.add(tokens[i]);
                }
            }else{
                System.out.println("NULL");
            }
            

            //s'inscrire sur la queue en basic consume


            // faire l'interface
            String input ="";
            System.out.print("prompt :");
            BufferedReader stdIn =
                    new BufferedReader(
                        new InputStreamReader(System.in));
            while((input = stdIn.readLine()) != null)
            {
                if(input.equals("EXIT"))
                {
                    channel.close();
                    connection.close();
                    break;
                }
                
                if(input.equals("STATUS"))
                {
                    System.out.println("=== status node no "+id+" ===");
                    System.out.println("= physical neighboor =");
                    for(int i = 0; i<voisins.size(); i++)
                    {
                        System.out.println(voisins.get(i));
                    }
                    System.out.println("= ring =");
                    System.out.println(" left : "+logicalVoisins.get(0));
                    System.out.println(" right : "+logicalVoisins.get(1));
                    System.out.println("===============================");
                }
            }

        }

        
        

    }

}