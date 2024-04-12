package org.example;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Scanner;

// Taken from oop-client-server-multithreaded-2024 sample

public class Client {
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
            System.out.println("Valid commands are: \"time\" to get time, or \"echo <message>\" to get message echoed back, \"quit\"");
            System.out.println("Please enter a command: ");
            String userRequest = consoleInput.nextLine();

            while(true) {
                // send the command to the server on the socket
                out.println(userRequest);      // write the request to socket along with a newline terminator (which is required)
                // out.flush();                      // flushing buffer NOT necessary as auto flush is set to true

                // process the answer returned by the server
                //
                if (userRequest.startsWith("time"))   // if user asked for "time", we expect the server to return a time (in milliseconds)
                {
                    String timeString = in.readLine();  // (blocks) waits for response from server, then input string terminated by a newline character ("\n")
                    System.out.println("Client message: Response from server after \"time\" request: " + timeString);
                }
                else if (userRequest.startsWith("echo")) // if the user has entered the "echo" command
                {
                    String response = in.readLine();   // wait for response - expecting it to be the same message that we sent to server
                    System.out.println("Client message: Response from server: \"" + response + "\"");
                }
                else if (userRequest.startsWith("quit")) // if the user has entered the "quit" command
                {
                    String response = in.readLine();   // wait for response -
                    System.out.println("Client message: Response from server: \"" + response + "\"");
                    break;  // break out of while loop, client will exit.
                }
                else {
                    System.out.println("Command unknown. Try again.");
                }

                consoleInput = new Scanner(System.in);
                System.out.println("Valid commands are: \"time\" to get time, or \"echo <message>\" to get message echoed back, \"quit\"");
                System.out.println("Please enter a command: ");
                userRequest = consoleInput.nextLine();
            }
        } catch (IOException e) {
            System.out.println("Client message: IOException: " + e);
        }
        // sockets and streams are closed automatically due to try-with-resources
        // so no finally block required here.

        System.out.println("Exiting client, but server may still be running.");
    }
}

//  LocalTime time = LocalTime.parse(timeString); // Parse String -> convert to LocalTime object if required LocalTime.parse(timeString); // Parse timeString -> convert to LocalTime object if required