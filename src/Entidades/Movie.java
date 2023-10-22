package Entidades;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Date;

public class Movie implements Serializable, Comparable<Movie> {


    private Integer userId;
    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private Date creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private long oscarsCount; //Значение поля должно быть больше 0
    private Integer budget; //Значение поля должно быть больше 0, Поле не может быть null
    private int totalBoxOffice; //Значение поля должно быть больше 0
    private MpaaRating mpaaRating; //Поле может быть null
    private Person operator; //Поле может быть null


    public Movie(String name, Coordinates coordinates, Date creationDate, long oscarsCount, Integer budget, int totalBoxOffice, MpaaRating mpaaRating, Person operator) {
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.oscarsCount = oscarsCount;
        this.budget = budget;
        this.totalBoxOffice = totalBoxOffice;
        this.mpaaRating = mpaaRating;
        this.operator = operator;
    }

    public Movie() {
    }

    public Movie(Integer userId,long id, String name, Coordinates coordinates, Date creationDate, long oscarsCount, Integer budget, int totalBoxOffice, MpaaRating mpaaRating, Person operator) {
        this.userId=userId;
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.oscarsCount = oscarsCount;
        this.budget = budget;
        this.totalBoxOffice = totalBoxOffice;
        this.mpaaRating = mpaaRating;
        this.operator = operator;
    }
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public long getOscarsCount() {
        return oscarsCount;
    }

    public void setOscarsCount(long oscarsCount) {
        this.oscarsCount = oscarsCount;
    }

    public Integer getBudget() {
        return budget;
    }

    public void setBudget(Integer budget) {
        this.budget = budget;
    }

    public int getTotalBoxOffice() {
        return totalBoxOffice;
    }

    public void setTotalBoxOffice(int totalBoxOffice) {
        this.totalBoxOffice = totalBoxOffice;
    }

    public MpaaRating getMpaaRating() {
        return mpaaRating;
    }

    public void setMpaaRating(MpaaRating mpaaRating) {
        this.mpaaRating = mpaaRating;
    }

    public Person getOperator() {
        return operator;
    }

    public void setOperator(Person operator) {
        this.operator = operator;
    }

    public String toCsv() {
        String userIdCsv = String.format("%s,", getUserId());
        String idCsv = String.format("%s,", getId());
        String nameCsv = String.format("%s,", getName());
        String dateCsv = String.format("%s,", getCreationDate());
        String oscarsCountCsv = String.format("%s,", getOscarsCount());
        String budgetCsv = String.format("%s,", getBudget());
        String totalBoxOfficeCsv = String.format("%s,", getTotalBoxOffice());
        String coordinatesCsv = getCoordinates().toCsv();
        String mpaaRatingCsv = String.format("%s,", getMpaaRating());
        String personCsv = getOperator().toCsv();
        String movieCsv = String.format("%s%s%s%s%s%s%s%s%s%s",
                userIdCsv,
                idCsv,
                nameCsv,
                coordinatesCsv,
                dateCsv,
                oscarsCountCsv,
                budgetCsv,
                totalBoxOfficeCsv,
                mpaaRatingCsv,
                personCsv);
        return movieCsv;
    }

    @Override
    public String toString() {
        return String.format("id: %s, name: %s", getId(), getName());
    }

    @Override
    public int compareTo(Movie o) {
        return this.getName().compareTo(o.getName());
    }

    public String toInsert() {
        return String.format("""
                        INSERT INTO public.movies(name, creation_date, oscars_count, total_box_office, budget, mpaa_rating, user_id) VALUES ('%s', '%s', %s, %s, %s, '%s', %s);
                        INSERT INTO public.coordinates(movie_id, x, y) SELECT MAX(id), %s, %s FROM public.movies;
                        INSERT INTO public.persons(movie_id, name, height, passport_id, eye_color) SELECT MAX(id), '%s', %s, '%s' , '%s' FROM public.movies;
                        INSERT INTO public.locations(person_id, x, y, z, name) SELECT MAX(id), %s, %s, %s, '%s' FROM public.movies;"""
                , getName()
                , getCreationDate().toString()
                , getOscarsCount()
                , getBudget().toString()
                , getTotalBoxOffice()
                , getMpaaRating().toString()
                , getUserId().toString()
                , getCoordinates().getX().toString()
                , getCoordinates().getY()
                , getOperator().getName()
                , getOperator().getHeight().toString()
                , getOperator().getPassportID().toString()
                , getOperator().getEyeColor().toString()
                , getOperator().getLocation().getX().toString()
                , getOperator().getLocation().getY()
                , getOperator().getLocation().getZ().toString()
                , getOperator().getLocation().getName());
    }
}
