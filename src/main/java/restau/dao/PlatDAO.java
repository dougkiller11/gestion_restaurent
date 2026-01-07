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
    public int addPlat(String nom, String categorie, double prix, boolean disponible, String imageUrl, String description, List<String> ingredients) {
        String sql = "INSERT INTO plats (nom, categorie, prix, disponible, image_url, description) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, nom);
            pstmt.setString(2, categorie);
            pstmt.setDouble(3, prix);
            pstmt.setBoolean(4, disponible);
            pstmt.setString(5, imageUrl);
            pstmt.setString(6, description);
            pstmt.executeUpdate();

            int platId = -1;
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    platId = rs.getInt(1);
                }
            }

            if (platId > 0 && ingredients != null && !ingredients.isEmpty()) {
                String ingSql = "INSERT INTO ingredients (plat_id, nom_ingredient) VALUES (?, ?)";
                try (PreparedStatement ingStmt = conn.prepareStatement(ingSql)) {
                    for (String ing : ingredients) {
                        if (ing == null || ing.trim().isEmpty()) continue;
                        ingStmt.setInt(1, platId);
                        ingStmt.setString(2, ing.trim());
                        ingStmt.addBatch();
                    }
                    ingStmt.executeBatch();
                }
            }

            return platId;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public List<Plat> listAvailable() {
        List<Plat> plats = new ArrayList<>();
        String sql = "SELECT id, nom, categorie, prix, disponible, image_url, description, reclamations FROM plats WHERE disponible = true";
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
                        rs.getString("description"),
                        rs.getInt("reclamations")
                );
                p.setIngredient(fetchIngredients(conn, p.getId()));
                plats.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plats;
    }

    public List<Plat> listAll() {
        List<Plat> plats = new ArrayList<>();
        String sql = "SELECT id, nom, categorie, prix, disponible, image_url, description, reclamations FROM plats ORDER BY id ASC";
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
                        rs.getString("description"),
                        rs.getInt("reclamations")
                );
                p.setIngredient(fetchIngredients(conn, p.getId()));
                plats.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plats;
    }

    private List<String> fetchIngredients(Connection conn, int platId) {
        List<String> ingredients = new ArrayList<>();
        String sql = "SELECT nom_ingredient FROM ingredients WHERE plat_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, platId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ingredients.add(rs.getString("nom_ingredient"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ingredients;
    }

    public boolean deletePlat(int platId) {
        String delIng = "DELETE FROM ingredients WHERE plat_id = ?";
        String delPlat = "DELETE FROM plats WHERE id = ?";
        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement pi = conn.prepareStatement(delIng)) {
                pi.setInt(1, platId);
                pi.executeUpdate();
            }
            try (PreparedStatement pp = conn.prepareStatement(delPlat)) {
                pp.setInt(1, platId);
                return pp.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean incrementReclamation(int platId) {
        String sql = "UPDATE plats SET reclamations = reclamations + 1 WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, platId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePlat(int id, String nom, String categorie, double prix, boolean disponible, String imageUrl, String description) {
        String sql = "UPDATE plats SET nom = ?, categorie = ?, prix = ?, disponible = ?, image_url = ?, description = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nom);
            ps.setString(2, categorie);
            ps.setDouble(3, prix);
            ps.setBoolean(4, disponible);
            ps.setString(5, imageUrl);
            ps.setString(6, description);
            ps.setInt(7, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

