package Server;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class KeyboardHandler implements Runnable{
    public void run() {
        String data;
        Scanner scanner = new Scanner(System.in);
        System.out.println("READFILE: to read movies from file\nSAVE: to save file\nSAVEDB: to save to database\nADMIN:createAdmin\nEXIT: to finish execution");
        ProcessHandler processHandler = new ProcessHandler();
        while (!(data = scanner.nextLine().toUpperCase()).equals("EXIT")) {
            switch (data) {
                case "SAVE" -> {
                    System.out.println("Saving file...");
                    System.out.println(processHandler.saveToFile());
                }
                case "SAVEDB" -> {
                    System.out.println("Saving to database...");
                    System.out.println(processHandler.save());
                }
                case "READFILE" -> {
                    ServerApp.movieHashtable = new CsvReader().read(ServerApp.getFileName());
                    System.out.println("Read from file...");
                }
                case "ADMIN" -> {
                    try {
                        processHandler.CreateAdmin();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println("Ready ADMIN\n");
                }
            }
            System.out.println("READFILE: to read movies from file\nSAVE: to save file\nSAVEDB: to save to database\nADMIN:createAdmin\nEXIT: to finish execution");
        }
        ServerApp.setExit(true);
    }
}