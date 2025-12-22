package restau.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class CommandeDAO {
    public void createOrder(int clientId, int employeId, List<Integer> platIds, double total) {
        String orderSql = "INSERT INTO commandes (client_id, employe_id, status, total) VALUES (?, ?, 'En cours', ?)";
        String itemSql = "INSERT INTO commande_plats (commande_id, plat_id) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            int orderId = -1;
            try (PreparedStatement pstmt = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, clientId);
                pstmt.setInt(2, employeId);
                pstmt.setDouble(3, total);
                pstmt.executeUpdate();

                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) orderId = rs.getInt(1);
            }

            try (PreparedStatement itemStmt = conn.prepareStatement(itemSql)) {
                for (Integer platId : platIds) {
                    itemStmt.setInt(1, orderId);
                    itemStmt.setInt(2, platId);
                    itemStmt.addBatch();
                }
                itemStmt.executeBatch();
            }

            conn.commit();
            System.out.println("✅ Order placed successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

