import javax.swing.*; // Bibliothèque pour l'interface graphique
import java.awt.*; // Pour la gestion de la disposition des composants
import java.awt.event.*; // Pour la gestion des événements

// Classe principale qui hérite de JFrame pour créer une interface graphique
public class LoginUI extends JFrame {

    // Champs de connexion
    private JTextField loginUsername; // Champ de texte pour le nom d'utilisateur (connexion)
    private JPasswordField loginPassword; // Champ de mot de passe (connexion)

    // Champs d'inscription
    private JTextField registerUsername; // Champ de texte pour le nom d'utilisateur (inscription)
    private JPasswordField registerPassword; // Champ de mot de passe (inscription)
    private JComboBox<String> roleComboBox; // Menu déroulant pour choisir le rôle (utilisateur/admin)

    // Constructeur de l'interface
    public LoginUI() {
        setTitle("Système d'Authentification"); // Titre de la fenêtre
        setSize(400, 350); // Dimensions de la fenêtre
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Ferme l'application quand la fenêtre est fermée
        setLocationRelativeTo(null); // Centre la fenêtre à l'écran
        setResizable(false); // Empêche le redimensionnement de la fenêtre

        // Création de l'onglet avec deux panneaux : Connexion et Inscription
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Connexion", createLoginPanel()); // Onglet Connexion
        tabbedPane.addTab("Inscription", createRegisterPanel()); // Onglet Inscription

        add(tabbedPane); // Ajoute les onglets à la fenêtre principale
        setVisible(true); // Rend la fenêtre visible
    }

    // Création du panneau de connexion
    private JPanel createLoginPanel() {
        // Panneau avec disposition en grille de 4 lignes et 1 colonne
        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); // Marge intérieure

        // Champs de saisie
        loginUsername = new JTextField();
        loginPassword = new JPasswordField();

        // Ajout des composants au panneau
        panel.add(new JLabel("Nom d'utilisateur :"));
        panel.add(loginUsername);
        panel.add(new JLabel("Mot de passe :"));
        panel.add(loginPassword);

        // Bouton de connexion
        JButton loginButton = new JButton("Se connecter");
        loginButton.addActionListener(e -> handleLogin()); // Action à effectuer lors du clic

        // Conteneur global pour panneau + bouton
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(panel, BorderLayout.CENTER);
        wrapper.add(loginButton, BorderLayout.SOUTH); // Positionne le bouton en bas

        return wrapper;
    }

    // Création du panneau d’inscription
    private JPanel createRegisterPanel() {
        // Panneau avec disposition en grille
        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); // Marge intérieure

        // Champs de saisie
        registerUsername = new JTextField();
        registerPassword = new JPasswordField();

        // Menu déroulant pour le rôle
        roleComboBox = new JComboBox<>(new String[] { "utilisateur", "admin" });

        // Ajout des composants au panneau
        panel.add(new JLabel("Nom d'utilisateur :"));
        panel.add(registerUsername);
        panel.add(new JLabel("Mot de passe :"));
        panel.add(registerPassword);
        panel.add(new JLabel("Rôle :"));
        panel.add(roleComboBox);

        // Bouton d’inscription
        JButton registerButton = new JButton("S'inscrire");
        registerButton.addActionListener(e -> handleRegister()); // Action au clic

        // Conteneur global pour panneau + bouton
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(panel, BorderLayout.CENTER);
        wrapper.add(registerButton, BorderLayout.SOUTH);

        return wrapper;
    }

    // Méthode de traitement de la connexion
    private void handleLogin() {
        // Récupération des valeurs saisies
        String username = loginUsername.getText();
        String password = new String(loginPassword.getPassword());

        // Appel du service d'authentification
        boolean success = UtilisateurService.authentifier(username, password);

        if (success) {
            // Si authentification réussie
            JOptionPane.showMessageDialog(this, "Connexion réussie !");
            dispose(); // Ferme la fenêtre de connexion

            // Ouvre la fenêtre d’accueil avec le nom de l'utilisateur
            AccueilUI accueil = new AccueilUI(loginUsername.getText());
            accueil.setVisible(true);
        } else {
            // Si échec
            JOptionPane.showMessageDialog(this, "Identifiants incorrects.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Méthode de traitement de l’inscription
    private void handleRegister() {
        // Récupération des valeurs saisies
        String username = registerUsername.getText();
        String password = new String(registerPassword.getPassword());
        String role = (String) roleComboBox.getSelectedItem();

        // Appel du service d’inscription
        boolean inscrit = UtilisateurService.inscrire(username, password, role);

        if (inscrit) {
            // Si inscription réussie
            JOptionPane.showMessageDialog(this, "Inscription réussie !");
        } else {
            // Si échec (par exemple, nom d'utilisateur déjà utilisé)
            JOptionPane.showMessageDialog(this, "Erreur lors de l'inscription (nom déjà utilisé ?)", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Point d’entrée du programme
    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginUI::new); // Lancer l’interface graphique dans le thread principal
    }
}
