package org.example;

import org.example.DTO.Circuit;
import org.example.DAO.JsonConverter;
import org.example.Exceptions.DaoException;

import java.io.*;
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
            System.out.println("Valid commands are: \"1\" to Display Entity by Id, or \"2\" to “Display all Entities, \"3\" to “Add an Entity.");
            System.out.println("Please enter a command: ");
            String userRequest = consoleInput.nextLine();

            while(true) {
                // send the command to the server on the socket
                out.println(userRequest);      // write the request to socket along with a newline terminator (which is required)
                // out.flush();                      // flushing buffer NOT necessary as auto flush is set to true

                // process the answer returned by the server
                //
                if (userRequest.startsWith("1"))   // if user asked for "1", we expect the server to return a time (in milliseconds)
                {
                    String timeString = in.readLine();  // (blocks) waits for response from server, then input string terminated by a newline character ("\n")
                    System.out.println("Client message: Response from server after \"time\" request: " + timeString);
                }
                else if (userRequest.startsWith("2")) // if the user has entered the "2" command
                {
                    String response = in.readLine();
                    List<Circuit> CircuitList = JsonConverter.jsonToCircuitList(in.readLine());
                    for (Circuit circuit : CircuitList) {
                        System.out.println("ID: " + circuit.getId());
                        System.out.println("Name: " + circuit.getCircuitName());
                        System.out.println("Country: " + circuit.getCountry());
                        System.out.println("Length: " + circuit.getLength());
                        System.out.println("Turns: " + circuit.getTurns());
                        System.out.println();
                    }
                }
                else if (userRequest.startsWith("3")) // if the user has entered the "quit" command
                {
                    String response = in.readLine();   // wait for response -
                    System.out.println("Client message: Response from server: \"" + response + "\"");
                    break;  // break out of while loop, client will exit.
                }
                else {
                    System.out.println("Command unknown. Try again.");
                }

                consoleInput = new Scanner(System.in);
                System.out.println("Valid commands are: \"1\" to Display Entity by Id, or \"2\" to Display all Entities, \"3\" to Add an Entity.");
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
