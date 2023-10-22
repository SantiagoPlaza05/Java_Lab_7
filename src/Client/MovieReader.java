package Client;

import Entidades.*;

import java.util.Date;
import java.util.Scanner;

public class MovieReader {
    public static Movie read(){
        Scanner sc = new Scanner(System.in);
        String message = "Introduce the name of the movie:";
        String name;
        do {
            name = readString(sc, message, true);
            if (name.isEmpty()) {
                System.out.println("The field cannot be empty.");
            }
        } while (name.isEmpty() == true);

        Coordinates coordinates = createCoordinates(sc);

        Date date;
        do {
            date = new Date();
            if (date == null) {
                System.out.println("The field cannot be null.");
            }
        } while (date == null);

        message = "Enter  the oscars count:";
        long oscarsCount;
        do {
            oscarsCount = readLong(sc, message, true);
            if (oscarsCount <= 0) {
                System.out.println("The value must be greater than 0");
            }
        } while (oscarsCount <= 0);


        message = "Enter the budget:";
        Integer budget;
        do {
            budget = readInt(sc, message, true);
            if (budget <= 0) {
                System.out.println("The value must be greater than 0");
            }
        } while (budget <= 0);

        message = "Enter the box office:";
        int totalBoxOffice;
        do {
            totalBoxOffice = readInt(sc, message, true);
            if (totalBoxOffice <= 0) {
                System.out.println("The value must be greater than 0");
            }
        } while (totalBoxOffice <= 0);
        MpaaRating rating = getMpaaRating(sc);
        Person operator = createPerson(sc);
        return new Movie(name, coordinates, date, oscarsCount, budget, totalBoxOffice, rating, operator);
    }
    private static MpaaRating getMpaaRating(Scanner sc) {
        System.out.println("Enter the Mpaa rating(  G, PG, PG_13, R, NC_17):");
        String rate = sc.next();
        MpaaRating[] mpaaRatings = MpaaRating.values();
        MpaaRating rating = null;
        for (MpaaRating mpaaRating : mpaaRatings) {
            if (mpaaRating.name().equals(rate)) {
                rating = mpaaRating;
                return rating;
            }
        }
        System.out.println("The value that you entered was not correct.");
        return getMpaaRating(sc);
    }

    /**
     * Creates the coordinates
     * @param sc Scanner
     * @return returns the new Coordinates
     */
    public static Coordinates createCoordinates(Scanner sc) {
        String message = "Introduce the coordinate x:";
        Double x = readDouble(sc, message, true);
        message = "Introduce the coordinate y:";
        float y = readFloat(sc, message, true);
        while (y <= -43) {
            System.out.println("You have entered a wrong value, you can only enter values greater than -43.");
            y = readFloat(sc, message, true);
        }
        return new Coordinates(x, y);
    }

    /**
     * Fills up a person object
     *
     * @param sc scanner
     * @return filled person
     */
    public static Person createPerson(Scanner sc) {
        String message = "Enter the person's name:";
        String name;
        do {
            name = readString(sc, message, true);
            if (name.isEmpty()) {
                System.out.println("The field cannot be empty.");
            }
        } while (name.isEmpty());

        message = "Enter the height of the person";
        Long height = readLong(sc, message, true);
        while (height <= 0) {
            System.out.println("You have entered a wrong value, you can only enter values greater than 0.");
            height = readLong(sc, message, true);
        }

        message = "Enter the person's passport id:";
        String passport;
        do {
            passport = readString(sc, message, true);
            if (passport.isEmpty()) {
                System.out.println("The field cannot be empty.");
            }
            if (passport.length() < 4) {
                System.out.println("The field length cannot be less than 4");
            }
        } while (passport.isEmpty() || passport.length() < 4);

        Color eye = createEye(sc);

        Location location = createLocation(sc);

        return new Person(name, height, passport, eye, location);
    }

    /**
     * Fills a location with its values
     *
     * @param sc scanner
     * @return filled location
     */
    private static Location createLocation(Scanner sc) {
        String message = "Introduce the Location coordinate x:";
        Long x = readLong(sc, message, true);
        message = "Introduce the Location coordinate y:";
        float y = readFloat(sc, message, true);
        message = "Introduce the Location coordinate z:";
        Float z = readFloat(sc, message, true);
        message = "Introduce the Location name:";
        String name = readString(sc, message, true);

        return new Location(x, y, z, name);
    }

    /**
     * Chooses a constant from EColor
     *
     * @param sc scanner
     * @return a constant from a color enum
     */
    private static Color createEye(Scanner sc) {
        System.out.println("Enter the eye color(RED, YELLOW, ORANGE, BROWN):");
        String color = sc.next();
        Color[] colors = Color.values();
        Color colorEye = null;
        for (Color color1 : colors) {
            if (color1.name().equals(color)) {
                colorEye = color1;
                return colorEye;
            }
        }
        System.out.println("The eye color is wrong.\n");
        return createEye(sc);
    }

    /**
     * Transforms from a string to an Integer
     *
     * @param text a string
     * @return Integer
     */
    public static Integer tryParseInt(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            System.out.println("The value you entered is not an int");
            return null;
        }
    }

    /**
     * Transforms from a string to a Long
     *
     * @param text a string
     * @return Long
     */
    public static Long tryParseLong(String text) {
        try {
            return Long.parseLong(text);
        } catch (NumberFormatException e) {
            System.out.println("The value you entered is not a long.");
            return null;
        }
    }

    /**
     * Transforms from a string to a Float
     *
     * @param text a string
     * @return Float
     */
    private static Float tryParseFloat(String text) {
        try {
            return Float.parseFloat(text);
        } catch (NumberFormatException e) {
            System.out.println("The value you entered is not a float.");
            return null;
        }
    }

    /**
     * Transforms from a string to a Double
     *
     * @param text a string
     * @return Double
     */
    public static Double tryParseDouble(String text) {
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            System.out.println("The value you entered is not a double");
            return null;
        }
    }

    /**
     * Reads a Double object
     *
     * @param sc         scanner
     * @param message    indica que informacion se lee
     * @param obligatory true indica que continue leyendo hasta que sea distinto de null, false indica una sola lectura
     * @return long
     */
    public static Long readLong(Scanner sc, String message, boolean obligatory) {
        System.out.println(message);
        boolean continueReading = obligatory;
        Long x = null;
        do {
            x = tryParseLong(sc.next());
            if (x == null) {
                System.out.println(message);
            } else {
                continueReading = false;
            }
        } while (continueReading);
        return x;
    }

    /**
     * Reads a Double object
     *
     * @param sc         scanner
     * @param message    indica que informacion se lee
     * @param obligatory true indica que continue leyendo hasta que sea distinto de null, false indica una sola lectura
     * @return double
     */
    public static Double readDouble(Scanner sc, String message, boolean obligatory) {
        System.out.println(message);
        boolean continueReading = obligatory;
        Double x = null;
        do {
            x = tryParseDouble(sc.next());
            if (x == null) {
                System.out.println(message);
            } else {
                continueReading = false;
            }
        } while (continueReading);
        return x;
    }

    /**
     * Reads a String object
     *
     * @param sc         scanner
     * @param message    indica que informacion se lee
     * @param obligatory true indica que continue leyendo hasta que sea distinto de null, false indica una sola lectura
     * @return string
     */
    public static String readString(Scanner sc, String message, boolean obligatory) {
        System.out.println(message);
        boolean continueReading = obligatory;
        String x = null;
        do {
            x = sc.next();
            if (x == null) {
                System.out.println("The entered value cannot be null,\n" + message);
            } else {
                continueReading = false;
            }
        } while (continueReading);
        return x;
    }

    /**
     * Reads an Integer object
     *
     * @param sc         scanner
     * @param message    indica que informacion se lee
     * @param obligatory true indica que continue leyendo hasta que sea distinto de null, false indica una sola lectura
     * @return int
     */
    public static Integer readInt(Scanner sc, String message, boolean obligatory) {
        System.out.println(message);
        boolean continueReading = obligatory;
        Integer x = null;
        do {
            x = tryParseInt(sc.next());
            if (x == null) {
                System.out.println(message);
            } else {
                continueReading = false;
            }
        } while (continueReading);
        return x;
    }

    /**
     * Reads a Float object
     *
     * @param sc         scanner
     * @param message    indica que informacion se lee
     * @param obligatory true indica que continue leyendo hasta que sea distinto de null, false indica una sola lectura
     * @return float
     */
    private static Float readFloat(Scanner sc, String message, boolean obligatory) {
        System.out.println(message);
        boolean continueReading = obligatory;
        Float x = null;
        do {
            x = tryParseFloat(sc.next());
            if (x == null) {
                System.out.println(message);
            } else {
                continueReading = false;
            }
        } while (continueReading);
        return x;
    }
}
