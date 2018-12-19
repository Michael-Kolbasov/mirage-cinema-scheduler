import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.util.*;

/**
 * The Cinema class parses data from particular Cinema object and formats it.
 * {@param cinemas} - a Map of all "Mirage Cinema" cinemas. Needs to be created manually. Data is actual as for 19 december 2018.
 * {@param parses} - a utility nested class for parsing data.
 * {@param formalName} - a name of a cinema with official mall information.
 * {@param simpleName} - a name of a cinema without official mall information.
 * {@param link} - a http link for particular cinema. Needs to be created manually. Data is actual as for 19 december 2018.
 * {@param schedule} - formatted day schedule of a particular cinema.
 */
public class Cinema {
    private static Map<String, List<Cinema>> cinemas = fillCinemas();
    private static CinemaScheduleParser parser = new CinemaScheduleParser();
    private String formalName;
    private String simpleName;
    private String link;
    private List<Show> schedule;

    public Cinema(String formalName, String simpleName, String link) {
        this.formalName = formalName;
        this.simpleName = simpleName;
        this.link = link;
    }

    /**
     * This method is just an API for outer calls.
     * @param date  a date for forming a schedule.
     */
    public void parseScheduleByDate(LocalDate date) {
        parser.parseScheduleByDateAndLink(this, date);
    }

    public String getFormalName() {
        return formalName;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public List<Show> getSchedule() {
        return schedule;
    }

    public static Map<String, List<Cinema>> getCinemas() {
        return cinemas;
    }

    /**
     * Link parameter of {@code Cinema} object contains date info in format YYYYMMDD. It is manually substituted by
     * "---DATEINFO---" substring here and is required to change later when forming a schedule.
     * @return TreeMap of all Mirage cinemas as for 2018.12.17
     */
    private static Map<String, List<Cinema>> fillCinemas() {
        Map<String, List<Cinema>> cinemas = new TreeMap<>();

        List<Cinema> archangelsk = new ArrayList<>(3);
        archangelsk.add(new Cinema("ТРК \"ЕвроПарк\"", "Европарк", "http://arhangelsk.mirage.ru/schedule/---DATEINFO---/0/15/0/0/0/schedule.htm"));
        archangelsk.add(new Cinema("ТРК \"Титан Арена\"", "Титан Арена", "http://arhangelsk.mirage.ru/schedule/---DATEINFO---/0/19/0/0/0/schedule.htm"));
        archangelsk.add(new Cinema("ТРЦ \"Макси\"", "Макси", "http://arhangelsk.mirage.ru/schedule/---DATEINFO---/0/22/0/0/0/schedule.htm"));
        cinemas.put("Архангельск", archangelsk);

        List<Cinema> velikiyNovgorod = new ArrayList<>(1);
        velikiyNovgorod.add(new Cinema("ТРЦ \"Мармелад\"", "Мармелад", "http://novgorod.mirage.ru/schedule/---DATEINFO---/0/20/0/0/0/schedule.htm"));
        cinemas.put("Великий Новгород", velikiyNovgorod);

        List<Cinema> moscow = new ArrayList<>(1);
        moscow.add(new Cinema("ТРК \"MARi\"", "Мари", "http://moscow.mirage.ru/schedule/---DATEINFO---/0/18/0/0/0/schedule.htm"));
        cinemas.put("Москва", moscow);

        List<Cinema> murmansk = new ArrayList<>(1);
        murmansk.add(new Cinema("МФК \"Северное Нагорное\"", "Северное Нагорное", "http://murmansk.mirage.ru/schedule/---DATEINFO---/0/12/0/0/0/schedule.htm"));
        cinemas.put("Мурманск", murmansk);

        List<Cinema> petrozavodsk = new ArrayList<>(3);
        petrozavodsk.add(new Cinema("ТРЦ \"Макси\"", "Макси", "http://petrozavodsk.mirage.ru/schedule/---DATEINFO---/0/7/0/0/0/schedule.htm"));
        petrozavodsk.add(new Cinema("ТРЦ \"Тетрис\"", "Тетрис", "http://petrozavodsk.mirage.ru/schedule/---DATEINFO---/0/6/0/0/0/schedule.htm"));
        petrozavodsk.add(new Cinema("ТРК \"Лотос Plaza\"", "Лотос Плаза", "http://petrozavodsk.mirage.ru/schedule/---DATEINFO---/0/16/0/0/0/schedule.htm"));
        cinemas.put("Петрозаводск", petrozavodsk);

        List<Cinema> pskov = new ArrayList<>(1);
        pskov.add(new Cinema("ТРК \"Акваполис\"", "Акваполис", "http://pskov.mirage.ru/schedule/---DATEINFO---/0/21/0/0/0/schedule.htm"));
        cinemas.put("Псков", pskov);

        List<Cinema> saintPetersburg = new ArrayList<>(10);
        saintPetersburg.add(new Cinema("на Большом", "на Большом", "http://www.mirage.ru/schedule/---DATEINFO---/0/1/0/0/0/schedule.htm"));
        saintPetersburg.add(new Cinema("ТРК \"Гулливер\"", "Гулливер", "http://www.mirage.ru/schedule/---DATEINFO---/0/2/0/0/0/schedule.htm"));
        saintPetersburg.add(new Cinema("ТРК \"Ульянка\"", "Ульянка", "http://www.mirage.ru/schedule/---DATEINFO---/0/3/0/0/0/schedule.htm"));
        saintPetersburg.add(new Cinema("в Озерках", "в Озерках", "http://www.mirage.ru/schedule/---DATEINFO---/0/4/0/0/0/schedule.htm"));
        saintPetersburg.add(new Cinema("ТРК \"Атлантик-Сити\"", "Атлантик-Сити", "http://www.mirage.ru/schedule/---DATEINFO---/0/5/0/0/0/schedule.htm"));
        saintPetersburg.add(new Cinema("ТРК \"Международный\"", "Международный", "http://www.mirage.ru/schedule/---DATEINFO---/0/8/0/0/0/schedule.htm"));
        saintPetersburg.add(new Cinema("ТРК \"Балкания NOVA-2\"", "Балкания НОВА-2", "http://www.mirage.ru/schedule/---DATEINFO---/0/10/0/0/0/schedule.htm"));
        saintPetersburg.add(new Cinema("ТК \"Ока\", г. Колпино", "Ока", "http://www.mirage.ru/schedule/---DATEINFO---/0/11/0/0/0/schedule.htm"));
        saintPetersburg.add(new Cinema("ТРЦ \"Монпасье\"", "Монпасье", "http://www.mirage.ru/schedule/---DATEINFO---/0/13/0/0/0/schedule.htm"));
        saintPetersburg.add(new Cinema("ТРК \"Европолис\"", "Европолис", "http://www.mirage.ru/schedule/---DATEINFO---/0/14/0/0/0/schedule.htm"));
        cinemas.put("Санкт-Петербург", saintPetersburg);

        List<Cinema> smolensk = new ArrayList<>(1);
        smolensk.add(new Cinema("ТРЦ \"Макси\"", "Макси", "http://smolensk.mirage.ru/schedule/---DATEINFO---/0/17/0/0/0/schedule.htm"));
        cinemas.put("Смоленск", smolensk);

        List<Cinema> sterlitamak = new ArrayList<>(1);
        sterlitamak.add(new Cinema("ТЦ \"Арбат\"", "Арбат", "http://sterlitamak.mirage.ru/schedule/---DATEINFO---/0/9/0/0/0/schedule.htm"));
        cinemas.put("Стерлитамак", sterlitamak);

        return cinemas;
    }

    /**
     * A utility parser class.
     */
    private static class CinemaScheduleParser {

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
        private void parseScheduleByDateAndLink(Cinema cinema, LocalDate date) {
            String cinemaLink = cinema.link;
            String dateLink = date.toString().replaceAll("-", "");
            cinemaLink = cinemaLink.replaceAll("---DATEINFO---", dateLink);
            String rawHtmlContent = getContentOfHtmlPage(cinemaLink);
            List<String> rawSchedule = createRawSchedule(rawHtmlContent);
            List<String> cleanSchedule = cleanRawSchedule(rawSchedule);
            cinema.schedule = formSchedule(cleanSchedule);
        }

        /**
         * Step 3. Connect to the site and parse raw html code.
         * @param pageAddress html link from the {@code Cinema} instance
         * @return  raw html code.
         */
        private String getContentOfHtmlPage(String pageAddress) {
            StringBuilder sb = new StringBuilder();
            URLConnection urlConnection = null;
            try {
                URL pageURL = new URL(pageAddress);
                urlConnection = pageURL.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (urlConnection == null) {
                throw new RuntimeException("Could not connect to the site: " + pageAddress);
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"))) {
                String inputLine;
                while ((inputLine = reader.readLine()) != null) {
                    sb.append(inputLine).append(System.getProperty("line.separator"));
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
                if (string.startsWith("<td class=\"col1\">")     //время    (time)
                        || (checkStringForStartingByOnlyRussianLettersOrDigits(string) && string.endsWith("</a>"))  //название фильма   (film name)
                        || (string.contains("Цифровой") || string.contains("Трехмерная"))   //формат    (format)
                        || checkStringForBeingAnAgeLimit(string)    //возрастное ограничение    (age limit)
                        || string.startsWith("<b>Зал")   //зал  (auditorium)
                        || string.contains("<span class=\"price-data\">")   //цена  (price)
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
            for (int i = 0; i < rawSchedule.size(); i++) {
                String content = rawSchedule.get(i);
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
                if (content.startsWith("<span class=\"price-data\">")) {
                    content = content.replaceAll("<span class=\"price-data\">", "Цена: ");
                }
                if (content.startsWith("Время: <span style='display:none;'>24")) {
                    content = content.replaceAll("<span style='display:none;'>24", "");
                    content = content.replaceAll("</span>", "");
                }
                if (content.endsWith("</span>")) {
                    content = content.replaceAll("</span>", "");
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
}