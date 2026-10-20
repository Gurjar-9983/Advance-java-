package ServerClient;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        try {
            // Fixed: Removed 'host:' and 'port:' labels
            Socket socket = new Socket("localhost", 5000);
            System.out.println("Connected to Server... ");

            // Added: Writer to actually send a message to the server
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            output.println("Hello from Client!"); 

            // Reader to get the server's response
            BufferedReader input = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
            );
            
            String response = input.readLine();
            System.out.println("Server Says: " + response);

            // Clean up resources
            input.close();
            output.close();
            socket.close();
            System.out.println("Client Connection Closed");

        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
