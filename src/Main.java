import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.io.File;
public class Main {
    public static void main(String[] args) {
//        Book book = new Book("All the Kings Men", "Robert Penn Warren", "Historical Fiction", "10/12/2004");
//        book.Date();
//        ReadFile("vivek");
        String tempPath = new File(".").getAbsolutePath();
        String path = tempPath.substring(0,tempPath.length()-1);
        path += "/data";
        makeFolder(path);
        User user = LoginSignUp(path);
        if (user != null) {
            Menu(user);
        }
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
        System.out.print("User name: ");
        String username = scan.nextLine().split(" ")[0]; //get only first part of input
        if (username.toLowerCase().equals("x")) {
            LoginSignUp(path);
            return null;
        }
        if (!invalidUsername(username, path)) {
            System.out.println("Invalid username");
            Login(path);
            return null;
        }
        String correctPassword = ReadFile(username);
        System.out.print("Password: ");
        String givenPassword = scan.nextLine().split(" ")[0]; //get only first part of input
        String hashedPassword = hashPassword(givenPassword);
        if (correctPassword.contains(hashedPassword)) {
            System.out.println("Logged in");
        }
        else {
            System.out.println("Invalid password");
            Login(path);
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
        System.out.println("Sign up screen");
        System.out.print("User name: ");
        String username = scan.nextLine().split(" ")[0];
        if (username.toLowerCase().equals("x")) {
            LoginSignUp(path);
            return null;
        }
        if(invalidUsername(username, path)) {
            System.out.println("Sorry that username is already taken");
            SignUp(path);
            return null;
        }
        System.out.print("Password: ");
        String password = scan.nextLine().split(" ")[0]; //TODO: nextLine that get first word
        System.out.print("Re-enter password: ");
        String doubleVerification = scan.nextLine().split(" ")[0];


        if(!password.equals((doubleVerification))) {
            System.out.println("Sorry that's wrong. Please sign up again");
            SignUp(path);
            return null;
        }
        User person = new User(username);
        person.saveToFile(hashPassword(password));
        return person;
    }
    public static void Menu(User user) {
        String[] menuOptions = {"Browse books", "Return a Book", "Donate a Book", "Exit"};
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
                    browseBooks();
                    break;
                case 2:
                    returnBook();
                    break;
                case 3:
                    donateBook();
                    break;
                case 4:
                    valid = exit();
                    break;
            }
        } catch (Exception e) {
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

    public static void browseBooks() {
        System.out.println("Sorry this functionality has not been built as of yet");
    }
    public static void returnBook() {
        System.out.println("Sorry this functionality has not been built as of yet");
    }
    public static void donateBook() {
        System.out.println("Sorry this functionality has not been built as of yet");
    }
    public static boolean exit() {
        return false;
    }
}
