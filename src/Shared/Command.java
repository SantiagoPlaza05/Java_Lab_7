package Shared;

import Entidades.User;

import java.io.Serializable;

public class Command implements Serializable {
    private User user;
    private DataBox dataCommand;
    private CommandType commandType;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public Command() {

    }

    public DataBox getDataCommand() {
        return dataCommand;
    }

    public void setDataCommand(DataBox dataCommand) {
        this.dataCommand = dataCommand;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public void setCommandType(CommandType commandType) {
        this.commandType = commandType;
    }
}