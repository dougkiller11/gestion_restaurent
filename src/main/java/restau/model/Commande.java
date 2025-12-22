package restau.model;

import java.util.Date;
import java.util.List;

public class Commande {
    private int id;
    private Date date;
    private List<Plat> plats;
    private String status;
    private double total;
    private int employeId;

    public Commande(int id, Date date, List<Plat> plats, String status, double total, int employeId) {
        this.id = id;
        this.date = date;
        this.plats = plats;
        this.status = status;
        this.total = total;
        this.employeId = employeId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    public List<Plat> getPlats() { return plats; }
    public void setPlats(List<Plat> plats) { this.plats = plats; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    public int getEmployeId() { return employeId; }
    public void setEmployeId(int employeId) { this.employeId = employeId; }

    public void ajouterPlat(Plat p) {
        List<Plat> list = this.getPlats();
        list.add(p);
        this.setPlats(list);
    }

    public void supprimerPlat(Plat p) {
        List<Plat> list = this.getPlats();
        list.remove(p);
        this.setPlats(list);
    }
}

