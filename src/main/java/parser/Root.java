package parser;

import object.LabWork;
import parser.parserFromJson.ParserFromJson;
import java.util.HashSet;

/**
 * Ключевой класс парсинга в json.
 * @see parser.parserToJson.ParserToJson
 * @see ParserFromJson
 */

public class Root {

    private ParserFromJson parser = new ParserFromJson(); // не может быть null
    private HashSet<LabWork> labWorkSet = new HashSet<>(); // не мохет быть null

    private boolean valid;

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean getValid() {
        return this.valid;
    }

    public void setParser(ParserFromJson parser) {
        this.parser = parser;
    }

    public void setLabWorkSet(HashSet<LabWork> labWorkSet) {
        this.labWorkSet = labWorkSet;
    }

    public HashSet<LabWork> getLabWorkSet() {
        return labWorkSet;
    }

    //to String. Пусть будет.
    @Override
    public String toString() {
        return "Root{" +
                ", labs=" + this.labWorkSet +
                '}';
    }
}
