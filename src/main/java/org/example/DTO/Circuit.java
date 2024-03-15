package org.example.DTO;

public class Circuit
{
    private int id;
    private String circuitName;
    private String country;
    private float length;
    private int turns;

    public Circuit(int id, String circuitName, String country, float length, int turns) {
        this.id = id;
        this.circuitName = circuitName;
        this.country = country;
        this.length = length;
        this.turns = turns;
    }

    // Used in Feature 5 method - this is for inserting new values into an existing entity
    public Circuit(String circuitName, String country, float length, int turns) {
        this.circuitName = circuitName;
        this.country = country;
        this.length = length;
        this.turns = turns;
    }

    public Circuit() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCircuitName() {
        return circuitName;
    }

    public void setCircuitName(String circuitName) {
        this.circuitName = circuitName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public int getTurns() {
        return turns;
    }

    public void setTurns(int turns) {
        this.turns = turns;
    }

    @Override
    public String toString() {
        return "Circuit{" +
                "id=" + id +
                ", circuitName='" + circuitName + '\'' +
                ", country='" + country + '\'' +
                ", length=" + length +
                ", turns=" + turns +
                '}';
    }
}
