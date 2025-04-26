import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnexion {
    // lien avec le base de donné oracle SGBD
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    // Nom de l'user
    private static final String USER = "haendel";
    // Mot de passe de la base de donné
    private static final String PASSWORD = "haendel";

    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new SQLException("Erreur de connexion à la base de données.", e);
        }
    }
}
