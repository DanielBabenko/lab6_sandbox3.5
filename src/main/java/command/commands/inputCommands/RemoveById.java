package command.commands.inputCommands;

import manager.HelperController;
import command.commands.Invoker;

import java.io.IOException;
import java.text.ParseException;

public class RemoveById extends Invoker {

    private HelperController helperController;

    private static final String COMMAND_NAME = RemoveById.class.getSimpleName();

    public RemoveById(HelperController helperController) {
        this.helperController = helperController;
    }


    public void execute(int id) {
        helperController.removeEl(id);
    }

    public  String getCommandName() {
        return COMMAND_NAME;
    }

    @Override
    public void doCommand(String e) {
        try {
            int i = Integer.parseInt(e.replaceAll("^\\D*?(-?\\d+).*$", "$1"));
            execute(i);
        } catch (NumberFormatException ex) {
            try {
                helperController.getServer().sentToClient("Невалидный ввод данных. Повторите попытку.");
            } catch (IOException exc) {
                throw new RuntimeException(exc);
            }
        }
    }
}
