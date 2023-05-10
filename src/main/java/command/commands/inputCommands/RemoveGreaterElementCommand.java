package command.commands.inputCommands;


import command.Command;
import command.ElementCommand;
import manager.HelperController;

import java.io.IOException;
import java.text.ParseException;

public class RemoveGreaterElementCommand implements Command {
    private HelperController helperController;

    public RemoveGreaterElementCommand(HelperController helperController) {
        this.helperController = helperController;
    }

    @Override
    public void execute()throws ParseException {
        try {
            helperController.removeGreater();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

