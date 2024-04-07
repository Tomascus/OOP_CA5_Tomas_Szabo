package org.example;

import org.example.DAO.JsonConverter;
import org.example.DAO.MySqlCircuitDao;
import org.example.DTO.Circuit;
import org.example.Exceptions.DaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

class MySqlCircuitDaoTest { // by Darren Meidl
    private MySqlCircuitDao dao; // declare
    private JsonConverter Json; // declare - by Tomas Szabo
    @BeforeEach
    void setUp() {
        dao = new MySqlCircuitDao(); // initialize
        Json = new JsonConverter(); // initialize - by Tomas Szabo
    }
    // INSERT CIRCUIT TESTS
    @Test
    void insertCircuitTestByName() throws DaoException { // By Darren Meidl --- 06/04/2024
        System.out.println("TEST 1: 'insertCircuit()' ");
        String expected = "t1"; // the expected name
        Circuit test = new Circuit("t1", "t1Country", 1, 1); // create a test circuit
        int testID = dao.insertCircuit(test).getId(); // insert the test circuit & get its auto id from the database
        String actual = dao.getCircuitById(testID).getCircuitName(); // Get the name from the test Circuit from the test database
        assertEquals(expected, actual); // compare the strings
        System.out.println("EXPECTED: "+expected+" ACTUAL: "+actual); // print the results
        System.out.println("");
    }
    @Test
    void insertCircuitTestByTurns() throws DaoException { // By Darren Meidl --- 06/04/2024
        System.out.println("TEST 2: 'insertCircuit()' ");
        int expected = 2; // the expected number of turns
        Circuit test = new Circuit("t2", "t2Country", 2, 2); // create a test circuit
        int testID = dao.insertCircuit(test).getId(); // insert the test circuit & get its auto id from the database
        int actual = dao.getCircuitById(testID).getTurns(); // Get the turns from the test Circuit from the test database
        assertEquals(expected, actual); // compare the integers
        System.out.println("EXPECTED: "+expected+" ACTUAL: "+actual); // print the results
        System.out.println("");
    }

    // UPDATE CIRCUIT TESTS
    @Test
    void updateCircuitTestByName() throws DaoException { // By Darren Meidl --- 06/04/2024
        System.out.println("TEST 1: 'updateCircuit()' ");
        String expected = "t1"; // the expected name
        Circuit test = new Circuit("t1", "t1Country", 1, 1); // create a test circuit
        dao.updateCircuit(1, test); // update circuit with id 1 with the test circuit fields
        String actual = dao.getCircuitById(1).getCircuitName(); // Get the name from the test Circuit from the test database
        assertEquals(expected, actual); // compare the strings
        System.out.println("EXPECTED: "+expected+" ACTUAL: "+actual); // print the results
        System.out.println("");
    }

    @Test
    void updateCircuitTestByTurns() throws DaoException { // By Darren Meidl --- 06/04/2024
        System.out.println("TEST 2: 'updateCircuit()' ");
        int expected = 2; // the expected number of turns
        Circuit test = new Circuit("t2", "t2Country", 2, 2); // create a test circuit
        dao.updateCircuit(1, test); // update circuit with id 1 with the test circuit fields
        int actual = dao.getCircuitById(1).getTurns(); // Get the turns from the test Circuit from the test database
        assertEquals(expected, actual); // compare the integers
        System.out.println("EXPECTED: "+expected+" ACTUAL: "+actual); // print the results
        System.out.println("");
    }

    @Test
    void deleteCircuitByIdTest() throws DaoException { // By Tomas Szabo --- 07/04/2024
        System.out.println("TEST 1: 'deleteCircuit()' ");
        Circuit test = new Circuit("Circuit A", "Country A", 18.4f, 4); // create a test circuit
        int testID = dao.insertCircuit(test).getId(); // insert the test circuit & get its auto id from the database
        Circuit deletedCircuit = dao.deleteCircuitById(testID); // delete test circuit by the test id
        int expected = testID;
        int actual = deletedCircuit.getId();
        assertEquals(expected, actual); // compare the ID, if they match
        System.out.println("EXPECTED ID: " + expected + " ACTUAL ID: " + actual);
        assertNull(dao.getCircuitById(testID)); // null check to verify the deleted circuit is not present in database anymore
        System.out.println("Circuit with ID " + testID + " deleted successfully.");
        System.out.println("");
    }

    @Test
    void circuitListToJsonTest() throws DaoException { // By Tomas Szabo --- 07/04/2024
        System.out.println("TEST: 'circuitListToJson()' ");
        List<Circuit> circuitList = new ArrayList<>(); //Create a list of circuit objects to be tested
        circuitList.add(new Circuit(1, "Circuit A", "Country A", 10.5f, 7));
        circuitList.add(new Circuit(2, "Circuit B", "Country B", 8.2f, 6));
        circuitList.add(new Circuit(3, "Circuit C", "Country C", 7.0f, 2));

        String expected = "[{\"id\":1,\"circuitName\":\"Circuit A\",\"country\":\"Country A\",\"length\":10.5,\"turns\":7},{\"id\":2,\"circuitName\":\"Circuit B\",\"country\":\"Country B\",\"length\":8.2,\"turns\":6},{\"id\":3,\"circuitName\":\"Circuit C\",\"country\":\"Country C\",\"length\":7.0,\"turns\":2}]";
        String actual = Json.circuitListToJson(circuitList);
        assertEquals(expected, actual); //Compare predetermined json and actual method
        System.out.println("EXPECTED JSON: " + expected);
        System.out.println("ACTUAL JSON: " + actual);
        System.out.println("");
    }

    @Test
    void circuitToJsonTest() throws DaoException { // By Tomas Szabo --- 07/04/2024
        System.out.println("TEST: 'circuitToJson()' ");
        Circuit circuit = new Circuit(1, "Circuit A", "Country A", 10.5f, 7); //Create single circuit object to be tested

        String expected = "{\"id\":1,\"circuitName\":\"Circuit A\",\"country\":\"Country A\",\"length\":10.5,\"turns\":7}";
        String actual = Json.circuitToJson(circuit);
        assertEquals(expected, actual);
        System.out.println("EXPECTED JSON: " + expected);
        System.out.println("ACTUAL JSON: " + actual);
        System.out.println("");
    }
}