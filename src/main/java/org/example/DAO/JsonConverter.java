package org.example.DAO;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.DTO.Circuit;
import org.example.Exceptions.DaoException;

import java.util.List;
public class JsonConverter {

    final static Gson gsonParser = new Gson();

    // Feature 7 | Written by Tomas Szabo --- 01/04/2024 --- 60 minutes

    public String circuitListToJson(List<Circuit> circuitList) throws DaoException {
        return gsonParser.toJson(circuitList);
    }

    // Feature 10 | Written by Tomas Szabo --- 12/04/2024 --- 20 minutes

    public List<Circuit> jsonToCircuitList(String json) throws DaoException {
        return gsonParser.fromJson(json, new TypeToken<List<Circuit>>(){}.getType()); //TypeToken gson class to specify generic type, in our case specifies that it is list of circuit objects
    }

    // Feature 8 | Written by Tomas Szabo --- 01/04/2024 --- 30 minutes
    public String circuitToJson(Circuit circuitKey) throws DaoException {
        return gsonParser.toJson(circuitKey);
    }

    // Feature 9 | Written by Darren Meidl --- 13/04/2024 - 5 minutes
    public Circuit jsonToCircuit(String json) throws DaoException {
        Circuit c = gsonParser.fromJson(json, Circuit.class);
        return c;
    }
}