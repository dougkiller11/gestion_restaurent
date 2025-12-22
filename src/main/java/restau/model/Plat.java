package restau.model;

import java.util.List;

public class Plat {
    private int id;
    private String nom;
    private String categorie;
    private List<String> ingredient;
    private double prix;
    private boolean disponible;

    public Plat(int id, String nom, String categorie, List<String> ingredient, double prix, boolean disponible) {
        this.id = id;
        this.nom = nom;
        this.categorie = categorie;
        this.ingredient = ingredient;
        this.prix = prix;
        this.disponible = disponible;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }
    public List<String> getIngredient() { return ingredient; }
    public void setIngredient(List<String> ingredient) { this.ingredient = ingredient; }
    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
}

