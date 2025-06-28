// Classe représentant un utilisateur du système
public class Utilisateur {

    // Identifiant unique de l'utilisateur (clé primaire dans la base)
    private int id;

    // Nom ou nom d'utilisateur (login)
    private String nom;

    // Adresse email (facultatif si non utilisée dans le login)
    private String email;

    // Mot de passe (en général stocké sous forme hachée)
    private String motDePasse;

    // Rôle de l'utilisateur : "admin" ou "utilisateur"
    private String role;

    // Constructeur par défaut (obligatoire si tu veux instancier vide)
    public Utilisateur() {
    }

    // Constructeur avec paramètres (facultatif)
    public Utilisateur(int id, String nom, String email, String motDePasse, String role) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.role = role;
    }

    // Getters et setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Pour affichage ou débogage si besoin
    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

}
