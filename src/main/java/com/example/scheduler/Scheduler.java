package com.example.scheduler;

import com.example.scheduler.model.Cinema;
import com.example.scheduler.model.City;
import com.example.scheduler.model.Show;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The Cinema class parses data from particular Cinema object and formats it.
 * {@param cinemas} - a Map of all "Mirage Cinema" cinemas. Needs to be created manually. Data is actual as for 19 december 2018.
 * {@param parses} - a utility nested class for parsing data.
 * {@param formalName} - a name of a cinema with official mall information.
 * {@param simpleName} - a name of a cinema without official mall information.
 * {@param link} - a http link for particular cinema. Needs to be created manually. Data is actual as for 19 december 2018.
 * {@param schedule} - formatted day schedule of a particular cinema.
 */
public class Scheduler {
    private static final Map<City, List<Cinema>> cinemas = fillCinemas();
    private static final CinemaScheduleParser parser = new CinemaScheduleParser();

    /**
     * This method is just an API for outer calls.
     * @param date  a date for forming a schedule.
     */
    public List<Show> parseScheduleByDate(Cinema cinema, LocalDate date) {
        return parser.parseScheduleByDateAndLink(cinema, date);
    }

    public static Map<City, List<Cinema>> getCinemas() {
        return cinemas;
    }

    /**
     * Link parameter of {@code Cinema} object contains date info in format YYYYMMDD. It is manually substituted by
     * "---DATEINFO---" substring here and is required to change later when forming a schedule.
     * @return TreeMap of all Mirage cinemas as for 2018.12.17
     */
    private static Map<City, List<Cinema>> fillCinemas() {
        return EnumSet.allOf(City.class)
                .stream()
                .collect(Collectors.toMap(
                        Function.identity(), City::getCinemas)
                );
    }
}