package Client;

import Entidades.User;
import Server.KeyboardHandler;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientApp {

    private static User user= new User();

    public static User getUser() {
        return user;
    }

    private static CommunicationClient communication;
    public static CommunicationClient getCommunication() {
        return communication;
    }
    private static void setCommunication(CommunicationClient communication) {
        ClientApp.communication = communication;
    }

    public static void main(String[] args) {
        String serverAddress;
        int port;
        Scanner scanner = new Scanner(System.in);
        if (args.length != 2) {

            System.out.print("Enter the server address: ");
            serverAddress = scanner.nextLine();
            System.out.print("Enter the port: ");
            port = Integer.parseInt(scanner.nextLine());
        } else {
            serverAddress = args[0];
            port = Integer.parseInt(args[1]);
        }
        System.out.print("Enter your name: ");
        user.setName(scanner.nextLine());
        System.out.print("Enter your password: ");
        user.setPassword(scanner.nextLine());

        if (initializeCommunication(serverAddress, port)){
            CommandReader commandReader = new CommandReader();
            Thread t1 = new Thread(commandReader);
            t1.start();
        }
    }

    private static Boolean initializeCommunication(String serverAddress, Integer port) {
        Scanner scanner = new Scanner(System.in);
        do {
            try {
                setCommunication(new CommunicationClient(port, serverAddress));
                return true;
            } catch (SocketException e) {
                e.printStackTrace();
                System.out.println("There was an exception while connecting to the server." + e.getMessage());
            } catch (UnknownHostException e) {
                e.printStackTrace();
                System.out.println("There was an unknown exception. " + e.getMessage());
            } catch (Exception e) {
                System.out.println("There was an generic exception. " + e.getMessage());
            }
            System.out.println("Would you like to try again (yes/no)?");
        } while (scanner.nextLine().equalsIgnoreCase("YES"));
        return false;
    }
}
