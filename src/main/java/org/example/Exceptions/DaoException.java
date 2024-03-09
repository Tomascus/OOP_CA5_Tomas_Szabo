package org.example.Exceptions;

import java.sql.SQLException;

// Taken from oop-data-access-layer-sample-1
public class DaoException extends SQLException
{
    public DaoException() {}
    public DaoException(String aMessage)
    {
        super(aMessage);
    }
}
