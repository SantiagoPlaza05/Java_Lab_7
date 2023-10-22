package Server;

import Entidades.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

public class CsvReader {
    /**
     * reads the file
     *
     * @param fileName file path
     * @return collection
     */
    public Hashtable<Long, Movie> read(String fileName) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        String line;
        Hashtable<Long, Movie> movies = new Hashtable<>();
        List<String> list;
        Movie movie;
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        synchronized (movies) {
            try {
                while ((line = reader.readLine()) != null) {
                    list = Arrays.asList(line.split(","));
                    movie = new Movie(Integer.parseInt(list.get(0)), Long.parseLong(list.get(1)), String.format(list.get(2)),
                            new Coordinates(
                                    Double.parseDouble(list.get(3)),
                                    Float.parseFloat(list.get(4))
                            ),
                            formatter.parse(list.get(5)),
                            Long.parseLong(list.get(6)),
                            Integer.parseInt(list.get(7)),
                            Integer.parseInt(list.get(8)),
                            getMpaaRating(list.get(9)),
                            new Person(
                                    list.get(10),
                                    Long.parseLong(list.get(11)),
                                    list.get(12),
                                    getEye(list.get(13)),
                                    new Location(
                                            Long.parseLong(list.get(14)),
                                            Float.parseFloat(list.get(15)),
                                            Float.parseFloat(list.get(16)),
                                            list.get(17)
                                    )
                            )
                    );
                    movies.put(Long.parseLong(list.get(0)), movie);
                }
                return movies;
            } catch (IOException | ParseException e) {
                e.printStackTrace();
                return null;
            }
        }

    }

    /**
     * get the rating
     *
     * @param rate string
     * @return rating
     */
    private static MpaaRating getMpaaRating(String rate) {
        List<MpaaRating> mpaaRatings = Arrays.asList(MpaaRating.values());
        MpaaRating rating = null;
        for (MpaaRating mpaaRating : mpaaRatings) {
            if (mpaaRating.name().equals(rate)) {
                rating = mpaaRating;
                break;
            }
        }
        return rating;
    }

    /**
     * get the  color
     *
     * @param eye string
     * @return color
     */
    private static Color getEye(String eye) {
        List<Color> colors = Arrays.asList(Color.values());
        Color colorEye = null;
        for (Color color1 : colors) {
            if (color1.name().equals(eye)) {
                colorEye = color1;
                break;
            }
        }
        return colorEye;
    }
}
