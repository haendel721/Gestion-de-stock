import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

// Classe utilitaire pour le hachage des mots de passe
public class PasswordUtil {

    // Méthode pour hacher un mot de passe en utilisant SHA-256
    public static String hashPassword(String password) {
        try {
            // Création d'une instance de l'algorithme SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Transformation du mot de passe en tableau de bytes, puis hachage
            byte[] hash = md.digest(password.getBytes());

            // Conversion des bytes en chaîne hexadécimale lisible
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                // Formatage des bytes en hexadécimal sur 2 chiffres
                sb.append(String.format("%02x", b));
            }

            // Retourne la chaîne de caractères hachée
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // Exception si l'algorithme SHA-256 n'est pas supporté sur la machine
            throw new RuntimeException("Erreur de hachage", e);
        }
    }
}
