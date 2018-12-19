import java.util.ArrayList;
import java.util.List;

/**
 * Class Show is used to fill the Cinema's schedule. It's just a "container" to store all the info about the show
 * in the single object.
 */
public class Show {
    private String time;
    private String name;
    private String format;
    private String ageLimit;
    private String auditorium;
    private List<String> price;

    public Show() {
        price = new ArrayList<>();
    }

    @Override
    public String toString() {
        return time + System.getProperty("line.separator") +
                name + System.getProperty("line.separator") +
                format + System.getProperty("line.separator") +
                ageLimit + System.getProperty("line.separator") +
                auditorium + System.getProperty("line.separator") +
                price;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setAgeLimit(String ageLimit) {
        this.ageLimit = ageLimit;
    }

    public void setAuditorium(String auditorium) {
        this.auditorium = auditorium;
    }

    public List<String> getPrice() {
        return price;
    }
}