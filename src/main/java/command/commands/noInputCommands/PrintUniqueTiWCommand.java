package command.commands.noInputCommands;


import command.Command;
import manager.HelperController;

public class PrintUniqueTiWCommand implements Command {
    private HelperController helperController;

    public PrintUniqueTiWCommand(HelperController helperController) {
        this.helperController = helperController;
    }
    @Override
    public void execute() {
        helperController.printUniqueTunedInWorks();
    }
}
