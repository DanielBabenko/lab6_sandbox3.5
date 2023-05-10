package manager;

import command.commands.ExecuteScript;
import command.commands.Invoker;
import command.commands.noInputCommands.help.GetHelpCommand;
import command.commands.noInputCommands.help.Information;
import command.inputCmdCollection.InputCommands;
import command.noInputCmdCollection.NoInputCommands;
import exceptions.NotJsonFile;
import object.LabWork;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.text.WordUtils;
import parser.Root;
import parser.parserFromJson.ParserFromJson;
import parser.parserToJson.ParserToJson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

/**
 * @see Controller нужен для вызова команд. Ыз него уже происходит вся работа программы.
 * Ключевой класс программы.
 */

public class Controller {
    private final String file;
    private Map<String, Invoker> commands = new HashMap<>(); // Map для команд БЕЗ входных данных, не может быть null
    private Map<String, Invoker> inputCommands = new HashMap<>(); // Map для команд С входными данными, не может быть null
    private HashSet<LabWork> labWorks = new HashSet<>(); // Коллекция объектов, не может быть null
    private ParserFromJson parserFromJson = new ParserFromJson(); // Парсинг в коллекцию. Не может быть null
    private GetHelpCommand help = new GetHelpCommand(new HelperController()); // Не может быть null
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));  // Не может быть null
    private HelperController helperController; // Не может быть null
    private Root root; // Не может быть null
    private ExecuteScript executeScript; // Не может быть null

    private Server server = new Server();

    /**
     * В конструкторе происходит автоматическая проверка json-файла.
     * Если в файле есть хотя бы один обьект класса LabWork он подгружается в коллекцию LabWorks класса
     *
     * @throws FileNotFoundException
     * @see LabWork
     */
    public Controller(String file) throws IOException {
        getExtensionByApacheCommonLib(file);
        this.file = file;
        if (parserFromJson.checkOnEmpty(this.file)) {
            root = parserFromJson.parse(this.file);
            labWorks = root.getLabWorkSet();
        } else {
            root = new Root();
            root.setValid(true);
        }

        this.helperController = new HelperController(this.file, getRoot(), getServer());
    }

    /**
     * Самый главный метод класса, а может и всей программы.
     * Сперва в методе запускается статический метод help.execute
     * Переменная flag нужна чтобы контролировать цикл while
     * Проверяется наличие execute_script на вводе
     *
     * @throws IOException
     */
    public void start() throws IOException, ParseException {
        if (getRoot().getValid()) {
            setExecuteScript(new ExecuteScript(getHelperController()));
            boolean flag = false;
            while (!flag) {
                String cmd = reformatCmd(getHelperController().getServer().dataFromClient());
                String[] arr = cmd.split(" ", 2);
                if (arr[0].equals("execute_script")) {
                    getExecuteScript().execute(arr[1]);
                } else if (arr[0].equals("add")) {
                    try {
                        getRoot().getLabWorkSet().add(getServer().getObjectFromClient());
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                } else if (arr[0].equals("exit")){
                    // close socket connection
                    getServer().getServerSocket().close();
                    System.exit(0);
                }
                searchCommandInCollection(cmd);

                save();
                System.out.println("---------------------");
                System.out.println("? Если возникли трудности, введите команду help");
            }
        }
    }

    public void save() {
        ParserToJson parserToJson = new ParserToJson();

        if (parserToJson.serialization(getRoot().getLabWorkSet(), this.file)) {
            System.out.println("Ызменения в коллекции " + getRoot().getLabWorkSet().getClass().getSimpleName() + " успешно сохранены в файл!");
        } else {
            System.out.println("Что-то пошлое не так :(");
        }
    }


    /**
     * В параметры метода передается переменная типа String
     * Цикл foreach проходит по каждому обьекту коллекции commandArrayList, чтобы найти нужную команду
     *
     * @param
     */
    public void searchCommandInCollection(String cmd) throws IOException, ParseException {

        getHelperController().setReader(new BufferedReader(new InputStreamReader(System.in)));

        NoInputCommands noInputCommands = new NoInputCommands(helperController);
        setCommands(noInputCommands.getCommands());

        InputCommands inputCommands = new InputCommands(helperController);
        setInputCommands(inputCommands.getInputCommands());

        boolean flag = true;
        //  No input commands
        for (Map.Entry<String, Invoker> entry : getCommands().entrySet()) {
            String key = entry.getKey();
            if (cmd.equals(key)) {
                System.out.println("Активирована команда " + entry.getValue().getClass().getSimpleName());
                entry.getValue().doCommand(cmd);
                flag = false;
                break;
            }
        }

        //если не было совпадений в первом мапе, пробегаемся по мапу команд с аргументами
        for (Map.Entry<String, Invoker> entry : getInputCommands().entrySet()) {
            String commandValue = "";
            String commandKey = "";
            if (cmd.contains(" ")) {
                String[] arr = cmd.split(" ", 2);

                commandKey = arr[0];
                commandValue = arr[1];

            } else {
                commandKey = cmd;
            }
            String key = entry.getKey();
            if (commandKey.equals(key)) {
                System.out.println("Активирована команда " + entry.getValue().getClass().getSimpleName());
                entry.getValue().doCommand(commandValue);
                flag = false;
                break;
            }
        }
        if (flag == true){
            getServer().sentToClient("Невалидный ввод данных, повторите попытку.");
        }
    }

    /**
     * Метод форматирует введенные данные, и преобразовывает в нужную форму.
     *
     * @param cmd
     * @return
     */
    private String reformatCmd(String cmd) {
        if (cmd != null && !checkOnExecuteScript(cmd)) {
            if (cmd.contains(" ")) {
                String[] arr = cmd.split(" ", 2);
                cmd = arr[0].replaceAll("_", " ");
                cmd = WordUtils.capitalize(cmd);
                cmd = cmd.replaceAll(" ", "");
                cmd = cmd.concat(" " + arr[1]);
            } else {
                cmd = cmd.replaceAll("_", " ");
                cmd = WordUtils.capitalize(cmd);
                cmd = cmd.replaceAll(" ", "");
            }
        } else {
            return cmd;
        }
        return cmd;
    }

    /**
     * Метод проверяет наличие в введенных данных команду execute_script
     * Если execute_script, то выкидывается true, иначе false.
     */
    private boolean checkOnExecuteScript(String cmd) {
        if (cmd != null) {
            String[] arr = cmd.split(" ", 2);
            return Objects.equals(arr[0], "execute_script");
        }

        return false;
    }

    public void getExtensionByApacheCommonLib(String filename) {
        try {
            if (!FilenameUtils.getExtension(filename).equals("json")) {
                throw new NotJsonFile("Файл должен быть с расширением json");
            }
        } catch (NotJsonFile e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }

    public void setRoot(Root root) {
        this.root = root;
    }

    public Root getRoot() {
        return root;
    }

    public void setCommands(Map<String, Invoker> commands) {
        this.commands = commands;
    }

    public Map<String, Invoker> getCommands() {
        return commands;
    }

    public void setInputCommands(Map<String, Invoker> inputCommands) {
        this.inputCommands = inputCommands;
    }

    public Map<String, Invoker> getInputCommands() {
        return inputCommands;
    }

    public HelperController getHelperController() {
        return helperController;
    }

    public ExecuteScript getExecuteScript() {
        return executeScript;
    }

    public void setExecuteScript(ExecuteScript executeScript) {
        this.executeScript = executeScript;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    @Override
    public String toString() {
        return "Controller{" +
                "commands=" + commands +
                ", inputCommands=" + inputCommands +
                ", labWorks=" + labWorks +
                ", parserFromJson=" + parserFromJson +
                ", help=" + help +
                ", reader=" + reader +
                ", helperController=" + helperController +
                ", root=" + root +
                ", executeScript=" + executeScript +
                '}';
    }
}