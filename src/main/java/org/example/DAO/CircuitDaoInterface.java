package org.example.DAO;

import org.example.DTO.Circuit;
import org.example.Exceptions.DaoException;

import java.util.List;

public interface CircuitDaoInterface
{
    List<Circuit> getAllCircuits() throws DaoException;
    Circuit getCircuitById(int id) throws DaoException;
}


