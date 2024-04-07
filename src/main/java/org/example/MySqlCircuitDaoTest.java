package org.example;

import org.example.DAO.MySqlCircuitDao;
import org.example.DTO.Circuit;
import org.example.Exceptions.DaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MySqlCircuitDaoTest { // by Darren Meidl
    private MySqlCircuitDao dao; // declare
    @BeforeEach
    void setUp() {
        dao = new MySqlCircuitDao(); // initialize
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
}