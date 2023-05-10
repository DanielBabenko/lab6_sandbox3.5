package command.commands.inputCommands;

import command.Command;
import manager.HelperController;

import java.io.IOException;
import java.text.ParseException;

public class AddNewElementCommand implements Command {
    private HelperController helperController;

    public AddNewElementCommand(HelperController helperController) {
        this.helperController = helperController;
    }

    @Override
    public void execute() throws ParseException {
        try {
            helperController.addElement();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
