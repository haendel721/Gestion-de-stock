public class Utilisateur {
    private String nomUtilisateur;
    private String role;

    public Utilisateur(String nomUtilisateur, String role) {
        this.nomUtilisateur = nomUtilisateur;
        this.role = role;
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public String getRole() {
        return role;
    }
}
