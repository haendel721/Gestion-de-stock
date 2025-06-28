import java.sql.*;

// Service pour gérer les utilisateurs (inscription et authentification)
public class UtilisateurService {

    // Méthode pour inscrire un nouvel utilisateur
    public static boolean inscrire(String nomUtilisateur, String motDePasse, String role) {
        // Hachage du mot de passe avant de le stocker dans la base de données
        String motDePasseHache = PasswordUtil.hashPassword(motDePasse);

        // Requête SQL d'insertion
        String sql = "INSERT INTO utilisateurs (nom_utilisateur, mot_de_passe, role) VALUES (?, ?, ?)";

        // Bloc try-with-resources : connexion et préparation de la requête
        try (Connection conn = DatabaseConnexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Insertion des valeurs dans la requête préparée
            stmt.setString(1, nomUtilisateur);
            stmt.setString(2, motDePasseHache); // mot de passe déjà haché
            stmt.setString(3, role);

            // Exécution de la requête
            stmt.executeUpdate();
            return true; // inscription réussie

        } catch (SQLIntegrityConstraintViolationException e) {
            // Erreur si le nom d'utilisateur est déjà utilisé (clé unique)
            System.out.println("Nom d'utilisateur déjà utilisé !");
            return false;

        } catch (SQLException e) {
            // Autre erreur SQL
            e.printStackTrace();
            return false;
        }
    }

    // Méthode pour authentifier un utilisateur et retourner ses informations
    public static Utilisateur authentifier(String username, String password) {
        try (Connection conn = DatabaseConnexion.getConnection()) {

            // Hachage du mot de passe fourni
            String hashedPassword = PasswordUtil.hashPassword(password);

            // Requête SQL
            String sql = "SELECT * FROM utilisateurs WHERE nom_utilisateur = ? AND mot_de_passe = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Utilisateur user = new Utilisateur();
                user.setId(rs.getInt("id_utilisateur"));
                user.setNom(rs.getString("nom_utilisateur"));
                user.setMotDePasse(rs.getString("mot_de_passe"));
                user.setRole(rs.getString("role"));
                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


}