package restau.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Gestion simple de l'authentification et de l'inscription.
 * (mots de passe en clair pour la maquette uniquement).
 */
public class AuthDAO {

    public static class AuthResult {
        public final boolean success;
        public final String role;
        public final int userId;
        public final boolean client;
        public final String displayName;

        public AuthResult(boolean success, String role, int userId, boolean client, String displayName) {
            this.success = success;
            this.role = role;
            this.userId = userId;
            this.client = client;
            this.displayName = displayName;
        }

        public static AuthResult failure() {
            return new AuthResult(false, null, -1, false, null);
        }
    }

    public boolean registerClient(String nom, String prenom, String username, String password) {
        String sql = "INSERT INTO clients (nom, prenom, username, password) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nom);
            ps.setString(2, prenom);
            ps.setString(3, username);
            ps.setString(4, password);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public AuthResult authenticate(String username, String password) {
        String empSql = "SELECT id, nom, prenom, role FROM employes WHERE username = ? AND password = ? AND actif = true";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(empSql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new AuthResult(true, rs.getString("role"), rs.getInt("id"), false,
                            rs.getString("nom") + " " + rs.getString("prenom"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return AuthResult.failure();
        }

        String cliSql = "SELECT id, nom, prenom FROM clients WHERE username = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(cliSql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new AuthResult(true, "client", rs.getInt("id"), true,
                            rs.getString("nom") + " " + rs.getString("prenom"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return AuthResult.failure();
        }

        return AuthResult.failure();
    }
}

