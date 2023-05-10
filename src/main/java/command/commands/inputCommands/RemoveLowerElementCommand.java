package command.commands.inputCommands;


import command.Command;
import command.ElementCommand;
import manager.HelperController;

import java.io.IOException;

public class RemoveLowerElementCommand implements Command {
    private HelperController helperController;

    public RemoveLowerElementCommand(HelperController helperController) {
        this.helperController = helperController;
    }

    @Override
    public void execute() {
        try {
            helperController.removeLower();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
