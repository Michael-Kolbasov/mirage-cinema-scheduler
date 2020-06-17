package com.example.scheduler.core;

import com.example.scheduler.model.Cinema;
import com.example.scheduler.model.City;
import com.example.scheduler.model.Show;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * AppLauncher controls the flow of the program. It asks user about which cinema info does that want to get and validates
 * users input.
 */
public class AppLauncher {
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static final Map<City, List<Cinema>> ALL_CINEMAS = Scheduler.getCinemas();
    private static final String INCORRECT_DATA = "Вы ввели некорректные данные. Пожалуйста, попробуйте еще раз.";
    private static final String DATE_PATTERN = "[0-3][0-9].[0-1][0-9].[0-9]{4}";
    private static final String CHOOSE_DATE = "Введите дату в формате ДД.ММ.ГГГГ";

    /**
     * This method retrieves all needed information from user step by step.
     */
    public void launch() {
        String city = defineCity(Scheduler.getCinemas());
        Cinema cinema = defineCinema(city);
        LocalDate date = defineDate();

        Scheduler scheduler = new Scheduler();
        List<Show> shows = scheduler.parseScheduleByDate(cinema, date);
        printCinemaSchedule(shows);

        closeStream();
    }

    /**
     * This method simply prints info about the schedule.
     * @param schedule  to be printed.
     */
    private void printCinemaSchedule(List<Show> schedule) {
        if (schedule.size() == 0) {
            System.out.println("Сеансов нет.");
            return;
        }

        for (Show show : schedule) {
            System.out.println(show);
            System.out.println();
        }
    }

    /**
     * This method asks user which city he is interested in.
     * @param cinemas   a map of all Mirage Cinema's cinemas in all cities.
     * @return  a city that user has chosen.
     */
    private String defineCity(Map<City, List<Cinema>> cinemas) {
        System.out.println("Выберите город:");
        cinemas.keySet().forEach(System.out::println);

        String userInput;
        try {
            userInput = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (!isCity(userInput)) {
            throw new RuntimeException(INCORRECT_DATA);
        } else {
            return userInput;
        }
    }

    /**
     * If there is only one cinema in the previously chosen city, then it is chosen automatically.
     * If there are several cinemas in the city, then {@code chooseCinemaInCity()} method starts.
     * @param city a key from the Map of cinemas that is a city representation.
     * @return  a cinema in the city that user has chosen.
     */
    private Cinema defineCinema(String city) {
        City key = Objects.requireNonNull(City.byName(city), INCORRECT_DATA);
        if (ALL_CINEMAS.get(key).size() == 1) {
            return ALL_CINEMAS.get(key).get(0);
        } else {
            return chooseCinemaInCity(key);
        }
    }

    /**
     * This method asks user which cinema in the previously chosen city he is interested in.
     * @param city a key from the Map of cinemas that is a city representation.
     * @return  a cinema in the city that user has chosen.
     */
    private Cinema chooseCinemaInCity(City city) {
        List<Cinema> cinemasInCity = ALL_CINEMAS.get(city);
        System.out.println("Выберите кинотеатр из списка:");
        cinemasInCity.stream()
                .map(Cinema::getSimpleName)
                .forEach(System.out::println);

        String userInput;
        try {
            userInput = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return cinemasInCity.stream()
                .filter(cinema -> cinema.getSimpleName().equalsIgnoreCase(userInput) || cinema.getFormalName().equalsIgnoreCase(userInput))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(INCORRECT_DATA));
    }

    /**
     * This method asks user about which date he is interested in.
     * Users input must follow this pattern: DD.MM.YYYY
     * @return  a chosen date.
     */
    private LocalDate defineDate() {
        System.out.println(CHOOSE_DATE);

        String date;
        try {
            date = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (!isDate(date)) {
            throw new RuntimeException(INCORRECT_DATA);
        } else {
            String[] chunks = date.split("\\.");
            return LocalDate.of(Integer.parseInt(chunks[2]), Integer.parseInt(chunks[1]), Integer.parseInt(chunks[0]));
        }
    }

    private boolean isCity(String city) {
        return Scheduler.getCinemas().keySet().stream()
                .map(City::getName)
                .anyMatch(name -> name.equalsIgnoreCase(city));
    }

    /**
     * The pattern is: DD.MM.YYYY
     */
    private boolean isDate(String date) {
        Pattern pattern = Pattern.compile(DATE_PATTERN);
        Matcher matcher = pattern.matcher(date);
        return matcher.matches();
    }

    private void closeStream() {
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}