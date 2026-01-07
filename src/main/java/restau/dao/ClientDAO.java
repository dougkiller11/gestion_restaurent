package restau.dao;

import restau.model.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

    public List<Client> listAll() {
        List<Client> out = new ArrayList<>();
        String sql = "SELECT id, nom, prenom, username, password, phone, address, role, statut, actif FROM clients ORDER BY role DESC, id ASC";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Client c = new Client(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("username"),
                        rs.getString("password")
                );
                c.setPhone(rs.getString("phone"));
                c.setAddress(rs.getString("address"));
                c.setRole(rs.getString("role"));
                c.setStatut(rs.getString("statut"));
                c.setActif(rs.getBoolean("actif"));
                out.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return out;
    }

    public Client add(String nom, String prenom, String username, String password, String phone, String address, String role, String statut, boolean actif) throws SQLException {
        String sql = "INSERT INTO clients (nom, prenom, username, password, phone, address, role, statut, actif) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nom);
            ps.setString(2, prenom);
            ps.setString(3, username);
            ps.setString(4, password);
            ps.setString(5, phone);
            ps.setString(6, address);
            ps.setString(7, role);
            ps.setString(8, statut);
            ps.setBoolean(9, actif);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    Client c = new Client(id, nom, prenom, username, password);
                    c.setPhone(phone);
                    c.setAddress(address);
                    c.setRole(role);
                    c.setStatut(statut);
                    c.setActif(actif);
                    return c;
                }
            }
        }
        return null;
    }

    public void update(int id, String nom, String prenom, String username, String password, String phone, String address, String role, String statut, boolean actif) throws SQLException {
        String sql = "UPDATE clients SET nom = ?, prenom = ?, username = ?, password = ?, phone = ?, address = ?, role = ?, statut = ?, actif = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nom);
            ps.setString(2, prenom);
            ps.setString(3, username);
            ps.setString(4, password);
            ps.setString(5, phone);
            ps.setString(6, address);
            ps.setString(7, role);
            ps.setString(8, statut);
            ps.setBoolean(9, actif);
            ps.setInt(10, id);
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM clients WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}

