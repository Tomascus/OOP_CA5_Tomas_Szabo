package org.example.DAO;

import com.google.gson.Gson;
import org.example.DTO.Circuit;
import org.example.Exceptions.DaoException;

import java.util.ArrayList;
import java.util.List;
public class JsonConverter extends MySqlCircuitDao implements CircuitDaoInterface {

    final static Gson gsonParser = new Gson();

    // Feature 7 | Written by Tomas Szabo --- 01/04/2024 --- 60 minutes
    public String circuitListToJson(List<Circuit> circuitList) throws DaoException {
        return gsonParser.toJson(circuitList);
    }

    // Feature 8 | Written by Tomas Szabo --- 01/04/2024 --- 30 minutes
    public String circuitToJson(Circuit circuitKey) throws DaoException {
        return gsonParser.toJson(circuitKey);
    }

}
