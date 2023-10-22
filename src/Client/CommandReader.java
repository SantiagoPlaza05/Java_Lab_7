package Client;

import Shared.Command;
import Shared.DataBoxHandler;

import java.util.Objects;
import java.util.Scanner;

public class CommandReader implements Runnable {
    public void readConsoleCommand() {
        String currentCommand;
        CommandSender commandSender = new CommandSender();
        StringBuilder stringBuilder = new StringBuilder();
        System.out.print("\nEnter the command: ");
        while (continuar(stringBuilder)) {
            currentCommand = stringBuilder.toString();
            String[] parts = currentCommand.split(" ");
            stringBuilder.delete(0, currentCommand.length());
            Command command = new Command();
            StringBuilder comments = new StringBuilder();
            if (DataBoxHandler.getDataBox(parts, command, comments, true)) {
                String result =  commandSender.sendCommand(command);
                System.out.println(result);
            } else
                System.out.println(comments);
            System.out.print("\nEnter the new command: ");
        }
    }
    /**
     * Reads the commands and sends them to process
     * @param commando StringBuilder
     * @return returns true if  the command is different from exit.
     */
    static boolean continuar(StringBuilder commando) {
        Scanner keyboard = new Scanner(System.in);
        String currentCommand = keyboard.nextLine().toUpperCase();
        if (currentCommand.isEmpty()) {
            currentCommand = null;
        }
        if (currentCommand == null) {
            return true;
        }
        commando.append(currentCommand);
        return !Objects.equals(currentCommand, "EXIT");
        // si es  exit es !true osea false y se termina el loop, su no es exit es !false  por lo que es true

    }

    @Override
    public void run() {
        readConsoleCommand();
    }
}
