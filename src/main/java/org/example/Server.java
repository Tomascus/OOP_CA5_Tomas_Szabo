package org.example;

import org.example.DAO.CircuitDaoInterface;
import org.example.DAO.JsonConverter;
import org.example.DAO.MySqlCircuitDao;
import org.example.DTO.Circuit;
import org.example.Exceptions.DaoException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

// Taken from oop-client-server-multithreaded-2024 sample

public class Server {

    final int SERVER_PORT_NUMBER = 8888;  // could be any port from 1024 to 49151 (that doesn't clash with other Apps)

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    public void start() {

        ServerSocket serverSocket =null;
        Socket clientSocket =null;

        try {
            serverSocket = new ServerSocket(SERVER_PORT_NUMBER);
            System.out.println("Server has started.");
            int clientNumber = 0;  // a number sequentially allocated to each new client (for identification purposes here)

            while (true) {
                System.out.println("Server: Listening/waiting for connections on port ..." + SERVER_PORT_NUMBER);
                clientSocket = serverSocket.accept();
                clientNumber++;
                System.out.println("Server: Listening for connections on port ..." + SERVER_PORT_NUMBER);

                System.out.println("Server: Client " + clientNumber + " has connected.");
                System.out.println("Server: Port number of remote client: " + clientSocket.getPort());
                System.out.println("Server: Port number of the socket used to talk with client " + clientSocket.getLocalPort());

                // create a new ClientHandler for the requesting client, passing in the socket, client number, JsonConverter & CircuitDaoInterface
                JsonConverter j = new JsonConverter();
                CircuitDaoInterface i = new MySqlCircuitDao();
                // pass the handler into a new thread, and start the handler running in the thread.
                Thread t = new Thread(new ClientHandler(clientSocket, clientNumber, i, j));
                t.start();

                System.out.println("Server: ClientHandler started in thread " + t.getName() + " for client " + clientNumber + ". ");

            }
        } catch (IOException ex) {
            System.out.println(ex);
        }
        finally{
            try {
                if(clientSocket!=null)
                    clientSocket.close();
            } catch (IOException e) {
                System.out.println(e);
            }
            try {
                if(serverSocket!=null)
                    serverSocket.close();
            } catch (IOException e) {
                System.out.println(e);
            }

        }
        System.out.println("Server: Server exiting, Goodbye!");
    }
}

// Taken from oop-client-server-multithreaded-2024 sample

class ClientHandler implements Runnable   // each ClientHandler communicates with one Client
{
    BufferedReader socketReader;
    PrintWriter socketWriter;
    Socket clientSocket;
    final int clientNumber;
    private CircuitDaoInterface circuitDaoInterface;
    private JsonConverter jsonConverter;

    // Constructor by Darren Meidl --- 13/04/2024
    public ClientHandler(Socket clientSocket, int clientNumber, CircuitDaoInterface i, JsonConverter j) {
        this.clientSocket = clientSocket;  // store socket for closing later
        this.clientNumber = clientNumber;  // ID number that we are assigning to this client
        this.circuitDaoInterface = i;
        this.jsonConverter = j;
        try {
            // assign to fields
            this.socketWriter = new PrintWriter(clientSocket.getOutputStream(), true);
            this.socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Taken from oop-client-server-multithreaded-2024 sample
    @Override
    public void run() {
        String command;
        ServerRequest request;
        String jsonMessage;
        try {
            while ((command = socketReader.readLine()) != null) {
                System.out.println("Server: (ClientHandler): Read command from client " + clientNumber + ": " + command);
                request = ServerRequest.idToRequest(command);

                // Implement our PROTOCOL
                // The protocol is the logic that determines the responses given based on requests received.

                // By Petr Sulc --- 14/04/2024
                switch(request)
                {
                    case GET_CIRCUIT_BY_ID:
                        // By Darren Meidl --- 13/04/2024
                        String cID = socketReader.readLine(); // read id sent by client
                        int id = Integer.parseInt(cID); // convert id to integer
                        Circuit c = circuitDaoInterface.getCircuitById(id); // get circuit by id
                        jsonMessage = jsonConverter.circuitToJson(c);
                        socketWriter.println(jsonMessage); // send the received message back to the client
                        System.out.println("Server message: circuit with id " + id + " sent to client.");
                        break;
                    case GET_ALL_CIRCUITS:
                        // By Tomas Szabo --- 12/04/2024
                        List<Circuit> message = circuitDaoInterface.getAllCircuits(); // get all circuits from DAO method
                        jsonMessage = jsonConverter.circuitListToJson(message); // converts to json
                        socketWriter.println(jsonMessage);   // send the received message back to the client
                        System.out.println("Server message: json message sent to client.");
                        break;
                    case ADD_NEW_CIRCUIT:
                        // By Petr Sulc --- 14/04/2024
                        String newCircuitJson = socketReader.readLine();
                        Circuit newCircuit = jsonConverter.jsonToCircuit(newCircuitJson);
                        try {
                            Circuit addedCircuit = circuitDaoInterface.insertCircuit(newCircuit);
                            String addedCircuitJson = jsonConverter.circuitToJson(addedCircuit);
                            socketWriter.println(1);
                            socketWriter.println(addedCircuitJson);
                        }
                        catch (DaoException e)
                        {
                            socketWriter.println(0);
                            socketWriter.println("Insertion Failed: " + e.getMessage());
                        }
                        break;
                    case DISCONNECT:
                        socketWriter.println("Sorry to see you leaving. Goodbye.");
                        System.out.println("Server message: Client has notified us that it is quitting.");
                        break;
                    case UNKNOWN_REQUEST:
                        socketWriter.println("ERROR: I'm sorry, I don't understand your request");
                        System.out.println("Server message: Invalid request from client.");
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }
        finally
        {
            this.socketWriter.close();
            try
            {
                this.socketReader.close();
                this.clientSocket.close();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("Server: (ClientHandler): Handler for Client " + clientNumber + " is terminating .....");
    }
}

// By Petr Sulc --- 14/04/2024
enum ServerRequest
{
    UNKNOWN_REQUEST(0),
    GET_CIRCUIT_BY_ID(1),
    GET_ALL_CIRCUITS(2),
    ADD_NEW_CIRCUIT(3),
    DISCONNECT(4);

    public final int id;
    ServerRequest(int id)
    {
        this.id = id;
    }

    // By Petr Sulc --- 14/04/2024
    public static ServerRequest idToRequest(int id)
    {
        return (0 < id && id < ServerRequest.values().length) ? ServerRequest.values()[id] : UNKNOWN_REQUEST;
    }

    // By Petr Sulc --- 14/04/2024
    public static ServerRequest idToRequest(String id)
    {
        try {
            return ServerRequest.idToRequest(Integer.parseInt(id));
        }
        catch(NumberFormatException e) {
            return UNKNOWN_REQUEST;
        }
    }
}