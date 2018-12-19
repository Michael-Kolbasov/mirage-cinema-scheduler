import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * AppLauncher controls the flow of the program. It asks user about which cinema info does that want to get and validates
 * users input.
 */
public class AppLauncher {
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    /**
     * This method retrieves all needed information from user step by step.
     */
    public void launch() {
        String city = defineCity(Cinema.getCinemas());
        Cinema cinema = defineCinema(city);
        LocalDate date = defineDate();
        cinema.parseScheduleByDate(date);
        printCinemaSchedule(cinema.getSchedule());
        closeStream();
    }

    /**
     * This method simply prints info about the schedule.
     * @param schedule  to be printed.
     */
    private void printCinemaSchedule(List<Show> schedule) {
        for (Show show : schedule) {
            System.out.println(show);
            System.out.println();
        }
    }

    /**
     * This method asks user about which city he is interested in.
     * @param cinemas   a map of all Mirage Cinema's cinemas in all cities.
     * @return  a city that user has chosen.
     */
    private String defineCity(Map<String, List<Cinema>> cinemas) {
        System.out.println("Выберите город:");
        for (String city : cinemas.keySet()) {
            System.out.println(city);
        }
        String userInput = null;
        try {
            userInput = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (userInput == null || !checkStringForBeingACorrectCity(userInput)) {
            throw new RuntimeException("Вы ввели некорректные данные. Пожалуйста, попробуйте еще раз.");
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
        Map<String, List<Cinema>> allCinemas = Cinema.getCinemas();
        Cinema cinema;
        if (allCinemas.get(city).size() == 1) {
            cinema = allCinemas.get(city).get(0);
        } else {
            cinema = chooseCinemaInCity(city);
        }
        return cinema;
    }

    /**
     * This method asks user about which cinema in the previously chosen city he is interested in.
     * @param city a key from the Map of cinemas that is a city representation.
     * @return  a cinema in the city that user has chosen.
     */
    private Cinema chooseCinemaInCity(String city) {
        Map<String, List<Cinema>> allCinemas = Cinema.getCinemas();
        List<Cinema> cinemasInCity = allCinemas.get(city);
        System.out.println("Выберите кинотеатр из списка:");
        for (Cinema cinema : cinemasInCity) {
            System.out.println(cinema.getFormalName());
        }
        Cinema chosenCinema = null;
        String userInput = null;
        try {
            userInput = reader.readLine();
            for (int i = 0; i < cinemasInCity.size(); i++) {
                if (userInput.equalsIgnoreCase(cinemasInCity.get(i).getSimpleName())
                        || userInput.equalsIgnoreCase(cinemasInCity.get(i).getFormalName())) {
                    chosenCinema = cinemasInCity.get(i);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (chosenCinema == null || !checkStringForBeingACorrectCinemaInCity(userInput)) {
            throw new RuntimeException("Вы не выбрали кинотеатр из списка. Пожалуйста, попробуйте еще раз.");
        } else {
            return chosenCinema;
        }
    }

    /**
     * This method asks user about which date he is interested in.
     * Users input must follow this pattern: DD.MM.YYYY
     * @return  a chosen date.
     */
    private LocalDate defineDate() {
        System.out.println("Введите дату в формате ДД.ММ.ГГГГ. Пример: 19.12.2018");
        String date = null;
        try {
            date = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (date == null || !checkStringForBeingADate(date)) {
            throw new RuntimeException("Вы не выбрали дату. Пожалуйста, попробуйте еще раз.");
        } else {
            String[] chunks = date.split("\\.");
            int day = Integer.parseInt(chunks[0]);
            int month = Integer.parseInt(chunks[1]);
            int year = Integer.parseInt(chunks[2]);
            return LocalDate.of(year, month, day);
        }
    }

    private boolean checkStringForBeingACorrectCity(String string) {
        Map<String, List<Cinema>> cinemas = Cinema.getCinemas();
        for (String city : cinemas.keySet()) {
            if (string.equalsIgnoreCase(city)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkStringForBeingACorrectCinemaInCity(String string) {
        Map<String, List<Cinema>> cinemas = Cinema.getCinemas();
        for (List<Cinema> cinemasInCity : cinemas.values()) {
            for (Cinema cinema : cinemasInCity) {
                if (string.equalsIgnoreCase(cinema.getSimpleName()) || string.equalsIgnoreCase(cinema.getFormalName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * The pattern is: DD.MM.YYYY
     * @param string
     * @return
     */
    private boolean checkStringForBeingADate(String string) {
        Pattern pattern = Pattern.compile("[0-3]{1,1}[0-9]{1,1}.[0-1]{1,1}[0-9]{1,1}.[0-9]{4,4}");
        Matcher mather = pattern.matcher(string);
        return mather.matches();
    }

    private void closeStream() {
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}