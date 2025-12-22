package restau.model;

public class Employe {
    private int id;
    private String nom;
    private String prenom;
    private String role;
    private String username;
    private String password;
    private boolean actif;

    public Employe(int id, String nom, String prenom, String role, String username, String password, boolean actif) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.role = role;
        this.username = username;
        this.password = password;
        this.actif = actif;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public boolean isActif() { return actif; }
    public void setActif(boolean actif) { this.actif = actif; }
}

