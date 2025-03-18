import java.awt.*;
import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    // ANSI escape codes for colors and bold text
    public static final String RESET = "\u001B[0m"; // Reset to default
    public static final String BOLD = "\u001B[1m";
    public static final String CYAN = "\u001B[36m";
    public static final String PURPLE = "\u001B[35m";
    public static final String YELLOW = "\u001B[33m";
    public static final String GREEN = "\u001B[32m";
    public static final String RED = "\u001B[31m";

//    private static final String DB_URL = "jdbc:ucanaccess://C:/Users/thand/OneDrive/Desktop/" +
//            "Application/Hex_Internship/HexSoftwares_Movie_Database/MovieDatabase.accdb";

    // Defining the base URL part
    private static final String DB_BASE_URL = "jdbc:ucanaccess://";

    // Defining the file path part (Copy the path to your database here, and it should work)
    private static final String DB_FILE_PATH = "C:/Users/thand/OneDrive/Desktop/Application/Hex_Internship/HexSoftwares_Movie_Database/MovieDatabase.accdb";

    // Combine the base URL and file path to form the full URL
    private static final String DB_URL = DB_BASE_URL + DB_FILE_PATH;

    private static final String USER = "";
    private static final String PASS = "";
    private Connection con = null;
    private Statement stmt = null;
    //private Scanner scan = new Scanner(System.in);

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        // Instantiate Main class to run the application
        new Main();
    }

    public Main() throws SQLException, ClassNotFoundException {
        System.out.println(BOLD + RED + "███╗   ███╗ ██████╗ ██╗   ██╗██╗███████╗███████╗" + RESET);
        System.out.println(BOLD + GREEN + "████╗ ████║██╔═══██╗██║   ██║██║██╔════╝██╔════╝" + RESET);
        System.out.println(BOLD + CYAN + "██╔████╔██║██║   ██║██║   ██║██║█████╗  ███████╗" + RESET);
        System.out.println(BOLD + PURPLE + "██║╚██╔╝██║██║   ██║██║   ██║██║██╔══╝  ╚════██║" + RESET);
        System.out.println(BOLD + RED + "██║ ╚═╝ ██║╚██████╔╝╚██████╔╝██║███████╗███████║" + RESET);
        System.out.println(BOLD + RED + "╚═╝     ╚═╝ ╚═════╝  ╚═════╝ ╚═╝╚══════╝╚══════╝" + RESET);
        System.out.println();

        // Print a welcome message in bold
        System.out.println(BOLD + YELLOW + "✨ Welcome to HexSoftwares Movie Database! ✨" + RESET);
        System.out.println();

        boolean connected = connect(); // Ensure database connection
        while (!connected) {
            System.out.println("Failed to connect to database. Check username and password.");
            connected = connect(); // Retry connection
        }

        // Initialize and display the menu
        System.out.println();
        Movie_Menu menu = new Movie_Menu("Movie Database", con);

        menu.add_new_movie("Add Movie", this::addMovie);
        menu.add_new_movie("Delete Movie", this::deleteMovie);
        menu.add_new_movie("Search Movie", this::searchMovie);
        menu.add_new_movie("Display All Movies", this::displayMovies);

        menu.run(); // Run the menu interface
        exit(); // Exit the application
    }

    //1. Connect to the database
    public boolean connect() {
        try {
            // Load UCanAccess Driver
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

            // Attempt connection
            con = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

            System.out.println("Connected successfully to MS Access database!");
            return true;
        } catch (Exception e) {
            System.out.println("Connection attempt failed: " + e.getMessage());
            return false;
        }
    }
    //2. Exit cleanup to close connection.
    public void exit() {
        try {
            if (stmt != null) stmt.close();
            if (con != null) con.close();
            System.out.println("Database connection closed.");
        } catch (SQLException e) {
            System.out.println("Error closing the connection: " + e.getMessage());
        }
    }

    //3. Adding a new movie to a database
    public void addMovie() {
        Scanner in = new Scanner(System.in);

        // Getting movie details from the user
        System.out.println("Enter the movie title:");
        String title = in.nextLine();

        System.out.println("Enter the movie genre:");
        String genre = in.nextLine();

        System.out.println("Enter the movie release year:");
        int releaseYear = in.nextInt();

        in.nextLine();  // To consume the remaining newline
        System.out.println("Enter the movie director:");
        String director = in.nextLine();

        System.out.println("Enter the movie duration (e.g., 2h 30m):");
        String duration = in.nextLine();

        // Validate the rating input
        double rating = 0;
        boolean validRating = false;
        while (!validRating) {
            System.out.println("Enter the movie rating (out of 10):");
            try {
                rating = in.nextDouble();
                if (rating >= 1 && rating <= 10) {
                    validRating = true;  // If rating is valid, break out of the loop
                } else {
                    System.out.println("Invalid rating. Please enter a number between 1 and 10.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number between 1 and 10.");
                in.nextLine();  // Clear the buffer
            }
        }

        try {
            // SQL query to insert data into the database
            String sql = "INSERT INTO movie_details (Title, Genre, ReleaseYear, Director, Duration, Rating) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = con.prepareStatement(sql);

            // Setting parameters for the prepared statement
            stmt.setString(1, title);
            stmt.setString(2, genre);
            stmt.setInt(3, releaseYear);
            stmt.setString(4, director);
            stmt.setString(5, duration);
            stmt.setDouble(6, rating);

            // Executing the update query
            int rowsAffected = stmt.executeUpdate();

            // Checking if the movie was added successfully
            if (rowsAffected > 0) {
                System.out.println("Movie added successfully!");
            } else {
                System.out.println("Failed to add movie.");
            }
        } catch (SQLException e) {
            System.out.println("Error adding movie: " + e.getMessage());
        }
    }

    //4. Deleteing a movie from a database
    public void deleteMovie() {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter the movie ID to delete:");
        int id = in.nextInt();

        try {
            String sql = "DELETE FROM movie_details WHERE MovieID = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Movie deleted successfully!");
            } else {
                System.out.println("No movie found with that ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting movie: " + e.getMessage());
        }
    }
    //5. Searching for a movie from the database
    public void searchMovie() {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter the movie title to search:");
        String title = in.nextLine();

        try {
            String sql = "SELECT * FROM movie_details WHERE Title LIKE ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, "%" + title + "%");  // Using LIKE to search for titles that contain the provided string
            ResultSet rs = stmt.executeQuery();

            // Display headers if movies are found
            System.out.println("Movies Found:");
            System.out.println("------------------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("%-5s %-30s %-20s %-10s %-15s %-30s %-10s\n", "ID", "Title", "Director", "Year", "Genre", "Duration", "Rating");
            System.out.println("------------------------------------------------------------------------------------------------------------------------------------");

            // Check if any results are found
            boolean found = false;
            while (rs.next()) {
                int id = rs.getInt("MovieID");
                String movieTitle = rs.getString("Title");
                String director = rs.getString("Director");
                int year = rs.getInt("ReleaseYear");
                String genre = rs.getString("Genre");
                String duration = rs.getString("Duration");
                double rating = rs.getDouble("Rating");

                // Display movie details in a formatted way
                System.out.printf("%-5d %-30s %-20s %-10d %-15s %-30s %-10.1f\n", id, movieTitle, director, year, genre, duration, rating);
                found = true;
            }

            // If no movies found
            if (!found) {
                System.out.println("No movies found with the title \"" + title + "\".");
            }
        } catch (SQLException e) {
            System.out.println("Error searching movie: " + e.getMessage());
        }
        System.out.println();
    }

    //6. Displaying the movies in the database
    public void displayMovies() {
        try {
            String sql = "SELECT * FROM movie_details";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            // Display headers for clarity
            System.out.println("Movies in Database:");
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("%-5s %-30s %-20s %-10s %-15s %-30s %-10s\n", "ID", "Title", "Director", "Year", "Genre", "Duration", "Rating");
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------");

            // Iterate over the result set and display movie details
            while (rs.next()) {
                int id = rs.getInt("MovieID");
                String title = rs.getString("Title");
                String director = rs.getString("Director");
                int year = rs.getInt("ReleaseYear");
                String genre = rs.getString("Genre");
                String duration = rs.getString("Duration");
                double rating = rs.getDouble("Rating");

                // Display movie details in a formatted way
                System.out.printf("%-5d %-30s %-20s %-10d %-15s %-30s %-10.1f\n", id, title, director, year, genre, duration, rating);
            }
        } catch (SQLException e) {
            System.out.println("Error displaying movies: " + e.getMessage());
        }
        System.out.println();
    }

}
