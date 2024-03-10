package org.example.DAO;

import org.example.DTO.Circuit;
import org.example.Exceptions.DaoException;

import java.util.List;

public interface CircuitDaoInterface
{
    List<Circuit> getAllCircuits() throws DaoException;
    Circuit getCircuitById(int id) throws DaoException;
    Circuit deleteCircuitById(int id) throws DaoException;
    Circuit insertCircuit(Circuit c) throws DaoException; // by Darren Meidl
}


