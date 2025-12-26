package restau.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import restau.model.Plat;

public class PlatDAO {
    public void addPlat(String nom, String categorie, double prix, boolean disponible, String imageUrl, String description) {
        String sql = "INSERT INTO plats (nom, categorie, prix, disponible, image_url, description) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nom);
            pstmt.setString(2, categorie);
            pstmt.setDouble(3, prix);
            pstmt.setBoolean(4, disponible);
            pstmt.setString(5, imageUrl);
            pstmt.setString(6, description);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Plat> listAvailable() {
        List<Plat> plats = new ArrayList<>();
        String sql = "SELECT id, nom, categorie, prix, disponible, image_url, description FROM plats WHERE disponible = true";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Plat p = new Plat(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("categorie"),
                        rs.getDouble("prix"),
                        rs.getBoolean("disponible"),
                        rs.getString("image_url"),
                        rs.getString("description")
                );
                plats.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plats;
    }
}

