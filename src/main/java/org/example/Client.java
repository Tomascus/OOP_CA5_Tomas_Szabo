package org.example;

import org.example.DAO.JsonConverter;
import org.example.DTO.Circuit;

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
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream()))
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
                                "4. Delete a Circuit\n" +
                                "5. Receive an image\n" +
                                "6. Quit\n" +
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
                    case DELETE_CIRCUIT:
                        // By Darren Meidl --- 15/04/2024
                        System.out.println("++DELETE CIRCUIT BY ID++");
                        System.out.print("Enter the ID of a Circuit you want to delete: ");
                        id = consoleInput.nextLine(); // read user's input
                        out.println(id); // send id to server
                        response = in.readLine(); // wait for response
                        printCircuit(jsonConverter.jsonToCircuit(response));
                        break;
                    case GET_IMAGES_LIST:
                        // By Tomas Szabo --- 18/04/2024
                        System.out.println("++GET IMAGES LIST FROM THE SERVER++");
                        response = in.readLine(); // wait for response
                        String[] images = jsonConverter.jsonToImages(response);
                        for (int i = 0; i < images.length; i++) {       // print images available
                            System.out.println((i + 1) + ". " + images[i]);
                        }
                        System.out.println("Enter number of the image you would like to get: ");
                        String iNumber = consoleInput.nextLine(); // read user's input
                        out.println(iNumber);
                        receiveFile("images/image_received.png", dataInputStream);
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
        }
        // sockets and streams are closed automatically due to try-with-resources, so no finally block required here.

        System.out.println("Exiting client, but server may still be running.");
    }

    private static void receiveFile(String fileName, DataInputStream dataInputStream) throws IOException
    {
        int bytes = 0;
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);


        // DataInputStream allows us to read Java primitive types from stream e.g. readLong()
        // read the size of the file in bytes (the file length)
        long size = dataInputStream.readLong();
        System.out.println("Server: file size in bytes = " + size);


        // create a buffer to receive the incoming bytes from the socket
        byte[] buffer = new byte[4 * 1024];         // 4 kilobyte buffer

        System.out.println("Server:  Bytes remaining to be read from socket: ");

        // next, read the raw bytes in chunks (buffer size) that make up the image file
        while (size > 0 &&
                (bytes = dataInputStream.read(buffer, 0,(int)Math.min(buffer.length, size))) != -1) {

            // above, we read a number of bytes from stream to fill the buffer (if there are enough remaining)
            // - the number of bytes we must read is the smallest (min) of: the buffer length and the remaining size of the file
            //- (remember that the last chunk of data read will usually not fill the buffer)

            // Here we write the buffer data into the local file
            fileOutputStream.write(buffer, 0, bytes);

            // reduce the 'size' by the number of bytes read in.
            // 'size' represents the number of bytes remaining to be read from the socket stream.
            // We repeat this until all the bytes are dealt with and the size is reduced to zero
            size = size - bytes;
            System.out.print(size + ", ");
        }

        System.out.println("File is Received");
        fileOutputStream.close();
    }

    // By Petr Sulc --- 14/04/2024
    private void printCircuit(Circuit circuit)
    {
        System.out.printf("---%s---%nID: %s%nCountry: %s%nLength: %s%nTurns: %s%n%n",
                circuit.getCircuitName(),circuit.getId(),circuit.getCountry(),circuit.getLength(),circuit.getTurns());
    }
}