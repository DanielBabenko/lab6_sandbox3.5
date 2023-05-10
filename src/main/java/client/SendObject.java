package client;

import exceptions.InvalidFieldY;
import client.object.Coordinates;
import client.object.LabWork;
import client.object.Person;
import client.object.enums.Color;
import client.object.enums.Difficulty;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

public class SendObject {

    private HashSet<LabWork> labs;

    private BufferedReader reader;

    private LabWork labWork;

    public SendObject(HashSet<LabWork> labWorks) {
        this.labs = labWorks;
    }

    public void setLabWork(LabWork labWork) {
        this.labWork = labWork;
    }

    public LabWork getLabWork() {
        return labWork;
    }

    public void start() {
        try {
            this.labWork = adder();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private LabWork adder() throws IOException {
        System.out.println("Введите название Лабораторной работы: ");
        String name = null;
        while (name == null) {
            try {
                name = reader.readLine().trim();
                if (name == null || name.isEmpty()) {
                    throw new RuntimeException("Пустая строка не может именем лабораторной работы. Попробуй ещё раз.");
                }
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
                name = null;
            }
        }
        Coordinates coordinates = addCoordinates();
        Person author = addPerson();
        int minimalPoint = addMinimalPoint();
        Long tunedInWorks = addTunedInWorks();
        Difficulty difficulty = addDifficulty();
        LabWork e = new LabWork(generateId(), name, minimalPoint, tunedInWorks, difficulty, coordinates, author);
        return e;
    }

    /**
     * Доп метод для {@link#addElement(String)}: добавить сложность
     *
     * @return
     * @throws IOException
     */
    private Difficulty addDifficulty() throws IOException {
        System.out.println("Введите сложность работы (VERY_EASY, EASY, VERY_HARD, IMPOSSIBLE, HOPELESS:");
        String difficulty = checkOnEnum(Difficulty.class);
        return Difficulty.valueOf(difficulty);
    }

    /**
     * Метод генерирует id нового объекта
     *
     * @return
     */
    public int generateId() {
        Map<Integer, LabWork> labs = new HashMap<>();
        for (LabWork lab : this.labs)
            labs.put((int) lab.getId(), lab);
        labs = sortByKeys(labs);
        Integer size = labs.size();
        for (Map.Entry<Integer, LabWork> entry : labs.entrySet()) {
            if (size.equals(entry.getKey())) {
                size += 1;
            }
        }
        if (labs.size() == 0)
            return 0;
        return size;
    }

    /**
     * Сортирует коллекцию объектов по ключу.
     *
     * @param unsortedMap
     * @param <K>
     * @param <V>
     * @return
     */
    public <K, V> Map<K, V> sortByKeys(Map<K, V> unsortedMap) {
        return new TreeMap<>(unsortedMap);
    }


    /**
     * Метод обрабатывает поле
     *
     * @return
     */
    private Long addTunedInWorks() throws IOException {
        Long tunedInWorks = null;
        boolean flag = false;
        System.out.println("Введите tunedInWorks(1-1000):");
        String commandValue = reader.readLine().trim();
        if (!commandValue.trim().isEmpty())
            while (!flag) {
                try {
                    if (commandValue != null) {
                        String num = commandValue;
                        commandValue = null;
                        tunedInWorks = Long.parseLong(num);
                    } else {
                        System.out.println("Введите tunedInWorks(1-1000):");
                        tunedInWorks = checkOnLong();
                    }
                    if (tunedInWorks > 0 && tunedInWorks < 1001) {
                        flag = true;
                    }
                    //commandValue = null;
                } catch (NumberFormatException e) {
                    System.out.println(e.getMessage().trim());
                }
            }


        return tunedInWorks;
    }

    private int addMinimalPoint() throws IOException {
        int minimalPoint = 0;
        boolean flag = false;
        while (!flag) {
            System.out.println("Введите minimalPoint(1-1000):");
            minimalPoint = checkOnInt();
            if (minimalPoint > 0 && minimalPoint < 1001)
                flag = true;
            else
                System.out.println("Вы ввели некорректное число! Число не может быть отрицательным, или равно нулю.");
        }

        return minimalPoint;
    }

    private Person addPerson() throws IOException {
        boolean flag = false;
        String name = null;
        while (!flag) {
            System.out.println("Введите имя автора: ");
            name = reader.readLine().trim();
            if (!name.isEmpty())
                flag = true;
            else
                System.out.println("Поле имя автора не может быть пустым");
        }

        flag = false;
        float height = 0;
        while (!flag) {
            System.out.println("Введите рост автора: ");
            Float h = checkOnFloat();
            if (h.isInfinite())
                throw new IllegalArgumentException("Некорректный ввод. Повторите попытку.");
            if (h < 272 && h > 0) {
                flag = true;
                height = h;
            } else {
                System.out.println("Вы ввели неправильный рост! Доступно в интервале от 0 до 272.");
            }
        }


        String date = null;
        LocalDate birthday = null;
        while (date == null) {
            try {
                System.out.println("Введите дату рождения автора (гггг-мм-дд): ");
                birthday = LocalDate.parse(reader.readLine().trim());
                String[] dateSplit = birthday.toString().split("-");
                if (Integer.parseInt(dateSplit[0]) >= 1907 && Integer.parseInt(dateSplit[0]) < 2015)
                    date = dateSplit[2] + "-" + dateSplit[1] + "-" + dateSplit[0];
                else
                    System.out.println("Ты не мог родиться в такой год. Самый старый человек родился в 1907 году.Мария Браньяс Морера");
            } catch (DateTimeException e) {
                System.out.println("Невалидный ввод данных, повторите попытку.");
            }
        }

        System.out.println("Введите цвет глаз автора (GREEN, RED, ORANGE, WHITE, BLACK): ");

        String color = checkOnEnum(Color.class);

        return new Person(name, Color.valueOf(color), height, date);
    }

    private Coordinates addCoordinates() throws IOException {
        boolean flag = false;
        System.out.println("Введите координату x: ");
        int x = checkOnInt();
        double y = 0;
        while (!flag) {
            try {
                System.out.println("Введите координату y: ");
                y = checkOnDouble();

                if (y < -184) {
                    throw new InvalidFieldY("Field Y must be > -184 and can not be NULL");
                }
                flag = true;
            } catch (InvalidFieldY e) {
                System.out.println(e.getMessage());
            }
        }

        return new Coordinates(x, y);
    }

    private double checkOnDouble() throws IOException {
        double y = 0;
        boolean flag = false;
        while (!flag) {
            try {
                y = Double.parseDouble(reader.readLine().trim());
                flag = true;
            } catch (NumberFormatException | IOException e) {
                flag = false;
                System.out.println("Невалидный ввод данных, повторите попытку.");
            }
        }

        return y;
    }

    private Long checkOnLong() throws IOException {
        long y = 0;
        boolean flag = false;
        while (!flag)
            try {
                y = Long.parseLong(reader.readLine().trim());
                flag = true;
            } catch (NumberFormatException e) {
                flag = false;
            } catch (IOException e) {
                System.out.println("Невалидный ввод данных, повторите попытку.");
            }
        return y;
    }

    /**
     * Метод проверяет, является ли число типом {@link Integer}
     *
     * @return
     */
    private int checkOnInt() throws IOException {
        int y = 0;
        boolean flag = false;
        while (!flag)
            try {
                y = Integer.parseInt(reader.readLine().trim());
                flag = true;
            } catch (NumberFormatException | IOException e) {
                flag = false;
                System.out.println("Невалидный ввод данных, повторите попытку.");
            }
        return y;
    }

    /**
     * Метод проверяет, является ли число типом {@link Enum}
     *
     * @return
     */
    private String checkOnEnum(Class className) throws IOException {
        boolean flag = false;
        String enumValue = null;
        while (!flag) {
            try {
                enumValue = reader.readLine().toUpperCase().trim();
                Enum.valueOf(className, enumValue);
                flag = true;
            } catch (IllegalArgumentException | IOException e) {
                flag = false;
                System.out.println("Невалидный ввод данных, повторите попытку.");
            }
        }

        return enumValue;
    }

    /**
     * Метод проверяет, является ли число типом {@link Float}
     *
     * @return
     */
    private float checkOnFloat() throws IOException {
        float y = 0;
        boolean flag = false;
        while (!flag)
            try {
                String cmd = reader.readLine().trim();
                if (cmd != null) {
                    y = Float.parseFloat(cmd);
                    flag = true;
                }
            } catch (NumberFormatException | IOException e) {
                flag = false;
                System.out.println("Невалидный ввод данных, повторите попытку.");
            }
        return y;
    }

}
