import manager.Controller;
import manager.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;

/**
 * App запускающий класс, содержащий метод {@link #main(String[])}
 */

public class App {

    /**
     * Главный  метод, который запускает {@link Controller#start()}
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, ParseException {
        String file;
        try{
            file = args[0];
        } catch (IndexOutOfBoundsException e) {
            file = "notes.json";
        }

        Controller controller = new Controller(file);
        controller.start();
    }
}
