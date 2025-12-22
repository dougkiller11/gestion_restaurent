package restau.dao;

import restau.dao.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfileDAO {

    public static class ProfileData {
        public String nom;
        public String prenom;
        public String email;
        public String phone;
        public String address;
        public String avatarUrl;
    }

    public ProfileData load() {
        ProfileData p = new ProfileData();
        String sql = "SELECT nom, prenom, email, phone, address, avatar_url FROM user_profile WHERE id = 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                p.nom = nvl(rs.getString("nom"));
                p.prenom = nvl(rs.getString("prenom"));
                p.email = nvl(rs.getString("email"));
                p.phone = nvl(rs.getString("phone"));
                p.address = nvl(rs.getString("address"));
                p.avatarUrl = nvl(rs.getString("avatar_url"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return p;
    }

    public void save(ProfileData p) {
        String sql = """
            UPDATE user_profile
            SET nom = ?, prenom = ?, email = ?, phone = ?, address = ?, avatar_url = ?
            WHERE id = 1
            """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.nom);
            ps.setString(2, p.prenom);
            ps.setString(3, p.email);
            ps.setString(4, p.phone);
            ps.setString(5, p.address);
            ps.setString(6, p.avatarUrl);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String nvl(String s) {
        return s == null ? "" : s;
    }
}

