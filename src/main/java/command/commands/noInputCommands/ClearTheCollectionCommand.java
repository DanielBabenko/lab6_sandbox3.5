package command.commands.noInputCommands;

import command.Command;
import manager.HelperController;

public class ClearTheCollectionCommand implements Command {
    private HelperController helperController;

    public ClearTheCollectionCommand(HelperController helperController) {
        this.helperController = helperController;
    }

    public void execute()
    {
        helperController.clearCollection();
    }
}
