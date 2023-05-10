//package command.commands.noInputCommands;
//
//import manager.HelperController;
//import command.commands.Invoker;
//import manager.Server;
//
//import java.io.IOException;
//
//public class Save extends Invoker {
//    private Server server;
//
//    private static final String COMMAND_NAME = Save.class.getSimpleName();
//
//    public Save(Server server) {
//        this.server = server;
//    }
//
//    public String getCommandName() {
//        return COMMAND_NAME;
//    }
//
//    @Override
//    public void doCommand(String e) {
//        try {
//            server.save();
//        } catch (IOException ex) {
//            throw new RuntimeException(ex);
//        }
//    }
//}
