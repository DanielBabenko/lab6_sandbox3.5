package command;

public interface UpdateCommand {

    /**
     * Метод для работы с командами с параметром {@link object.LabWork#id}
     * @param id
     */
    void execute(int id);
}
