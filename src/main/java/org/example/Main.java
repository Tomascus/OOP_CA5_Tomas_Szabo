package org.example;

import org.example.DAO.CircuitDaoInterface;
import org.example.DAO.MySqlCircuitDao;
import org.example.DTO.Circuit;
import org.example.Exceptions.DaoException;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        CircuitDaoInterface ICircuitDao = new MySqlCircuitDao();

        try
        {
            System.out.println("\nCall getAllCircuits()");
            List<Circuit> circuits = ICircuitDao.getAllCircuits();     // call a method in the DAO
            for (Circuit circuit : circuits)
                System.out.println("Circuit: " + circuit.toString());

            System.out.println("\nCall getCircuitById(5)");
            System.out.println("Circuit: " + ICircuitDao.getCircuitById(5));

            System.out.println("\nCall deleteCircuitById(3)");
            System.out.println("Circuit: " + ICircuitDao.deleteCircuitById(3) + " deleted.");

            // By Darren Meidl
            System.out.println("\nCall insertCircuit(circuit)");
            Circuit circuit = new Circuit(90, "Redbull Ring 2", "Austria", 8, 19);
            System.out.println("Circuit: " + ICircuitDao.insertCircuit(circuit) + " inserted.");

            // By Darren Meidl
            System.out.println("\nCall updateCircuit(4, circuit)");
            Circuit c2 = new Circuit("Redbull Ring 9", "Russia", 99, 19);
            System.out.println("Circuit: " + ICircuitDao.updateCircuit(4, c2) + " updated.");

            // By Petr Sulc
            System.out.println("\nCall findCircuitsUsingFilter(country == Austria)");
            List<Circuit> filteredCircuits = ICircuitDao.findCircuitsUsingFilter((e) -> e.getCountry().equals("Austria"));
            for (Circuit filteredCircuit : filteredCircuits)
                System.out.println("Circuit: " + filteredCircuit.toString());
        }
        catch(DaoException e)
        {
            e.printStackTrace();
        }
    }
}