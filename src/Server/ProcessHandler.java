package Server;

import Entidades.Movie;
import Entidades.User;
import Shared.Command;
import Shared.DataBoxHandler;

import java.io.*;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class ProcessHandler {
    public String processCommand(Command command) {
        if (!isValidUser(command))
            return "Invalid user";
        ServerApp.setCurrentUser(command.getUser());
        ServerApp.getCurrentUser().setUserId(getIdUserDb(command));
        return switch (command.getCommandType()) {
            case HELP -> help();
            case INFO -> info();
            case SHOW -> show();
            case CLEAR -> clear();
            case COUNT_LESS_THAN_OSCARS_COUNT -> countLessThanOscarsCount(command);
            case REMOVE_KEY -> remove(command);
            case EXECUTE_SCRIPT -> executeScript(command);
            case REPLACE_IF_GREATER -> replaceIfGreater(command);
            case REPLACE_IF_LOWER -> replaceIfLower(command);
            case REMOVE_GREATER_KEY -> remove_greater_key(command);
            case PRINT_ASCENDING -> printAscending();
            case PRINT_FIELD_DESCENDING_OSCARS_COUNT -> printFieldDescendingOscarsCount();
            case INSERT -> insert(command);
            case UPDATE -> update(command);
        };
    }

    public void CreateAdmin() throws SQLException {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        databaseConnection.CreateAdmin();
    }

    public boolean isValidUser(Command command) {
        User user = command.getUser();
        if (user == null)
            return false;
        DatabaseConnection databaseConnection = new DatabaseConnection();
        return databaseConnection.IsValidUser(user);
    }

    public Integer getIdUserDb(Command command) {
        User user = command.getUser();
        DatabaseConnection databaseConnection = new DatabaseConnection();
        return databaseConnection.IdUser(user);
    }

    private String executeScript(Command command) {
        StringBuilder dataFile = new StringBuilder();
        User user = command.getUser();
        dataFile.append(command.getDataCommand().getDataFile());
        if (dataFile.length() <= 0) {
            return "The file was empty.";
        }

        String line;
        StringBuilder output = new StringBuilder();

        while (!(line = getLine(dataFile)).isEmpty()) {
            output.append("\n").append(line).append("\n");
            output.append(executeCommand(line, user));
        }
        return output.toString();
    }

    private String executeCommand(String currentCommand, User user) {
        String[] parts = currentCommand.toUpperCase().split(" ");
        Command command = new Command();
        command.setUser(user);
        StringBuilder comments = new StringBuilder();
        if (DataBoxHandler.getDataBox(parts, command, comments, false))
            return this.processCommand(command);
        else
            return comments.toString();
    }

    private String getLine(StringBuilder dataFile) {
        String line;
        if (dataFile.indexOf("\n") > 0) {
            line = dataFile.substring(0, dataFile.indexOf("\n"));
            dataFile.delete(0, dataFile.indexOf("\n") + 1);
        } else {
            line = dataFile.toString();
            dataFile.setLength(0);
        }
        return line;
    }

    public String save() {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        synchronized (ServerApp.movieHashtable) {
            ServerApp.movieHashtable = databaseConnection.saveMovies(ServerApp.movieHashtable, ServerApp.getCurrentUser().getUserId());
            return "The changes was saved in the database";
        }
    }

    public String saveToFile() {
        synchronized (ServerApp.movieHashtable) {
            Iterator<Map.Entry<Long, Movie>> it = ServerApp.movieHashtable.entrySet().iterator();
            String movieString = "";
            while (it.hasNext()) {
                Map.Entry<Long, Movie> currentMovie = it.next();
                movieString += currentMovie.getKey() + "," + currentMovie.getValue().toCsv() + "\n";
            }


            BufferedOutputStream writer;
            try {
                writer = new BufferedOutputStream(new FileOutputStream(ServerApp.getFileName()));
            } catch (IOException e) {
                e.printStackTrace();
                return "There was a problem while saving";
            }
            byte[] b = movieString.getBytes();
            try {
                writer.write(b);
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
                return "There was a problem while saving";
            }
            return "The changes were saved";
        }
    }

    private String help() {
        return """
                help : вывести справку по доступным командам
                info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)
                show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении
                insert null {element} : добавить новый элемент с заданным ключом
                update id {element} : обновить значение элемента коллекции, id которого равен заданному
                remove_key null : удалить элемент из коллекции по его ключу
                clear : очистить коллекцию
                execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.
                exit : завершить программу (без сохранения в файл)
                replace_if_greater null {element} : заменить значение по ключу, если новое значение больше старого
                replace_if_lowe null {element} : заменить значение по ключу, если новое значение меньше старого
                remove_greater_key null : удалить из коллекции все элементы, ключ которых превышает заданный
                count_less_than_oscars_count oscarsCount : вывести количество элементов, значение поля oscarsCount которых меньше заданного
                print_ascending : вывести элементы коллекции в порядке возрастания
                print_field_descending_oscars_count : вывести значения поля oscarsCount всех элементов в порядке убывания""";
    }

    private String info() {
        synchronized (ServerApp.movieHashtable) {
            return "type: Hashtable<Long, Movie>\n" +
                    "initialization: " + ServerApp.getInitialization() + "\n" +
                    "length: " + ServerApp.movieHashtable.size();
        }
    }

    private String show() {
        synchronized (ServerApp.movieHashtable) {
            if (ServerApp.movieHashtable.isEmpty()) {
                return "There are no movies";
            }
            StringBuilder dataMovie = new StringBuilder();

            ServerApp.movieHashtable.forEach((k, v) -> dataMovie.append("\n").append(v.toString()));
            return dataMovie.toString();
        }
    }

    private String clear() {
        synchronized (ServerApp.movieHashtable) {
            ServerApp.movieHashtable = new Hashtable<>();
            return "the collection was cleared";
        }
    }

    private String countLessThanOscarsCount(Command command) {
        synchronized (ServerApp.movieHashtable) {
            AtomicReference<Integer> counter = new AtomicReference<>(0);
            ServerApp.movieHashtable.forEach((k, v) -> {
                if (v.getOscarsCount() < command.getDataCommand().getOscarsCount())
                    counter.getAndSet(counter.get() + 1);
            });
            return "There are " + counter.get() + " movies with less than " + command.getDataCommand().getOscarsCount() + " oscars.";
        }
    }

    private String remove(Command command) {
        synchronized (ServerApp.movieHashtable) {
            if (ServerApp.movieHashtable.containsKey(command.getDataCommand().getKey())) {
                ServerApp.movieHashtable.remove(command.getDataCommand().getKey());
                return "The movie was removed.";
            } else
                return "The movie with the given key doesn't exist.";
        }
    }

    private String replaceIfGreater(Command command) {
        synchronized (ServerApp.movieHashtable) {
            Long key = command.getDataCommand().getKey();
            long oscarsCount = command.getDataCommand().getOscarsCount();
            if (ServerApp.movieHashtable.containsKey(key))
                if (ServerApp.movieHashtable.get(key).getOscarsCount() < oscarsCount) {
                    ServerApp.movieHashtable.get(key).setOscarsCount(oscarsCount);
                    return "The oscarsCount was replaced.";
                }
            return "Nothing to replace.";
        }
    }

    private String replaceIfLower(Command command) {
        synchronized (ServerApp.movieHashtable) {
            Long key = command.getDataCommand().getKey();
            long oscarsCount = command.getDataCommand().getOscarsCount();
            if (ServerApp.movieHashtable.containsKey(key))
                if (ServerApp.movieHashtable.get(key).getOscarsCount() > oscarsCount) {
                    ServerApp.movieHashtable.get(key).setOscarsCount(oscarsCount);
                    return "The oscarsCount was replaced.";
                }
            return "Nothing to replace.";
        }
    }

    private String remove_greater_key(Command command) {
        synchronized (ServerApp.movieHashtable) {
            Iterator<Map.Entry<Long, Movie>> it = ServerApp.movieHashtable.entrySet().iterator();
            int deleted = 0;
            while (it.hasNext()) {
                Map.Entry<Long, Movie> entry = it.next();
                if (entry.getKey() > command.getDataCommand().getKey()) {
                    it.remove();
                    deleted++;
                }
            }
            return "Deleted elements: " + deleted + ".";
        }
    }

    private String printAscending() {
        synchronized (ServerApp.movieHashtable) {
            Iterator<Map.Entry<Long, Movie>> it = ServerApp.movieHashtable.entrySet().iterator();
            ArrayList<Movie> list = new ArrayList<>();
            while (it.hasNext()) {
                Map.Entry<Long, Movie> currentMovie = it.next();
                list.add(currentMovie.getValue());
            }
            Collections.sort(list);
            return "Sorted collection: " + list;
        }
    }

    private String printFieldDescendingOscarsCount() {
        synchronized (ServerApp.movieHashtable) {
            Iterator<Map.Entry<Long, Movie>> it = ServerApp.movieHashtable.entrySet().iterator();
            ArrayList<Movie> list = new ArrayList<>();
            while (it.hasNext()) {
                Map.Entry<Long, Movie> currentMovie = it.next();
                list.add(currentMovie.getValue());
            }
            list.sort(compareByOscars);
            return "Sorted collection: " + list;
        }
    }

    Comparator<Movie> compareByOscars = (o1, o2) -> (int) (o1.getOscarsCount() - o2.getOscarsCount());

    private String insert(Command command) {
        synchronized (ServerApp.movieHashtable) {
            Movie movie = command.getDataCommand().getMovie();
            movie.setId(getNewId());
            movie.setUserId(ServerApp.getCurrentUser().getUserId());
            ServerApp.movieHashtable.put(command.getDataCommand().getKey(), movie);
            return "The movie was inserted.";
        }
    }

    private String update(Command command) {
        synchronized (ServerApp.movieHashtable) {
            for (Map.Entry<Long, Movie> entry : ServerApp.movieHashtable.entrySet()) {
                Movie movieA = ServerApp.movieHashtable.get(entry.getKey());
                if (movieA.getId() == command.getDataCommand().getMovie().getId()) {
                    ServerApp.movieHashtable.replace(entry.getKey(), command.getDataCommand().getMovie());
                    return "The movie was updated.";
                }
            }
        }
        return "The movie with the id doesn't exist.";
    }

    private static Long getNewId() {
        synchronized (ServerApp.movieHashtable) {
            Long maxKey = Long.valueOf(0);
            for (Map.Entry<Long, Movie> entry : ServerApp.movieHashtable.entrySet()) {
                if (entry.getKey() > maxKey) {
                    maxKey = entry.getKey();
                }
            }
            maxKey++;
            return maxKey;
        }
    }
}
