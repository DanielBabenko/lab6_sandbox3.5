package command.commands.inputCommands;


import command.Command;
import command.ElementCommand;
import command.commands.Invoker;
import object.LabWork;

import java.io.IOException;
import java.text.ParseException;

public class AddIfMax extends Invoker {
    private Command addMax;
    private static final String COMMAND_NAME = AddIfMax.class.getSimpleName();

    public static String getCommandName() {
        return COMMAND_NAME;
    }

    public AddIfMax(Command addMax){
        this.addMax = addMax;
    }


    @Override
    public void doCommand(String e) throws IOException, ParseException {
        addMax.execute();
    }
}
