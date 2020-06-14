package com.example.scheduler;

import com.example.scheduler.model.Cinema;
import com.example.scheduler.model.Show;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * A utility parser class.
 */
public class CinemaScheduleParser {

    /**
     * This method controls the flow of parsing content.
     * Step 1. Remove the "-" chars from LocalDate instance. "2018-12-31" -> "20181231";
     * Step 2. Replace substituted string "---DATEINFO---" in the link with actual formatted data:
     *          "http://mirage.ru/.../---DATAINFO---/..." -> "http://mirage.ru/.../20181231/..."
     * Step 3. Connect to the site and parse raw html code.
     * Step 4. Format the raw html code to the raw List of Strings.
     * Step 5. Clean the raw List of String from all unnecessary data to the clean List of Strings.
     * Step 6. Create a real cinema schedule from the clean List of Strings.
     * @param cinema    a particular cinema from which schedule will be parsed.
     * @param date  a date for schedule.
     */
    public List<Show> parseScheduleByDateAndLink(Cinema cinema, LocalDate date) {
        String cinemaLink = cinema.getLink();
        String dateLink = date.toString().replaceAll("-", "");
        cinemaLink = cinemaLink.replaceAll("---DATEINFO---", dateLink);
        String rawHtmlContent = getContentOfHtmlPage(cinemaLink);
        List<String> rawSchedule = createRawSchedule(rawHtmlContent);
        List<String> cleanSchedule = cleanRawSchedule(rawSchedule);
        return formSchedule(cleanSchedule);
    }

    /**
     * Step 3. Connect to the site and parse raw html code.
     * @param pageAddress html link from the {@code Cinema} instance
     * @return  raw html code.
     */
    private String getContentOfHtmlPage(String pageAddress) {
        StringBuilder sb = new StringBuilder();

        URLConnection urlConnection;
        try {
            URL pageURL = new URL(pageAddress);
            urlConnection = pageURL.openConnection();
        } catch (IOException e) {
            throw new RuntimeException("Could not connect to the site: " + pageAddress, e);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8))) {
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                sb.append(inputLine)
                    .append(System.getProperty("line.separator"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * Step 4. Format the raw html code with needed strings to the raw List of Strings.
     * @param htmlPage  raw html code with needed strings.
     * @return  raw List of needed Strings.
     */
    private List<String> createRawSchedule(String htmlPage) {
        String[] slices = htmlPage.split(System.getProperty("line.separator"));
        List<String> rawDataList = new ArrayList<>();
        for (String string : slices) {
            string = string.trim();
            if (string.startsWith("<span class=\"price\" title=\"") || string.startsWith("<span class=\"price last\" title=\"")) {
                continue;
            }
            if (string.startsWith("<td class=\"col1\">")     //время    (time)
                    || (checkStringForStartingByOnlyRussianLettersOrDigits(string) && string.endsWith("</a>"))  //название фильма   (film name)
                    || (string.contains("Цифровой") || string.contains("Трехмерная"))   //формат    (format)
                    || checkStringForBeingAnAgeLimit(string)    //возрастное ограничение    (age limit)
                    || string.startsWith("<b>Зал")   //зал  (auditorium)
                    || (string.startsWith("<b>Молния</b>") || string.startsWith("<b>Синема</b>") || string.startsWith("<b>Мираж</b>") || string.startsWith("<b>Венеция VIP</b>"))   //зал для кинотеатра на Большом в СПб  (auditorium for cinema on Bolshoi prospekt in SPb)
                    || (string.startsWith("<b>Красный</b>") || string.startsWith("<b>Бирюзовый</b>"))   //зал для кинотеатра в Гулливере в СПб  (auditorium for cinema in Gulliver in SPb)
                    || string.contains("<span class=\"price")   //цена  (price)
                    || string.startsWith("<i class=\"ico chair\"></i>")    //сиденье   (chair)
                    || string.startsWith("<i class=\"ico inv\"></i>")    //инвалидное кресло    (invalid armchair)
                    || string.startsWith("<i class=\"ico armchair\"></i>")    //кресло  (armchair)
                    || string.startsWith("<i class=\"ico double-armchair\"></i>")    //двойное кресло   (double armchair)
                    || string.startsWith("<i class=\"ico triple-armchair\"></i>"))    //тройное кресло  (triple armchair)
            {
                rawDataList.add(string);
            }
        }
        return rawDataList;
    }

    /**
     * Step 5. Clean the raw List of String from all unnecessary data to the clean List of Strings.
     * @param rawSchedule   raw schedule with all the html tags.
     * @return  clean schedule with only needed info.
     */
    private List<String> cleanRawSchedule(List<String> rawSchedule) {
        List<String> cleanSchedule = new ArrayList<>();
        for (String content : rawSchedule) {
            content = content.trim();
            content = content.replaceAll("\t", "");
            if (checkStringForStartingByOnlyRussianLettersOrDigits(content) && !checkStringForBeingAnAgeLimit(content)) {
                content = "Фильм: " + content;
            }
            if (checkStringForBeingAnAgeLimit(content)) {
                content = "Возрастное ограничение: " + content;
            }
            if (content.startsWith("<b>Зал")) {
                content = content.replaceAll("<b>Зал", "Зал:");
                content = content.replaceAll("</b>", "");
            }
            if ((content.startsWith("<b>Молния</b>") || content.startsWith("<b>Синема</b>")
                    || content.startsWith("<b>Мираж</b>") || content.startsWith("<b>Венеция VIP</b>"))
                    || content.startsWith("<b>Красный</b>") || content.startsWith("<b>Бирюзовый</b>")) {
                content = content.replaceAll("</b>", "");
                content = content.replaceAll("<b>", "");
                content = "Зал: " + content;
            }
            if (content.startsWith("<td class=\"col1\"><b>")) {
                content = content.replaceAll("<td class=\"col1\"><b>", "Время: ");
                content = content.replaceAll("</b>", "");
                content = content.replaceAll("</td>", "");
            }
            if (content.startsWith("<td class=\"col3\"><i class=\"ico ico-2d\" title=\"Цифровой\"></i>")) {
                content = content.replaceAll("<td class=\"col3\"><i class=\"ico ico-2d\" title=\"Цифровой\"></i>", "Формат: 2D");
                content = content.replaceAll("</td>", "");
            }
            if (content.startsWith("<td class=\"col3\"><i class=\"ico ico-3d\" title=\"Трехмерная\"></i>")) {
                content = content.replaceAll("<td class=\"col3\"><i class=\"ico ico-3d\" title=\"Трехмерная\"></i>", "Формат: 3D");
                content = content.replaceAll("</td>", "");
            }
            if (content.startsWith("<i class=\"ico chair\"></i>")) {
                content = content.replaceAll("<i class=\"ico chair\"></i>", "Сиденье");
            }
            if (content.startsWith("<i class=\"ico armchair\"></i>")) {
                content = content.replaceAll("<i class=\"ico armchair\"></i>", "Кресло");
            }
            if (content.startsWith("<i class=\"ico inv\"></i>")) {
                content = content.replaceAll("<i class=\"ico inv\"></i>", "Инвалидное кресло");
            }
            if (content.startsWith("<i class=\"ico double-armchair\"></i>")) {
                content = content.replaceAll("<i class=\"ico double-armchair\"></i>", "Двойное кресло");
            }
            if (content.startsWith("<i class=\"ico triple-armchair\"></i>")) {
                content = content.replaceAll("<i class=\"ico triple-armchair\"></i>", "Тройное кресло");
            }
            if (content.startsWith("<span class=\"price")) {
                if (content.startsWith("<span class=\"price-data\">")) {
                    content = content.replaceAll("<span class=\"price-data\">", "Цена: ");
                } else if (content.startsWith("<span class=\"price\">")) {
                    content = content.replaceFirst("<span class=\"price\">", "Цена: ");
                    content = content.replaceAll("<span class=\"price\"></span>", "");
                    content = content.replaceAll("<span class=\"price last\"></span>", "");
                    content = content.replaceAll("</span> ", "");
                    content = content.trim();
                }
            }
            if (content.startsWith("Время: <span style='display:none;'>24")) {
                content = content.replaceAll("<span style='display:none;'>24", "");
                content = content.replaceAll("</span>", "");
            }
            if (content.endsWith("</span>") || content.endsWith("</span> ")) {
                content = content.replaceAll("</span>", "");
                content = content.replaceAll("</span> ", "");
            }
            if (content.endsWith("</a>")) {
                content = content.replaceAll("</a>", "");
            }
            if (content.contains("~")) {
                content = content.replaceAll("~", "");
            }
            if (content.contains("Dolby Atmos")) {
                content = content.replaceAll("Dolby Atmos", "");
            }
            content = content.trim();
            cleanSchedule.add(content);
        }
        return cleanSchedule;
    }

    /**
     * Step 6. Create a real cinema schedule from the clean List of Strings.
     * A {@param cleanSchedule} Always starts with time and ends with price. (As for 2018.12.19).
     * @param cleanSchedule a List with only needed info.
     * @return  a real schedule.
     */
    private List<Show> formSchedule(List<String> cleanSchedule) {
        if (cleanSchedule.size() == 0) {
            return new ArrayList<>();
        }

        int count = 0;
        for (String string : cleanSchedule) {
            if (string.startsWith("Фильм:")) {
                count++;
            }
        }
        while (!cleanSchedule.get(0).startsWith("Время:")) {
            cleanSchedule.remove(0);
        }
        List<Show> realSchedule = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            realSchedule.add(new Show());
        }
        for (int i = 0, stringPosition = 0; i < realSchedule.size(); i++) {
            Show show = realSchedule.get(i);
            if (cleanSchedule.get(stringPosition).startsWith("Время:")) {
                show.setTime(cleanSchedule.get(stringPosition));
                stringPosition++;
            }
            if (cleanSchedule.get(stringPosition).startsWith("Фильм:")) {
                show.setName(cleanSchedule.get(stringPosition));
                stringPosition++;
            }
            if (cleanSchedule.get(stringPosition).startsWith("Формат:")) {
                show.setFormat(cleanSchedule.get(stringPosition));
                stringPosition++;
            }
            if (cleanSchedule.get(stringPosition).startsWith("Возрастное ограничение:")) {
                show.setAgeLimit(cleanSchedule.get(stringPosition));
                stringPosition++;
            }
            if (cleanSchedule.get(stringPosition).startsWith("Зал:")) {
                show.setAuditorium(cleanSchedule.get(stringPosition));
                stringPosition++;
            }
            if (stringPosition < cleanSchedule.size() && lineIsAChairTypeData(cleanSchedule.get(stringPosition))) {
                stringPosition++;
            }
            if (stringPosition < cleanSchedule.size() && cleanSchedule.get(stringPosition).startsWith("Цена:")) {
                if (lineIsAChairTypeData(cleanSchedule.get(stringPosition - 1))) {
                    show.getPrice().add(cleanSchedule.get(stringPosition - 1));
                    show.getPrice().add(cleanSchedule.get(stringPosition));
                    stringPosition++;
                    while (stringPosition < cleanSchedule.size() && lineIsAChairTypeData(cleanSchedule.get(stringPosition))) {
                        stringPosition++;
                        if (cleanSchedule.get(stringPosition).startsWith("Цена")) {
                            show.getPrice().add(cleanSchedule.get(stringPosition - 1));
                            show.getPrice().add(cleanSchedule.get(stringPosition));
                            stringPosition++;
                        }
                    }
                } else {
                    while (stringPosition < cleanSchedule.size() && cleanSchedule.get(stringPosition).startsWith("Цена:")) {
                        show.getPrice().add(cleanSchedule.get(stringPosition));
                        stringPosition++;
                    }
                }
            }
        }
        return realSchedule;
    }

    private boolean lineIsAChairTypeData(String string) {
        return string.equalsIgnoreCase("Сиденье") ||
                string.equalsIgnoreCase("Кресло") ||
                string.equalsIgnoreCase("Инвалидное кресло") ||
                string.equalsIgnoreCase("Двойное кресло") ||
                string.equalsIgnoreCase("Тройное кресло");
    }

    private boolean checkStringForStartingByOnlyRussianLettersOrDigits(String string) {
        string = string.toLowerCase();
        if (string.length() == 0) {
            return false;
        } else {
            char ch = string.charAt(0);
            return (ch >= 'а' && ch <= 'я') || (ch >= '1' && ch <= '9');
        }
    }

    private boolean checkStringForBeingAnAgeLimit(String string) {
        return (string.length() > 0 && string.length() < 4 && string.contains("+"));
    }
}
