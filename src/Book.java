import java.text.DateFormat;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Book {
    private String title;
    private String authorName;
    private String genre;
    private Date datePublished;
    DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    public Book(String _title, String _authorName, String _genre, String _datePublished) {
        title = _title;
        authorName = _authorName;
        genre = _genre;
        try {
            datePublished = format.parse(_datePublished);
        } catch (Exception e) {
            System.out.println("Failed");
        }
    }
    public void Date() {
        System.out.println(datePublished);
    }
}
