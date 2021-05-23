import java.util.ArrayList;
public class Library {
    ArrayList<String> titles, authors, genres, dates;
    ArrayList<Integer> indices, checkoutCount;
    public Library(String[] bookList) {
        for (String bookLine: bookList) {
            String[] bookInfo = bookLine.split("; ");
            String title = bookInfo[1], author = bookInfo[2], genre = bookInfo[3], date = bookInfo[4];
            int index = Integer.parseInt(bookInfo[0]), checkoutCount = Integer.parseInt(bookInfo[5]);
            titles.add(title);
        }
    }
}
