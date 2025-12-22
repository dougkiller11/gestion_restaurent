package restau.dao;

import restau.model.Paiment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaimentDAO {

    public boolean createPaiment(Paiment p) {
        String sql = "INSERT INTO paiments (nom, command_id, mode, total, date, statut) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, p.getNom());
            pstmt.setInt(2, p.getCommandId());
            pstmt.setString(3, p.getMode());
            pstmt.setDouble(4, p.getTotal());
            pstmt.setTimestamp(5, (Timestamp) p.getDate());
            pstmt.setString(6, p.getStatut());

            int rows = pstmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Paiment> getAllPaiments() {
        List<Paiment> list = new ArrayList<>();
        String sql = "SELECT * FROM paiments";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Paiment p = new Paiment(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getInt("command_id"),
                        rs.getString("mode"),
                        rs.getDouble("total"),
                        rs.getTimestamp("date"),
                        rs.getString("statut")
                );
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void updateStatut(int id, String newStatut) {
        String sql = "UPDATE paiments SET statut = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newStatut);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

