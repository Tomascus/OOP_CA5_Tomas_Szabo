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
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())))
        {
            System.out.println("Client message: The Client is running and has connected to the server");

            boolean running = true;
            Scanner consoleInput;
            String requestId;
            ServerRequest request;
            String response;

            do {
                // Show menu
                System.out.print(
                        "|********************************|\n" +
                                "|***** F1 CIRCUITS DATABASE *****|\n" +
                                "|********************************|\n" +
                                "1. Display Circuit by ID\n" +
                                "2. Display All Circuits\n" +
                                "3. Add a Circuit\n" +
                                "4. Quit\n" +
                                "Please enter a command: ");
                consoleInput = new Scanner(System.in);
                requestId = consoleInput.nextLine();
                request = ServerRequest.idToRequest(requestId);

                // send the command to the server on the socket
                out.println(request.id);      // write the request to socket along with a newline terminator (which is required), flushing buffer NOT necessary as auto flush is set to true

                // process the answer returned by the server
                switch(request) {
                    case GET_CIRCUIT_BY_ID:
                        System.out.println("++DISPLAY CIRCUIT BY ID++");
                        System.out.print("Enter the ID of a Circuit you want to find: ");
                        String id = consoleInput.nextLine(); // read user's input
                        out.println(id); // send id to server
                        response = in.readLine(); // wait for response
                        printCircuit(jsonConverter.jsonToCircuit(response));
                        break;
                    case GET_ALL_CIRCUITS:
                        System.out.println("++DISPLAY ALL CIRCUITS++");
                        response = in.readLine(); // wait for response
                        List<Circuit> CircuitList = jsonConverter.jsonToCircuitList(response); // convert json to list
                        for (Circuit cir : CircuitList)
                            printCircuit(cir);
                        break;
                    case ADD_NEW_CIRCUIT:
                        // By Petr Sulc --- 14/04/2024
                        System.out.println("++ADD A CIRCUIT++");
                        System.out.print("Circuit Name: ");
                        String cName = consoleInput.nextLine();
                        System.out.print("Country: ");
                        String cCountry = consoleInput.nextLine();
                        System.out.print("Length: ");
                        float cLength = consoleInput.nextFloat();
                        System.out.print("Turns: ");
                        int cTurns = consoleInput.nextInt();
                        Circuit newCircuit = new Circuit(cName,cCountry,cLength,cTurns);
                        String newCircuitJson = jsonConverter.circuitToJson(newCircuit);
                        out.println(newCircuitJson); // send new circuit to server
                        String success = in.readLine();
                        response = in.readLine();   // wait for response
                        if(success.equals("1"))
                            printCircuit(jsonConverter.jsonToCircuit(response)); // print added circuit
                        else
                            System.out.println("Client message: Response from server: \"" + response + "\"");
                        break;
                    case DISCONNECT:
                        System.out.println("Goodbye :(");
                        response = in.readLine();   // wait for response
                        System.out.println("Client message: Response from server: \"" + response + "\"");
                        running = false; // set running to false, client will exit.
                        break;
                    case UNKNOWN_REQUEST:
                        response = in.readLine();
                        System.out.println("Client message: Response from server: \"" + response + "\"");
                        break;
                }
            } while (running);
        } catch (IOException e) {
            System.out.println("Client message: IOException: " + e);
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }
        // sockets and streams are closed automatically due to try-with-resources, so no finally block required here.

        System.out.println("Exiting client, but server may still be running.");
    }

    // By Petr Sulc --- 14/04/2024
    private void printCircuit(Circuit circuit)
    {
        System.out.printf("---%s---%nID: %s%nCountry: %s%nLength: %s%nTurns: %s%n%n",
                circuit.getCircuitName(),circuit.getId(),circuit.getCountry(),circuit.getLength(),circuit.getTurns());
    }
}