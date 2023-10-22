package Client;

import Shared.Command;
import Shared.SerializationHandler;

import java.io.IOException;

public class CommandSender  {
    public String sendCommand(Command command) {
        command.setUser(ClientApp.getUser());
        byte[] data = SerializationHandler.serialize(command);
        if (data == null)
            return "There was an error while serialization.";
        try {
            System.out.println("message size: " + data.length + "\n");
            if (ClientApp.getCommunication().send(data))
                return ClientApp.getCommunication().receive();
            return "The command was not sent";
        } catch (IOException e) {
            e.printStackTrace();
            return "There was an error while sending data.";
        }
    }

}
