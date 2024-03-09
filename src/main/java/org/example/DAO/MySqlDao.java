package org.example.DAO;

import org.example.Exceptions.DaoException;

import java.sql.*;

public class MySqlDao
{
    // Taken from oop-data-access-layer-sample-1
    public Connection getConnection() throws DaoException
    {
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/F1";
        String username = "root";
        String password = "";
        Connection connection = null;

        try
        {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Failed to find driver class " + e.getMessage());
            System.exit(1);
        }
        catch (SQLException e)
        {
            System.out.println("Connection failed " + e.getMessage());
            System.exit(2);
        }
        return connection;
    }

    // Taken from oop-data-access-layer-sample-1
    public void freeConnection(Connection connection) throws DaoException
    {
        try
        {
            if (connection != null)
            {
                connection.close();
                connection = null;
            }
        }
        catch (SQLException e)
        {
            System.out.println("Failed to free connection: " + e.getMessage());
            System.exit(1);
        }
    }

    /*
        Written by Petr Sulc
        A decorator method that takes in a lambda function. The lambda function takes in an SQlWrapper
        which holds the connection, statement and resultSet variables. The method opens a new connection and passes it
        to the lambda through the SQLWrapper. The lambda is then called and its output saved. The method then closes
        the connection, statement and resultSet and returns the saved lambda output.
    */
    public <T> T SQLConnectionDecorator(CheckedFunction<SQLWrapper,T> statementFunction) throws DaoException
    {

        SQLWrapper wrapper = new SQLWrapper(null,null,null);
        T out;

        try {
            wrapper.connection = this.getConnection();
            out = statementFunction.apply(wrapper);

        }
        catch (SQLException e) {
            throw new DaoException(statementFunction.toString() + "() " + e.getMessage());
        }
        finally {
            try
            {
                if (wrapper.result != null)
                {
                    wrapper.result.close();
                }
                if (wrapper.statement != null)
                {
                    wrapper.statement.close();
                }
                if (wrapper.connection != null)
                {
                    freeConnection(wrapper.connection);
                }
            } catch (SQLException e)
            {
                throw new DaoException(statementFunction.toString() + "() " + e.getMessage());
            }
        }
        return out;
    }

    /*
        Written by Petr Sulc
        Wrapper object that holds a connection, statement and resultSet.
        Is used to pass values between the SQLConnectionDecorator and its lambda function.
    */
    class SQLWrapper
    {
        Connection connection;
        PreparedStatement statement;
        ResultSet result;
        SQLWrapper(Connection connection, PreparedStatement statement, ResultSet result)
        {
            this.connection = connection;
            this.statement = statement;
            this.result = result;
        }
    }

    // Written by Petr Sulc
    @FunctionalInterface
    interface CheckedFunction<T, R> {
        R apply(T t) throws SQLException;
    }
}