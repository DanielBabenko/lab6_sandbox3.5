package object;


import exceptions.InvalidFieldY;
import exceptions.NullX;

public class Coordinates {
    private Integer x; //Поле не может быть null
    private Double y; //Значение поля должно быть больше -184, Поле не может быть null

    public Coordinates() {
    }

    public Coordinates(Integer x, Double y) throws NullX, InvalidFieldY {
        if (x == null) {
            throw new NullX("Field X can not be NULL!");
        } else {
            this.x = x;
        }
        if (y == null) {
            throw new InvalidFieldY("Field Y can not be NULL");
        } else {
            this.y = y;
        }
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getX() {
        return x;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Double getY() {
        return y;
    }

}
