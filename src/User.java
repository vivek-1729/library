import java.io.FileWriter;
public class User {
    private String username;
    User(String _username) {
        username = _username;
    }
    public void saveToFile(String password) {
        try {
            FileWriter createFile = new FileWriter("data/" + username + ".txt");
            createFile.write(password);
            createFile.close();
            System.out.println("\n New User Created \n");
        }
        catch (Exception e) {
            System.out.println("Something went wrong");
        }
    }

    public String getUsername() {
        return username;
    }
}
