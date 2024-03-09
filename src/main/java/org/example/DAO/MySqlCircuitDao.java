package org.example.DAO;

import org.example.DTO.Circuit;
import org.example.Exceptions.DaoException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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
                    PreparedStatement preparedStatement = null;
                    String query = "SELECT * FROM Circuits WHERE id = ?";
                    preparedStatement = sql.connection.prepareStatement(query);
                    preparedStatement.setInt(1,id);
                    ResultSet rs = sql.statement.executeQuery();
                    Circuit circuit = null;
                    if(rs.next())
                    {
                        circuit = new Circuit(
                                rs.getInt("id"),
                                rs.getString("circuit_name"),
                                rs.getString("country"),
                                rs.getFloat("length"),
                                rs.getInt("turns")
                        );

                        String query2 = "DELETE FROM Circuits WHERE id = ?";
                        preparedStatement = sql.connection.prepareStatement(query2);
                        preparedStatement.setInt(1,id);
                        preparedStatement.executeUpdate();
                    }

                    return circuit;
                }
        );
    }
}


