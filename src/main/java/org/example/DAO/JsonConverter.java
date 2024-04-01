package org.example.DAO;

import com.google.gson.Gson;
import org.example.DTO.Circuit;
import org.example.Exceptions.DaoException;

import java.util.List;
public class JsonConverter extends MySqlCircuitDao {

    final static Gson gsonParser = new Gson();

    // Feature 7 | Written by Tomas Szabo --- 01/04/2024 --- 60 minutes
    @Override
    public String circuitListToJson(List<Circuit> circuitList) throws DaoException {
        return gsonParser.toJson(circuitList);
    }

    // Feature 8 | Written by Tomas Szabo --- 01/04/2024 --- 30 minutes
    @Override
    public String circuitToJson(Circuit circuitKey) throws DaoException {
        return gsonParser.toJson(circuitKey);
    }

}
