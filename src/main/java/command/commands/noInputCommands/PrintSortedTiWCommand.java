package command.commands.noInputCommands;

import command.Command;
import manager.HelperController;

public class PrintSortedTiWCommand implements Command {
    private HelperController helperController;

    public PrintSortedTiWCommand(HelperController helperController) {
        this.helperController = helperController;
    }

    @Override
    public void execute()
    {
        helperController.printFieldAscendingTunedInWorks();
    }
}
