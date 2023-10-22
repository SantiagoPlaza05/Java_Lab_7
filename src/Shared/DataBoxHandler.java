package Shared;

import Client.FileReader;
import Client.MovieReader;

import Entidades.Movie;

public class DataBoxHandler {
    public static boolean getDataBox(String[] parts, Command command, StringBuilder comments, boolean isInteractive) {
        DataBox dataBox;
        switch (parts[0]) {
            case "HELP" -> {
                command.setCommandType(CommandType.HELP);
                return true;
            }
            case "INFO" -> {
                command.setCommandType(CommandType.INFO);
                return true;
            }
            case "SHOW" -> {
                command.setCommandType(CommandType.SHOW);
                return true;
            }
            case "CLEAR" -> {
                command.setCommandType(CommandType.CLEAR);
                return true;
            }
            case "REMOVE_KEY" -> {
                command.setCommandType(CommandType.REMOVE_KEY);
                dataBox = readDataCommandRemoveKey(parts, comments);
                command.setDataCommand(dataBox);
                return dataBox != null;
            }
            case "REPLACE_IF_GREATER" -> {
                command.setCommandType(CommandType.REPLACE_IF_GREATER);
                dataBox = readDataCommandReplace(parts, comments);
                command.setDataCommand(dataBox);
                return dataBox != null;
            }
            case "REPLACE_IF_LOWER" -> {
                command.setCommandType(CommandType.REPLACE_IF_LOWER);
                dataBox = readDataCommandReplace(parts, comments);
                command.setDataCommand(dataBox);
                return dataBox != null;
            }
            case "REMOVE_GREATER_KEY" -> {
                command.setCommandType(CommandType.REMOVE_GREATER_KEY);
                dataBox = readDataCommandRemoveKey(parts, comments);
                command.setDataCommand(dataBox);
                return dataBox != null;
            }
            case "INSERT" -> {
                if (!isInteractive)
                    return false;
                command.setCommandType(CommandType.INSERT);
                dataBox = readDataCommandInsert(parts,comments);
                command.setDataCommand(dataBox);
                return dataBox != null;
            }
            case "UPDATE" -> {
                if (!isInteractive)
                    return false;
                command.setCommandType(CommandType.UPDATE);
                dataBox = readDataCommandUpdate(parts, comments);
                command.setDataCommand(dataBox);
                return dataBox != null;
            }
            case "EXECUTE_SCRIPT" -> {
                if (!isInteractive)
                    return false;
                command.setCommandType(CommandType.EXECUTE_SCRIPT);
                dataBox = readDataCommandExecuteScript(parts, comments);
                command.setDataCommand(dataBox);
                return dataBox != null;
            }
            case "COUNT_LESS_THAN_OSCARS_COUNT" -> {
                if (!isInteractive)
                    return false;
                command.setCommandType(CommandType.COUNT_LESS_THAN_OSCARS_COUNT);
                dataBox = readDataCommandCountLessThanOscars(parts, comments);
                command.setDataCommand(dataBox);
                return dataBox != null;
            }
            case "PRINT_ASCENDING" -> {
                command.setCommandType(CommandType.PRINT_ASCENDING);
                return true;
            }
            case "PRINT_FIELD_DESCENDING_OSCARS_COUNT" -> {
                command.setCommandType(CommandType.PRINT_FIELD_DESCENDING_OSCARS_COUNT);
                return true;
            }
            default -> {
                comments.append("unknown command");
                return false;
            }
        }
    }

    private static DataBox readDataCommandInsert(String[] parts, StringBuilder comments) {
        if (parts.length < 2) {
            comments.append("The command is incomplete, you need to enter the key.");
            return null;
        }
        try {
            Movie movieToUpdate = readMovie();
            DataBox dataBox = new DataBox();
            dataBox.setMovie(movieToUpdate);
            dataBox.setKey( Long.parseLong(parts[1]));
            return dataBox;
        } catch (NumberFormatException e) {
            comments.append("The command is invalid.");
            return null;
        }
    }

    private static Movie readMovie() {
        MovieReader movieReader = new MovieReader();
        return movieReader.read();
    }

    /**
     * updates a movie by its id, necesito saber como comprobar que este tiene ese id sin escribir todo el objeto
     *
     * @param parts
     * @param comments
     * @return
     */
    private static DataBox readDataCommandUpdate(String[] parts, StringBuilder comments) {
        if (parts.length < 2) {
            comments.append("The command is incomplete, you need to enter the id.");
            return null;
        }
        try {
            long id = Long.parseLong(parts[1]);
            Movie movieToUpdate = readMovie();
            movieToUpdate.setId(id);
            DataBox dataBox = new DataBox();
            dataBox.setMovie(movieToUpdate);
            return dataBox;
        } catch (NumberFormatException e) {
            comments.append("The command is invalid.");
            return null;
        }
    }

    private static DataBox readDataCommandRemoveKey(String[] parts, StringBuilder comments) {
        if (parts.length < 2) {
            comments.append("The command is incomplete, you need to enter the key .");
            return null;
        }
        DataBox dataBox = new DataBox();
        try {
            dataBox.setKey(Long.parseLong(parts[1]));
        } catch (NumberFormatException e) {
            comments.append("The command is invalid.");
            return null;
        }
        return dataBox;
    }

    private static DataBox readDataCommandReplace(String[] parts, StringBuilder comments) {
        if (parts.length < 3) {
            comments.append("The command is incomplete, you need to enter the key and the oscarsCount.");
            return null;
        }

        DataBox dataBox = new DataBox();
        try {
            dataBox.setKey(Long.parseLong(parts[1]));
        } catch (NumberFormatException e) {
            comments.append("The command is invalid.");
            return null;
        }

        try {
            dataBox.setOscarsCount(Long.parseLong(parts[2]));
        } catch (NumberFormatException e) {
            comments.append("The command is invalid.");
            return null;
        }
        return dataBox;
    }

    private static DataBox readDataCommandExecuteScript(String[] parts, StringBuilder comments) {
        if (parts.length < 2) {
            comments.append("The command is incomplete, you need to enter the filename that contain the commands.");
            return null;
        }
        FileReader fileReader = new FileReader();
        String dataFile = fileReader.read(parts[1]);
        if (dataFile == null) {
            comments.append("Can't read the file.");
            return null;
        }
        DataBox dataBox = new DataBox();
        dataBox.setDataFile(dataFile);
        return dataBox;
    }

    private static DataBox readDataCommandCountLessThanOscars(String[] parts, StringBuilder comments) {
        if (parts.length < 2) {
            comments.append("The command is incomplete, you need to enter the oscarsCount.");
            return null;
        }
        DataBox dataBox = new DataBox();
        try {
            dataBox.setOscarsCount(Long.parseLong(parts[1]));
        } catch (NumberFormatException e) {
            comments.append("The command is invalid.");
            return null;
        }
        return dataBox;
    }


}

