package restau.model;

import java.sql.Timestamp;
import java.util.Date;

public class Paiment {
    private int id;
    private String nom;
    private int commandId;
    private String mode;
    private double total;
    private Timestamp date;
    private String statut;

    public Paiment(int id, String nom, int commandId, String mode, double total, Timestamp date, String statut) {
        this.id = id;
        this.nom = nom;
        this.commandId = commandId;
        this.mode = mode;
        this.total = total;
        this.date = date;
        this.statut = statut;
    }

    public Paiment(String nom, int commandId, String mode, double total, String statut) {
        this.nom = nom;
        this.commandId = commandId;
        this.mode = mode;
        this.total = total;
        this.statut = statut;
        this.date = new Timestamp(System.currentTimeMillis());
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public int getCommandId() { return commandId; }
    public void setCommandId(int commandId) { this.commandId = commandId; }
    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    public Date getDate() { return date; }
    public void setDate(Timestamp date) { this.date = date; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public String toString() {
        return this.id + "," + this.getNom() + "," + this.getCommandId() + "," + this.getMode() + "," + this.getTotal() + "," + this.getDate() + "," + getStatut();
    }
}

