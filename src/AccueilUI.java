import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Fenêtre principale après connexion
public class AccueilUI extends JFrame {
    private Utilisateur utilisateur; // stocke l'utilisateur connecté

    public AccueilUI(Utilisateur utilisateur) {
        this.utilisateur = utilisateur; // garde une copie de l'utilisateur

        setTitle("Accueil - Gestion de stock");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Création de la barre de menu
        JMenuBar menuBar = new JMenuBar();

        // Menu toujours disponible : Stock
        JMenu menuStock = new JMenu("Stock");
        JMenuItem itemAfficherStock = new JMenuItem("Afficher les produits");
        menuStock.add(itemAfficherStock);
        menuBar.add(menuStock);

        // Action pour "Afficher les produits"
        itemAfficherStock.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new index(utilisateur); // ➜ ouverture correcte de l'index avec rôle
                dispose(); // ferme la fenêtre actuelle
            }
        });

        // Menu réservé à l'administrateur
        JMenu menuUtilisateurs = new JMenu("Utilisateurs");
        JMenuItem itemGererUtilisateurs = new JMenuItem("Gérer les utilisateurs");
        menuUtilisateurs.add(itemGererUtilisateurs);

        if ("admin".equals(utilisateur.getRole())) {
            menuBar.add(menuUtilisateurs);
        }

        setJMenuBar(menuBar);

        // Message de bienvenue
        JLabel bienvenue = new JLabel("Bienvenue, " + utilisateur.getNom(), SwingConstants.CENTER);
        bienvenue.setFont(new Font("Arial", Font.BOLD, 24));
        add(bienvenue, BorderLayout.CENTER);

        setVisible(true);
    }
}
