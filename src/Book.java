import java.io.FileWriter;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class Book {
    private String title, author, genre, date, returnDate;
    private int checkoutCount;

    public Book(String bookLine) {
        String[] bookInfo = bookLine.split("; ");
        title = bookInfo[0];
        author = bookInfo[1];
        genre = bookInfo[2];
        date = bookInfo[3];
        returnDate = bookInfo[4];
        checkoutCount = Integer.parseInt(bookInfo[5]);
    }

    public void printTitle() {
        System.out.println(title);
    }

    public boolean isReturned() {
        if (returnDate.equals("-")) {
            return false;
        } else {
            return true;
        }
    }

    public void checkIn(String userFile, String username) {
        ArrayList<String> originalFile = new ArrayList<>();
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            LocalDateTime now = LocalDateTime.now();
            String checkInDate = dtf.format(now);
            String[] userLines = userFile.split("\n");
            for (String line : userLines) {
                if (line.contains(title)) {
                    originalFile.add(line.replace("-",checkInDate));
                } else {
                    originalFile.add(line);
                }
            }
            String newFile = String.join("\n",originalFile);
            FileWriter createFile = new FileWriter("data/" + username + ".txt");
            createFile.write(newFile);
            createFile.close();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}
