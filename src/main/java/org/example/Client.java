
package org.example;

import org.example.DAO.JsonConverter;
import org.example.DTO.Circuit;
import org.example.Exceptions.DaoException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

// Taken from oop-client-server-multithreaded-2024 sample

public class Client {
    private final JsonConverter jsonConverter = new JsonConverter();
    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }

    public void start() {

        try (   // create socket to connect to the server
                Socket socket = new Socket("localhost", 8888);
                // get the socket's input and output streams, and wrap them in writer and readers
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ) {
            System.out.println("Client message: The Client is running and has connected to the server");
            //ask user to enter a command
            Scanner consoleInput = new Scanner(System.in);
            System.out.println("|********************************|");
            System.out.println("|***** F1 CIRCUITS DATABASE *****|");
            System.out.println("|********************************|");
            System.out.println("1. Display Circuit by ID");
            System.out.println("2. Display All Circuits");
            System.out.println("3. Add a Circuit");
            System.out.println("4. Quit");
            System.out.println("Please enter a command: ");
            String userRequest = consoleInput.nextLine();

            while(true) {
                // send the command to the server on the socket
                out.println(userRequest);      // write the request to socket along with a newline terminator (which is required)
                // out.flush();                      // flushing buffer NOT necessary as auto flush is set to true

                // process the answer returned by the server
                //
                if (userRequest.startsWith("1")) // if the user has entered the "1" command i.e. display entity by id
                {
                    System.out.println("++DISPLAY CIRCUIT BY ID++");
                    System.out.println("Enter the ID of a Circuit you want to find.. ");
                    String id = consoleInput.nextLine(); // read user's input
                    out.println(id); // send id to server

                    String response = in.readLine(); // get new circuit from server
                    Circuit circuit = JsonConverter.jsonToCircuit(response); // convert json to circuit
                    // Print the circuit
                    System.out.println("---"+circuit.getCircuitName()+"---");
                    System.out.println("ID: " + circuit.getId());
                    System.out.println("Country: " + circuit.getCountry());
                    System.out.println("Length: " + circuit.getLength());
                    System.out.println("Turns: " + circuit.getTurns());
                    System.out.println();
                }
                else if (userRequest.startsWith("2")) // if the user has entered the "2" command i.e. display all entities
                {
                    System.out.println("++DISPLAY ALL CIRCUITS++");
                    String response = in.readLine();
                    List<Circuit> CircuitList = JsonConverter.jsonToCircuitList(response);
                    for (Circuit circuit : CircuitList) {
                        System.out.println("ID: " + circuit.getId());
                        System.out.println("Name: " + circuit.getCircuitName());
                        System.out.println("Country: " + circuit.getCountry());
                        System.out.println("Length: " + circuit.getLength());
                        System.out.println("Turns: " + circuit.getTurns());
                        System.out.println(); //To space out each circuit
                    }
                }
                else if (userRequest.startsWith("3")) // if the user has entered the "3" command
                {
                    System.out.println("++ADD A CIRCUIT++");
                    String response = in.readLine();   // wait for response
                    System.out.println("Client message: Response from server: \"" + response + "\"");
                    break;  // break out of while loop, client will exit.
                }
                else if (userRequest.startsWith("4")) // if the user has entered the "4" command i.e. Quit
                {
                    System.out.println("Goodbye :(");
                    String response = in.readLine();   // wait for response
                    System.out.println("Client message: Response from server: \"" + response + "\"");
                    break;  // break out of while loop, client will exit.
                }
                else {
                    System.out.println("Command unknown. Try again.");
                }

                //ask user to enter a command again
                consoleInput = new Scanner(System.in);
                System.out.println("|********************************|");
                System.out.println("|***** F1 CIRCUITS DATABASE *****|");
                System.out.println("|********************************|");
                System.out.println("1. Display Circuit by ID");
                System.out.println("2. Display All Circuits");
                System.out.println("3. Add a Circuit");
                System.out.println("4. Quit");
                System.out.println("Please enter a command: ");
                userRequest = consoleInput.nextLine();
            }
        } catch (IOException e) {
            System.out.println("Client message: IOException: " + e);
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }
        // sockets and streams are closed automatically due to try-with-resources
        // so no finally block required here.

        System.out.println("Exiting client, but server may still be running.");
    }
}
