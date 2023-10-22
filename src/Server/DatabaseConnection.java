package Server;

import Entidades.*;


import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class DatabaseConnection {
    String user = "postgres";
    String password = "1234";
    String url = "jdbc:postgresql://localhost:5432/MovieDataBase";


    public Hashtable<Long, Movie> getMovies() {
        Hashtable<Long, Movie> movieHashtable = new Hashtable<>();
        synchronized (movieHashtable) {
            Connection conn = null;
            SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);

            try {
                conn = DriverManager.getConnection(url, user, password);
                String query = """
                        Select\s
                        movies.user_id,
                        movies.id,
                        movies.name,
                        movies.creation_date,
                        movies.oscars_count,
                        movies.total_box_office,
                        movies.budget,
                        movies.mpaa_rating,
                        coordinates.x,
                        coordinates.y,
                        persons.name person_name,
                        persons.height person_height,
                        persons.passport_id person_passport,
                        persons.eye_color person_eye,
                        locations.x location_x,
                        locations.y location_y,
                        locations.z location_z,
                        locations.name location_name
                        from movies\s
                        left join coordinates on movies.id = coordinates.movie_id\s
                        left join persons on movies.id = persons.movie_id\s
                        left join locations on persons.movie_id = locations.person_id""";
                try (Statement stmt = conn.createStatement()) {
                    ResultSet rs = stmt.executeQuery(query);

                    while (rs.next()) {
                        Integer userId = rs.getInt("user_id");
                        long id = rs.getLong("id");
                        String name = rs.getString("name");
                        String creationDate = rs.getString("creation_date");
                        long oscarsCount = rs.getLong("oscars_count");
                        Integer budget = rs.getInt("budget");
                        int totalBoxOffice = rs.getInt("total_box_office");
                        String mpaaRating = rs.getString("mpaa_rating");
                        Double x = rs.getDouble("x");
                        float y = rs.getFloat("y");
                        String personName = rs.getString("person_name");
                        Long height = rs.getLong("person_height");
                        String passportID = rs.getString("person_passport");
                        String eyeColor = rs.getString("person_eye");
                        Long xLocation = rs.getLong("location_x");
                        float yLocation = rs.getFloat("location_y");
                        Float zLocation = rs.getFloat("location_z");
                        String nameLocation = rs.getString("location_name");
                        Date date = formatter.parse(creationDate);

                        Movie movie = new Movie();
                        movie.setUserId(userId);
                        movie.setId(id);
                        movie.setName(name);
                        movie.setCreationDate(date);
                        movie.setOscarsCount(oscarsCount);
                        movie.setBudget(budget);
                        movie.setTotalBoxOffice(totalBoxOffice);
                        movie.setMpaaRating(MpaaRating.valueOf(mpaaRating));
                        movie.setCoordinates(new Coordinates(x, y));
                        movie.setOperator(new Person(personName, height, passportID, Color.valueOf(eyeColor), new Location(xLocation, yLocation, zLocation, nameLocation)));
                        movieHashtable.put(id, movie);
                    }
                    return movieHashtable;
                } catch (SQLException e) {
                    throw new Error("Problem", e);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            } catch (SQLException e) {
                throw new Error("problem", e);
            } finally {
                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }

    public Integer IdUser(User userToValidate) {
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url, user, password);
            Statement stmt;
            String query = "Select user_id from public.users where name ='"
                    + userToValidate.getName() + "'";
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            rs.next();
            return rs.getInt("user_id");
        } catch (SQLException e) {
            throw new Error("Problem", e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public boolean IsValidUser(User userToValidate) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            Statement stmt;
            String query = "Select password from public.users where name ='"
                    + userToValidate.getName() + "'";
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if (!rs.next())
                return false;
            EncriptionHelper encriptionHelper = new EncriptionHelper();
            byte[] givenPassword = encriptionHelper.encrypt(userToValidate.getPassword());
            byte[] dataToValidate = rs.getBytes("password");
            return Arrays.equals(dataToValidate, givenPassword);
        } catch (SQLException e) {
            throw new Error("Problem", e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public void CreateAdmin() throws SQLException {
        System.out.println("Write the name of the User:");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();

        System.out.println("Write a password for the User:");
        String user_password = scanner.nextLine();
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);

            byte[] encryptedData = new EncriptionHelper().encrypt(user_password);
            String query = "Insert into public.users (name, password) values (?, ?);";

            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, name);
            pst.setBytes(2, encryptedData);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Problem", e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public Hashtable<Long, Movie> saveMovies(Hashtable<Long, Movie> movieHashtable, Integer userId) {
        synchronized (ServerApp.movieHashtable) {
            // I delete all dragons that doesn't belong to the  current user
            movieHashtable.entrySet().removeIf(entry -> !entry.getValue().getUserId().equals(userId));

            Connection conn = null;
            try {
                conn = DriverManager.getConnection(url, user, password);
                System.out.println("Connection to de db established");
                String query = "Delete from public.movies where user_id = " + userId;
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute(query);
                    Statement stmtInsert = conn.createStatement();
                    movieHashtable.forEach((k, v) -> {
                        String insert = v.toInsert();
                        try {
                            stmtInsert.execute(insert);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    });
                    return this.getMovies();

                } catch (SQLException e) {
                    throw new Error("Problem", e);
                }
            } catch (SQLException e) {
                throw new Error("Problem", e);
            } finally {
                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }
}