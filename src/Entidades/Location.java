package Entidades;

import java.io.Serializable;

public class Location implements Serializable {
    private Long x; //Поле не может быть null
    private float y;
    private Float z; //Поле не может быть null
    private String name; //Поле не может быть null

    public Location(Long x, float y, Float z, String name) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
    }

    public Location() {

    }

    public Long getX() {
        return x;
    }

    public void setX(Long x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Float getZ() {
        return z;
    }

    public void setZ(Float z) {
        this.z = z;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toCsv() {
        String xCsv = String.format("%s,", getX());
        String yCsv = String.format("%s,", getY());
        String zCsv = String.format("%s,", getZ());
        String nameCsv = String.format("%s", getName());
        return String.format("%s%s%s%s", xCsv, yCsv, zCsv, nameCsv);
    }
}
