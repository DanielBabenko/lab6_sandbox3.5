package object.enums;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.SplittableRandom;

public enum Color {
    GREEN("зелёный"),
    RED("красный"),
    BLACK("чёрный"),
    ORANGE("оранжевый"),
    WHITE("белый");

    String name;

    Color(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}