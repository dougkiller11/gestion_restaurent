package restau.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DriverManager;

public class DBConnection {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/restau_db?createDatabaseIfNotExist=true&serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private static volatile boolean initialized = false;

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        ensureInitialized(conn);
        return conn;
    }
    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            if (conn != null) {
                System.out.println("✅ Connected to MySQL!");
                createTables(conn);
                System.out.println("✅ All tables are ready.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static synchronized void ensureInitialized(Connection conn) throws SQLException {
        if (initialized) return;
        createTables(conn);
        initialized = true;
    }

    private static void createTables(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();

        stmt.execute("""
            CREATE TABLE IF NOT EXISTS employes (
                id INT AUTO_INCREMENT PRIMARY KEY,
                nom VARCHAR(100),
                prenom VARCHAR(100),
                role VARCHAR(50),
                username VARCHAR(50) UNIQUE,
                password VARCHAR(255),
                actif BOOLEAN
            )
        """);

        stmt.execute("""
            CREATE TABLE IF NOT EXISTS admins (
                id INT AUTO_INCREMENT PRIMARY KEY,
                nom VARCHAR(100),
                prenom VARCHAR(100),
                username VARCHAR(50) UNIQUE,
                password VARCHAR(255),
                email VARCHAR(150),
                phone VARCHAR(50)
            )
        """);

        stmt.execute("""
            CREATE TABLE IF NOT EXISTS clients (
                id INT AUTO_INCREMENT PRIMARY KEY,
                nom VARCHAR(100),
                prenom VARCHAR(100),
                username VARCHAR(50) UNIQUE,
                password VARCHAR(255),
                phone VARCHAR(50),
                address TEXT
            )
        """);

        try {
            stmt.execute("ALTER TABLE clients ADD COLUMN phone VARCHAR(50)");
        } catch (SQLException dup) {
            if (!"42S21".equals(dup.getSQLState()) && dup.getErrorCode() != 1060) throw dup;
        }
        try {
            stmt.execute("ALTER TABLE clients ADD COLUMN address TEXT");
        } catch (SQLException dup) {
            if (!"42S21".equals(dup.getSQLState()) && dup.getErrorCode() != 1060) throw dup;
        }
        try {
            stmt.execute("ALTER TABLE clients ADD COLUMN role VARCHAR(50) DEFAULT 'client'");
        } catch (SQLException dup) {
            if (!"42S21".equals(dup.getSQLState()) && dup.getErrorCode() != 1060) throw dup;
        }
        try {
            stmt.execute("ALTER TABLE clients ADD COLUMN statut VARCHAR(50)");
        } catch (SQLException dup) {
            if (!"42S21".equals(dup.getSQLState()) && dup.getErrorCode() != 1060) throw dup;
        }
        try {
            stmt.execute("ALTER TABLE clients ADD COLUMN actif BOOLEAN DEFAULT TRUE");
        } catch (SQLException dup) {
            if (!"42S21".equals(dup.getSQLState()) && dup.getErrorCode() != 1060) throw dup;
        }

        stmt.execute("""
            CREATE TABLE IF NOT EXISTS plats (
                id INT AUTO_INCREMENT PRIMARY KEY,
                nom VARCHAR(100),
                categorie VARCHAR(50),
                prix DOUBLE,
                image_url TEXT,
                description TEXT,
                disponible BOOLEAN,
                reclamations INT DEFAULT 0
            )
        """);

        try {
            stmt.execute("ALTER TABLE plats ADD COLUMN image_url TEXT");
        } catch (SQLException dup) {
            if (!"42S21".equals(dup.getSQLState()) && dup.getErrorCode() != 1060) throw dup;
        }
        try {
            stmt.execute("ALTER TABLE plats MODIFY COLUMN image_url TEXT");
        } catch (SQLException ignore) {
            // ignore if already large enough / not supported
        }
        try {
            stmt.execute("ALTER TABLE plats ADD COLUMN reclamations INT DEFAULT 0");
        } catch (SQLException dup) {
            if (!"42S21".equals(dup.getSQLState()) && dup.getErrorCode() != 1060) throw dup;
        }
        try {
            stmt.execute("ALTER TABLE plats ADD COLUMN description TEXT");
        } catch (SQLException dup) {
            if (!"42S21".equals(dup.getSQLState()) && dup.getErrorCode() != 1060) throw dup;
        }

        stmt.execute("""
            CREATE TABLE IF NOT EXISTS commandes (
                id INT AUTO_INCREMENT PRIMARY KEY,
                date DATETIME DEFAULT CURRENT_TIMESTAMP,
                status VARCHAR(50),
                total DOUBLE,
                client_id INT,
                employe_id INT,
                FOREIGN KEY (client_id) REFERENCES clients(id),
                FOREIGN KEY (employe_id) REFERENCES employes(id)
            )
        """);

        stmt.execute("""
            CREATE TABLE IF NOT EXISTS commande_plats (
                commande_id INT,
                plat_id INT,
                FOREIGN KEY (commande_id) REFERENCES commandes(id),
                FOREIGN KEY (plat_id) REFERENCES plats(id)
            )
        """);

        stmt.execute("""
            CREATE TABLE IF NOT EXISTS paiments (
                id INT AUTO_INCREMENT PRIMARY KEY,
                nom VARCHAR(100),
                command_id INT,
                mode VARCHAR(50),
                total DOUBLE,
                date DATETIME,
                statut VARCHAR(50),
                FOREIGN KEY (command_id) REFERENCES commandes(id)
            )
        """);

        stmt.execute("""
            CREATE TABLE IF NOT EXISTS ingredients (
                id INT AUTO_INCREMENT PRIMARY KEY,
                plat_id INT,
                nom_ingredient VARCHAR(100),
                FOREIGN KEY (plat_id) REFERENCES plats(id)
            )
        """);

        stmt.execute("""
            CREATE TABLE IF NOT EXISTS catalogue_ingredients (
                id INT AUTO_INCREMENT PRIMARY KEY,
                nom VARCHAR(100) UNIQUE,
                is_available BOOLEAN DEFAULT TRUE
            )
        """);

        stmt.executeUpdate("""
            INSERT IGNORE INTO catalogue_ingredients (nom, is_available) VALUES
            ('Tomate', TRUE),
            ('Fromage', TRUE),
            ('Oignon', TRUE),
            ('Pain', TRUE),
            ('Salade', TRUE),
            ('Poulet', TRUE),
            ('Boeuf', TRUE),
            ('Poisson', TRUE),
            ('Riz', TRUE),
            ('Pates', TRUE),
            ('Sauce Tomate', TRUE),
            ('Creme Fraiche', TRUE),
            ('Mozzarella', TRUE),
            ('Champignon', TRUE)
        """);

        stmt.execute("""
            CREATE TABLE IF NOT EXISTS user_profile (
                id INT PRIMARY KEY,
                nom VARCHAR(100),
                prenom VARCHAR(100),
                email VARCHAR(150),
                phone VARCHAR(50),
                address TEXT,
                avatar_url VARCHAR(500)
            )
        """);

        stmt.executeUpdate("""
            INSERT IGNORE INTO user_profile (id, nom, prenom, email, phone, address, avatar_url)
            VALUES (1, 'Admin', 'Root', 'admin@example.com', '', '', '')
        """);

        stmt.executeUpdate("""
            INSERT IGNORE INTO admins (nom, prenom, username, password, email, phone)
            VALUES ('Admin', 'Root', 'admin', 'admin', 'admin@example.com', '')
        """);
    }
}

