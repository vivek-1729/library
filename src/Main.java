import java.nio.file.Files;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
public class Main {
    public static void main(String[] args) {
        String tempPath = new File(".").getAbsolutePath();
        String path = tempPath.substring(0,tempPath.length()-1);
        path += "/data";
        makeFolder(path);
        User user = LoginSignUp(path);
        while (user == null) {
            user = LoginSignUp(path);
        }
        System.out.println("Main Menu");
        Menu(user);
    }

    public static String hashPassword(String password) {
        GFG hashMapping = new GFG();
        String hashedPassword = hashMapping.hash(password);
        return hashedPassword;
    }
    public static User LoginSignUp(String path) {
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.print("Sign up or login(s/l)? ");
            char signUpOrLogin = scan.next().toLowerCase().charAt(0);
            if(signUpOrLogin == 's') {
                return SignUp(path);
            } //call sign up screen
            else if (signUpOrLogin=='l') {
                return Login(path);
            } //call login screen
            else if (signUpOrLogin == 'x' || signUpOrLogin == 'X') {
                return null;
            }
            else {
                System.out.println("Invalid input. Must enter s for signup or l for login");
            } //ask to reenter
        }
    }
    public static User Login(String path) {
        Scanner scan = new Scanner(System.in);
        System.out.print("User name (press x to go back): ");
        String username = scan.nextLine().split(" ")[0]; //get only first part of input
        if (username.toLowerCase().equals("x")) {
            LoginSignUp(path);
        }
        if (!invalidUsername(username, path)) {
            System.out.println("Invalid username");
            return null;
        }
        String correctPassword = ReadFile(username);
        System.out.print("Password: ");
        String givenPassword = scan.nextLine().split(" ")[0]; //get only first part of input
        String hashedPassword = hashPassword(givenPassword);
        if (correctPassword.contains(hashedPassword)) {
            System.out.println("\n Logged in \n");
        }
        else {
            System.out.println("Invalid password");
            return null;
        }
        return new User(username);
    }
    public static String ReadFile(String filename) {
        try {
            String content;
            content = new String(Files.readAllBytes(Paths.get("data/" + filename + ".txt")));
            return content;
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
    public static boolean invalidUsername(String username, String path) {
        File folder = new File(path); //TODO: Make dynamic
        File[] listOfFiles = folder.listFiles();
        boolean duplicate = false;
        String fullFilename;
        String filename;
        for (File file : listOfFiles) {
             fullFilename = file.getName().toLowerCase();
             filename = fullFilename.substring(0,fullFilename.length()-4);
            if (filename.equals(username.toLowerCase())) {
                duplicate = true;
            }
        }
        return duplicate;
    }
    public static User SignUp(String path) {
        boolean valid = true;
        Scanner scan = new Scanner(System.in);
        System.out.print("User name: ");
        String username = scan.nextLine().split(" ")[0];
        if (username.toLowerCase().equals("x")) {
            LoginSignUp(path);
            return null;
        }
        if(invalidUsername(username, path)) {
            System.out.println("Sorry that username is already taken");
            return null;
        }
        System.out.print("Password: ");
        String password = scan.nextLine().split(" ")[0];
        System.out.print("Re-enter password: ");
        String doubleVerification = scan.nextLine().split(" ")[0];


        if(!password.equals((doubleVerification))) {
            System.out.println("Sorry that's wrong. Please sign up again");
            return null;
        }

        User person = new User(username);
        person.saveToFile(hashPassword(password));
        return person;
    }
    public static void Menu(User user) {
        String[] bookList = readLibrary();
        String[] menuOptions = {"Browse books (sorted by checkout count)", "Return a Book", "Donate a Book", "Exit"};
            for (int i = 0; i < menuOptions.length; i++) {
                System.out.println((i + 1) + ". " + menuOptions[i]);
        }
        System.out.println("Choose an option: ");
        Scanner scan = new Scanner(System.in);
        boolean valid = true;
        try {
            int choice = scan.nextInt(); //check if the number is an input
            String temp = menuOptions[choice-1]; //check if the input number is valid
            switch (choice) {
                case 1:
                    browseBooks(bookList, user);
                    break;
                case 2:
                    returnBook(user);
                    break;
                case 3:
                    donateBook();
                    break;
                case 4:
                    valid = exit();
                    break;
            }
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Invalid input");
            Menu(user);
            return;
        }
        if (valid) {
            Menu(user);
        }
    }
    public static void makeFolder(String path) {
        File theDir = new File(path);
        if (!theDir.exists()) {
            theDir.mkdirs();
        }
    }

    public static String[] readLibrary() {
        String file = ReadFile("library");
        String[] books = file.split("\n");
//        System.out.println(Arrays.toString(books));
        return books;
    }


    public static void checkout(String[] bookList, int bookIndex, User user) {
        String name = user.getUsername();
        String book = bookList[bookIndex];
        String modifiedLine = "";
        ArrayList<String> originalFile = new ArrayList<>();
        try {
            FileWriter createFile = new FileWriter("data/library.txt");

        for (String bookLine : bookList) {
            if (bookLine.contains(book)) {
                int checkoutCount = Integer.parseInt(String.valueOf(bookLine.charAt(bookLine.length()-1)));
                 modifiedLine = bookLine.substring(0,bookLine.length()-1) + (checkoutCount+1);
                originalFile.add(modifiedLine);
            }
            else {
                originalFile.add(bookLine);
            }
        }
            System.out.println("\n " + book.split(";")[0] + " has been checked out \n");

        String newFile = String.join("\n",originalFile);
        createFile.write(newFile);
        createFile.close();

        String modifiedFile = ReadFile(name) + "\n" + modifiedLine;
        FileWriter writeUser = new FileWriter("data/" + name + ".txt");
        writeUser.write(modifiedFile);
        writeUser.close();


        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void browseBooks(String[] booklist, User user) {
        String[] titles = getTitles(booklist);
        Integer[] counts = getPopularity(booklist);
        Integer[] indices = new Integer[titles.length];
        for (int i = 0; i < indices.length; i++) {
            indices[i] = i;
        }
        int temp, temp3;
        String temp2 = "";
        for (int j = 0; j < counts.length; j++) {
            for (int i = 0; i < counts.length - 1; i++) {
                if (counts[i] < counts[i + 1]) {
                    temp = counts[i];
                    counts[i] = counts[i + 1];
                    counts[i + 1] = temp;

                    temp2 = titles[i];
                    titles[i] = titles[i+1];
                    titles[i+1] = temp2;

                    temp3 = indices[i];
                    indices[i] = indices[i+1];
                    indices[i+1] = temp3;
                }
            }
        }
        for (int i = 0; i < counts.length; i++) {
            System.out.println((i+1) + ". " + titles[i]);
        }
        Scanner scan = new Scanner(System.in);
        System.out.print("Check out book (press 0 to exit): ");
        try {
            int bookChosen = scan.nextInt()-1;
            if (bookChosen == -1) {
                return;
            }
            int bookIndex = indices[bookChosen];
            checkout(booklist,bookIndex, user);
        }
        catch (Exception e) {
            System.out.println("Invalid input");
        }
        }

    public static Integer[] getPopularity(String[] bookList) {
        ArrayList<Integer> counts = new ArrayList<>();
        for (String bookLine : bookList) {
            String[] bookInfo = bookLine.split("; ");
            counts.add(Integer.parseInt(bookInfo[5]));
        }
        Integer[] countsArr = counts.toArray(Integer[]::new);
        return countsArr;
    }
    public static String[] getTitles(String[] bookList) {
        ArrayList<String> titles = new ArrayList<>();
        for (String bookLine: bookList){
            String[] bookInfo = bookLine.split("; ");
            titles.add(bookInfo[0]);
        }

        String[] titlesArr = new String[titles.size()];
        titlesArr = titles.toArray(titlesArr);
        return titlesArr;
    }
    public static void returnBook(User user) {
        String userFile = ReadFile(user.getUsername());
        String[] userLines = userFile.split("\n");
        String[] books = Arrays.copyOfRange(userLines, 1, userLines.length);
        Book[] outstanding = new Book[books.length];
        int index = 0;
        for (int i = 0; i < books.length; i++) {
            Book b = new Book(books[i]);
            if (!b.isReturned()) {
                System.out.print((index+1) +". ");
                outstanding[index] = b;
                b.printTitle();
                index += 1;
            }
        }
        if (outstanding[0] == null) {
            System.out.println("No books to return");
        }
        else {
            for (int i = 0; i < outstanding.length; i++) {
                System.out.println("Which book to return (press 0 to go back)? ");
                Scanner scan = new Scanner(System.in);
                try {
                    int bookIndex = scan.nextInt() - 1;
                    outstanding[bookIndex].checkIn(userFile, user.getUsername());
                    System.out.println("\n" + books[bookIndex].split(";")[0] + " has been returned\n");
                    return;
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
    }
    public static void donateBook() {
        Scanner scan = new Scanner(System.in);
        System.out.print("Book name: ");
        String bookName = scan.nextLine();
        System.out.print("Author: ");
        String author = scan.nextLine();
        System.out.print("Genre: ");
        String genre = scan.nextLine();
        System.out.print("Published date (month/day/year): ");
        String date = scan.nextLine();

        String newBook = "\n" + bookName + "; " + author + "; " + genre + "; " + date + "; -; 0";
        String library = ReadFile("library");
        library += newBook;
        

        try {
            FileWriter createFile = new FileWriter("data/library.txt");
            createFile.write(library);
            createFile.close();
            System.out.println("\n Thank you for donating " + bookName + " to the library \n");
        }
        catch (Exception e) {

        }

    }
    public static boolean exit() {
        return false;
    }
}
