import javax.swing.*;
import java.awt.*;

public class AccueilUI extends JFrame {

    public AccueilUI(String nomUtilisateur) {
        setTitle("Bienvenue");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel label = new JLabel("Bienvenue, " + nomUtilisateur + " !");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 22));
        add(label, BorderLayout.CENTER);

        setVisible(true);

        // Timer pour 5 secondes, puis redirection
        Timer timer = new Timer(5000, e -> {
            dispose(); // Fermer la fenêtre actuelle
            index.main(null); // Lancer la fenêtre principale de l'application
        });
        timer.setRepeats(false); // Exécuter une seule fois
        timer.start();
    }

    // Point d’entrée (optionnel, si tu veux tester seul)
    public static void main(String[] args) {
        new AccueilUI("Admin"); // Tu peux remplacer "Admin" par le nom saisi depuis une fenêtre de login
    }
}
