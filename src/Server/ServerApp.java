package Server;

import Entidades.Movie;
import Entidades.User;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Scanner;

public class ServerApp {
    public static Hashtable<Long, Movie> movieHashtable;
    private static String fileName;
    private static String initialization;
    private static Boolean exit = false;

    private static User currentUser;

    public static Boolean getExit() {
        return exit;
    }

    public static void setExit(Boolean exit) {
        ServerApp.exit = exit;
        if (exit) System.exit(0);
    }

    public static String getFileName() {
        return fileName;
    }

    private static void setFileName(String fileName) {
        ServerApp.fileName = fileName;
    }

    public static String getInitialization() {
        return initialization;
    }

    public static void setInitialization(String initialization) {
        ServerApp.initialization = initialization;
    }

    public static void main(String[] args) {
        int port;
        CsvReader reader = new CsvReader();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        File f;
        if (args.length != 2) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the port: ");
            port = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter the file name: ");
            setFileName(scanner.nextLine());
        } else {
            port = Integer.parseInt(args[0]);
            setFileName(args[1]);
        }
        movieHashtable=reader.read(getFileName());
        f =new File(getFileName());
        initialization = sdf.format(f.lastModified());
        if (!createIfNotExists(getFileName())) {
            System.out.println("The file is invalid.");
            return;
        }
        try {
            KeyboardHandler keyboardHandler = new KeyboardHandler();
            Thread t1 = new Thread(keyboardHandler);
            t1.start();
            CommunicationServer communication = new CommunicationServer(port);
            Thread t2 = new Thread(communication);
            t2.start();
        } catch (SocketException e) {
            e.printStackTrace();
            System.out.println("There was a socket exception. " + e.getMessage());
        }
    }

    private static boolean createIfNotExists(String filename) {
        File file = new File(filename);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        if (file.exists())
            return true;
        try {
            boolean created=file.createNewFile();
            movieHashtable=new Hashtable<>();
            setInitialization(sdf.format(file.lastModified())) ;
            return created;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

}
