package org.example.DAO;

import org.example.DTO.Circuit;
import org.example.Exceptions.DaoException;
import java.util.function.Predicate;

import java.util.List;

public interface CircuitDaoInterface
{
    List<Circuit> getAllCircuits() throws DaoException;
    Circuit getCircuitById(int id) throws DaoException;
    Circuit deleteCircuitById(int id) throws DaoException;
    Circuit insertCircuit(Circuit c) throws DaoException; // by Darren Meidl
    Circuit updateCircuit(int id, Circuit c) throws DaoException; // by Darren Meidl
    List<Circuit> findCircuitsUsingFilter(Predicate<Circuit> filter) throws DaoException;
    String circuitListToJson(List<Circuit> circuitList) throws DaoException;
    String circuitToJson(Circuit circuitKey) throws DaoException;

}


