package restau.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
}

