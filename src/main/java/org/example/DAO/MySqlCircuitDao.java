package org.example.DAO;

import org.example.DTO.Circuit;
import org.example.Exceptions.DaoException;


import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class MySqlCircuitDao extends MySqlDao implements CircuitDaoInterface
{
    // Written by Petr Sulc
    @Override
    public List<Circuit> getAllCircuits() throws DaoException {
        return SQLConnectionDecorator(
                (sql) -> {
                    List<Circuit> output = new ArrayList<>();
                    String query = "SELECT * FROM Circuits";
                    sql.statement = sql.connection.prepareStatement(query);
                    sql.result = sql.statement.executeQuery();
                    Circuit next;
                    while(sql.result.next())
                    {
                        next = new Circuit(
                                sql.result.getInt("id"),
                                sql.result.getString("circuit_name"),
                                sql.result.getString("country"),
                                sql.result.getFloat("length"),
                                sql.result.getInt("turns")
                        );
                        output.add(next);
                    }
                    return output;
                }
        );
    }

    // Written by Petr Sulc
    @Override
    public Circuit getCircuitById(int id) throws DaoException {
        return SQLConnectionDecorator(
                (sql) -> {
                    String query = "SELECT * FROM Circuits WHERE id = ?";
                    sql.statement = sql.connection.prepareStatement(query);
                    sql.statement.setInt(1,id);
                    sql.result = sql.statement.executeQuery();
                    if(sql.result.next())
                    {
                        return new Circuit(
                                sql.result.getInt("id"),
                                sql.result.getString("circuit_name"),
                                sql.result.getString("country"),
                                sql.result.getFloat("length"),
                                sql.result.getInt("turns")
                        );
                    }
                    return null;
                }
        );
    }

    // Written by Tomas Szabo
    @Override
    public Circuit deleteCircuitById(int id) throws DaoException {
        return SQLConnectionDecorator(
                (sql) -> {
                    //Prepared statement, result set and connection is inside wrapper, therefore writing sql.statement instead of PreparedStatement for example.
                    //Makes it more efficient because it does not have to be defined each time.
                    //To make the code more optimized, instead of creating code for getting id, we call method getCircuitById
                    Circuit circuit = getCircuitById(id);
                    if (circuit != null) {
                        String query2 = "DELETE FROM Circuits WHERE id = ?";
                        sql.statement = sql.connection.prepareStatement(query2);
                        sql.statement.setInt(1,id);
                        sql.statement.executeUpdate();
                    }

                    return circuit;
                }
        );
    }

    @Override // Written by Darren Meidl --- 10/03/2024 --- 1 hour
    public Circuit insertCircuit(Circuit c) throws DaoException {
        return SQLConnectionDecorator(
                (sql) -> {
                    // Gather data of this circuit object
                    int newID = c.getId();
                    String newCircuitName = c.getCircuitName();
                    String newCountry = c.getCountry();
                    float newLength = c.getLength();
                    int newTurns = c.getTurns();
                    // Create instance of Circuit object based on values from passed in 'c' object
                    Circuit newC = new Circuit(newID, newCircuitName, newCountry, newLength, newTurns);
                    // Create query
                    String query = "INSERT INTO Circuits VALUES (null, ?, ?, ?, ?)";
                    sql.statement = sql.connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS); // Ensures that the key of the row can be returned later
                    // Set the values in the insert statement to be of the new circuit object's values
                    sql.statement.setString(1,newCircuitName);
                    sql.statement.setString(2,newCountry);
                    sql.statement.setFloat(3,newLength);
                    sql.statement.setInt(4,newTurns);
                    // Execute the query
                    sql.statement.executeUpdate();
                    // Get all the generated ids from the last statement executed (in this case: 1 row inserted = 1 id)
                    sql.result = sql.statement.getGeneratedKeys();
                    if (sql.result.next()){ // only 1 row in the result set, no need for loop
                        int tempID = sql.result.getInt(1); // Get the id from the id column
                        newC.setId(tempID); // set id to the newC object
                    }
                    return newC; // After the if statement, it will ensure newC has the id of the row in the last executed statement (which is the newC object we passed in)
                }
        );
    }

    @Override // Written by Darren Meidl --- 14/03/2024 --- 50 minutes
    public Circuit updateCircuit(int id, Circuit c) throws DaoException {
        return SQLConnectionDecorator(
                (sql) -> {
                    // Gather data of this circuit object
                    String newCircuitName = c.getCircuitName();
                    String newCountry = c.getCountry();
                    float newLength = c.getLength();
                    int newTurns = c.getTurns();
                    // Create instance of Circuit object based on values from passed in 'c' object
                    Circuit newC = new Circuit(id, newCircuitName, newCountry, newLength, newTurns);
                    // Create query
                    String query = "UPDATE Circuits SET circuit_name = ?, country = ?, length = ?, turns = ? WHERE id = ?";
                    sql.statement = sql.connection.prepareStatement(query);
                    // Set the values in the update statement to be of the new circuit object's values
                    sql.statement.setString(1,newCircuitName);
                    sql.statement.setString(2,newCountry);
                    sql.statement.setFloat(3,newLength);
                    sql.statement.setInt(4,newTurns);
                    sql.statement.setInt(5, id);
                    // Execute the query
                    sql.statement.executeUpdate();
                    return newC;
                }
        );
    }

    // Written by Petr Sulc
    public List<Circuit> findCircuitsUsingFilter(Predicate<Circuit> filter) throws DaoException
    {
        return getAllCircuits()
                .stream()
                .filter(filter)
                .collect(Collectors.toList());
    }

}


