package command.commands.inputCommands;

import command.Command;
import command.ElementCommand;
import manager.HelperController;

import java.io.IOException;
import java.text.ParseException;

public class AddIfMaxCommand implements Command {
    private HelperController helperController;

    public AddIfMaxCommand(HelperController helperController) {
        this.helperController = helperController;

    }

    @Override
    public void execute() {
        try {
            helperController.addIfMax();
        } catch (IOException | ParseException ex) {
            throw new RuntimeException(ex);
        }
    }
}
