import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import java.sql.*;

public class index extends JFrame {
    private Utilisateur utilisateur;
    // DefaultTableModel permet de gérer dynamiquement les données du JTable, sans avoir à redéfinir tout le tableau.
    private static DefaultTableModel model; // Modèle de table pour stocker les données des produits
    private static Connection conn; // Connexion à la base de données
    // DefaultTableModel permet de gérer dynamiquement les données du JTable, sans avoir à redéfinir tout le tableau.
    public index(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;

        try {
            // Établir la connexion à la base de données
            conn = DatabaseConnexion.getConnection();

            // Configuration de la fenêtre principale
            setTitle("Gestion des Stocks");
            setSize(1000, 700);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null); // Centrage de la fenêtre

            // Titre de l'application
            JLabel titre = new JLabel("Gestion des stocks", SwingConstants.CENTER);
            titre.setFont(new Font("Arial", Font.BOLD, 40));

            // Création du bouton "Ajouter"
            JButton ajouter = new JButton("Ajouter");
            ajouter.setFont(new Font("Arial", Font.BOLD, 20));

            // Zone de recherche + bouton "Rechercher"
            JTextField recherche = new JTextField(10);
            JButton rechercher = new JButton("Rechercher");

            // Panneau pour regrouper la zone de recherche et le bouton
            JPanel panerRechercheEnd = new JPanel(new BorderLayout());
            panerRechercheEnd.add(recherche, BorderLayout.CENTER);
            panerRechercheEnd.add(rechercher, BorderLayout.EAST);

            JPanel panelRechercher = new JPanel(new BorderLayout());
            panelRechercher.add(ajouter, BorderLayout.LINE_START);
            panelRechercher.add(panerRechercheEnd, BorderLayout.EAST);

            // Action de recherche d'un produit par nom
            rechercher.addActionListener(e -> {
                String sqlNom = "SELECT * FROM produits WHERE nomProduit = ?";
                try (PreparedStatement ppstt = conn.prepareStatement(sqlNom)) {
                    ppstt.setString(1, recherche.getText());
                    try (ResultSet rs = ppstt.executeQuery()) {
                        if (!rs.isBeforeFirst()) {
                            JOptionPane.showMessageDialog(null, "Le produit n'existe pas !");
                        } else {
                            while (rs.next()) {
                                JOptionPane.showMessageDialog(null,
                                        "Produit : " + rs.getString("nomProduit") +
                                                "\nPrix : " + rs.getDouble("prix") +
                                                "\nQuantité : " + rs.getInt("quantite")
                                );
                            }
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });

            // Panneau du haut contenant le titre et la barre de recherche
            JPanel panelHaut = new JPanel(new BorderLayout());
            panelHaut.add(titre, BorderLayout.CENTER);
            panelHaut.add(panelRechercher, BorderLayout.AFTER_LAST_LINE);

            // Définition des colonnes de la JTable
            String[] columnNames = {"Id", "Nom Produit", "Prix Unitaire (Ar)", "Catégorie",
                    "Fournisseur", "Date d'entrée", "Quantité en stock", "Action"};
            model = new DefaultTableModel(columnNames, 0);
            JTable table = new JTable(model);
            // Supprime la colonne "Action" si l'utilisateur n'est pas admin
            if (!"admin".equalsIgnoreCase(utilisateur.getRole())) {
                table.removeColumn(table.getColumn("Action"));
            }
            // Appliquer un rendu personnalisé pour les cellules selon la quantité en stock
            for (int i = 0; i < table.getColumnCount(); i++) {
                table.getColumnModel().getColumn(i).setCellRenderer(new StockRenderer());
            }

            table.setRowHeight(40); // Hauteur des lignes
            table.setDefaultEditor(Object.class, null); // Empêche l'édition directe

            if ("admin".equalsIgnoreCase(utilisateur.getRole())){
                // Configuration des boutons dans la colonne "Action"
                table.getColumn("Action").setCellRenderer(new ButtonRenderer());
                table.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox()));
            }


            // ScrollPane pour contenir la table
            JScrollPane sp = new JScrollPane(table);

            // Pied de page
            JTextField perso = new JTextField("@copyright 2025 Haendel Abraham");

            // Panneau principal
            JPanel panelContent = new JPanel(new BorderLayout());
            panelContent.add(perso, BorderLayout.SOUTH);
            panelContent.add(panelHaut, BorderLayout.NORTH);
            panelContent.add(sp, BorderLayout.CENTER);

            // Ajout du panneau à la fenêtre principale
            add(panelContent);

            // Chargement initial des produits
            chargerProduits();

            // Affiche ou cache le bouton "Ajouter" selon le rôle de l'utilisateur
            if ("admin".equalsIgnoreCase(utilisateur.getRole())) {
                ajouter.addActionListener(e -> ajouterProduit());
            } else {
                ajouter.setVisible(false);
            }

            // Afficher la fenêtre
            setVisible(true);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erreur de connexion à la base de données !");
        }
    }
    public static void main(String[] args) {
    }
    // Méthode pour charger les produits depuis la base de données
    private static void chargerProduits() {
        model.setRowCount(0); // Vide la table avant de la remplir afin d'eviter les repetition des produits
        try (Statement stmt = conn.createStatement(); // statement : objet pour pouvoir utiliser des requêtes
             ResultSet rs = stmt.executeQuery("SELECT * FROM produits ORDER BY id")) {
            //resultset : objet qui stock des éléments du tableau
            while (rs.next()) { // rs.next : connaitre s'il y a encore de ligne suivant
                // Ajout des données de chaque produit dans la table
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("nomProduit"),
                        rs.getDouble("prix"),
                        rs.getString("categorie"),
                        rs.getString("fournisseur"),
                        rs.getDate("date_entree"),
                        rs.getInt("quantite"),
                        "Actions"
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static class ButtonRenderer extends JPanel implements javax.swing.table.TableCellRenderer {
        private final JButton btnModifier = new JButton(resizeIcon("/img/modifier-les-informations.png", 20, 20));
        private final JButton btnSupprimer = new JButton(resizeIcon("/img/supprimer.png", 20, 20));

        private ImageIcon resizeIcon(String resourcePath, int width, int height) {
            ImageIcon icon = new ImageIcon(getClass().getResource(resourcePath));
            Image img = icon.getImage();
            Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(resizedImg);
        }



        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER));
            if ("admin".equals(session.roleConnecte)) {
                add(btnModifier);
                add(btnSupprimer);
            }
        }


        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            return this;
        }
    }
    static class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private final JPanel panel = new JPanel();
        private final JButton btnModifier = new JButton("Modifier");
        private final JButton btnSupprimer = new JButton("Supprimer");
        private int currentRow;

        public ButtonEditor(JCheckBox jCheckBox) {
            if ("admin".equals(session.roleConnecte)) {
                btnModifier.addActionListener(e -> {
                    modifierProduit(currentRow);
                    fireEditingStopped();
                });

                btnSupprimer.addActionListener(e -> {
                    supprimerProduit(currentRow);
                    fireEditingStopped();
                });

                panel.add(btnModifier);
                panel.add(btnSupprimer);
            }
        }


        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            currentRow = row;
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }
    }

    // Méthode pour modifier un produit
    private static void modifierProduit(int row) {
        int idProduit = (int) model.getValueAt(row, 0);

        JTextField nomField = new JTextField((String) model.getValueAt(row, 1));
        JTextField prixField = new JTextField(String.valueOf(model.getValueAt(row, 2)));
        JTextField categorieField = new JTextField((String) model.getValueAt(row, 3));
        JTextField fournisseurField = new JTextField((String) model.getValueAt(row, 4));

        // Création du spinner pour la quantité
        SpinnerNumberModel quantiteModel = new SpinnerNumberModel(
                (int) model.getValueAt(row, 6), // Valeur actuelle
                0, 1000, 1
        );
        JSpinner quantiteSpinner = new JSpinner(quantiteModel);

        JPanel panel = new JPanel(new GridLayout(5, 1));
        panel.add(new JLabel("Nom du produit:"));
        panel.add(nomField);
        panel.add(new JLabel("Prix unitaire:"));
        panel.add(prixField);
        panel.add(new JLabel("Catégorie:"));
        panel.add(categorieField);
        panel.add(new JLabel("Fournisseur:"));
        panel.add(fournisseurField);
        panel.add(new JLabel("Quantité en stock:"));
        panel.add(quantiteSpinner); // Utilisation du JSpinner

        int result = JOptionPane.showConfirmDialog(null, panel, "Modification du Produit avec l'Id° " + idProduit,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String sql = "UPDATE produits SET nomProduit=?, prix=?, categorie=?, fournisseur=?, quantite=? WHERE id=?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, nomField.getText());
                pstmt.setDouble(2, Double.parseDouble(prixField.getText()));
                pstmt.setString(3, categorieField.getText());
                pstmt.setString(4, fournisseurField.getText());
                pstmt.setInt(5, (Integer) quantiteSpinner.getValue()); // Récupération de la valeur du spinner
                pstmt.setInt(6, idProduit);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Produit modifié avec succès !");
                chargerProduits();
            } catch (SQLException | NumberFormatException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erreur lors de la modification !");
            }
        }
    }

    private static void supprimerProduit(int row) {
        int idProduit = (int) model.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(null,
                "Voulez-vous vraiment supprimer ce produit ?", "Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                PreparedStatement pstmt = conn.prepareStatement("DELETE FROM produits WHERE id = ?");
                pstmt.setInt(1, idProduit);
                pstmt.executeUpdate();
                chargerProduits(); // Rafraîchit la table
                JOptionPane.showMessageDialog(null, "Produit supprimé avec succès !");
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erreur lors de la suppression !");
            }
        }
    }



    // Méthode pour ajouter un nouveau produit
    private static void ajouterProduit() {
        JTextField nomField = new JTextField();
        JTextField prixField = new JTextField();
        JTextField categorieField = new JTextField();
        JTextField fournisseurField = new JTextField();

        // Création du spinner pour la quantité
        SpinnerNumberModel quantiteModel = new SpinnerNumberModel(0, 0, 1000, 1);
        JSpinner quantiteSpinner = new JSpinner(quantiteModel);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Nom du produit:"));
        panel.add(nomField);
        panel.add(new JLabel("Prix unitaire:"));
        panel.add(prixField);
        panel.add(new JLabel("Catégorie:"));
        panel.add(categorieField);
        panel.add(new JLabel("Fournisseur:"));
        panel.add(fournisseurField);
        panel.add(new JLabel("Quantité en stock:"));
        panel.add(quantiteSpinner); // Utilisation du JSpinner

        int result = JOptionPane.showConfirmDialog(null, panel, "Ajouter un produit",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String sql = "INSERT INTO produits (nomProduit, prix, categorie, fournisseur, quantite) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, nomField.getText());
                pstmt.setDouble(2, Double.parseDouble(prixField.getText()));
                pstmt.setString(3, categorieField.getText());
                pstmt.setString(4, fournisseurField.getText());
                pstmt.setInt(5, (Integer) quantiteSpinner.getValue()); // Récupération de la valeur du spinner

                pstmt.executeUpdate();
                chargerProduits(); // Rafraîchir la table après ajout

            } catch (SQLException | NumberFormatException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout !");
            }
        }
    }
    // Classe personnalisée pour modifier la couleur de fond des lignes en fonction de la quantité en stock
    static class StockRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            // Appelle la méthode parent pour obtenir le composant standard
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            // Récupérer la quantité en stock à partir du modèle de table
            int quantite = (int) table.getModel().getValueAt(row, 6); // Colonne 6 = Quantité en stock

            // Si la quantité est inférieure ou égale à 10, la ligne devient jaune
            if (quantite >= 6 && quantite <= 10 ) {
                c.setBackground(Color.YELLOW);
            } else if (quantite<=5) {
                c.setBackground(Color.red);
                c.setFont(new Font("Arial", Font.BOLD, 12));
            } else {
                c.setBackground(Color.WHITE); // Sinon, la couleur de fond reste blanche
            }

            return c; // Retourne le composant avec la couleur mise à jour
        }
    }


}