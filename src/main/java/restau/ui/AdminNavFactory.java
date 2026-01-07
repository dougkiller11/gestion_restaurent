package restau.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminNavFactory {

    public static VBox create(Stage stage) {
        Label hello = new Label("Hello, Admin");
        hello.setStyle("-fx-text-fill: #e9edf4; -fx-font-size: 14px; -fx-font-weight: bold;");

        Label avatar = new Label("👩");
        avatar.setStyle("-fx-background-color: #f0c14b; -fx-text-fill: #1f1f1f; -fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 10px; -fx-background-radius: 50%;");

        VBox header = new VBox(6, hello, avatar);
        header.setAlignment(Pos.CENTER);

        Button profile = createMenuButton("Profil");
        Button dashboard = createMenuButton("Dashboard");
        Button history = createMenuButton("Historique plats");
        Button settings = createMenuButton("Paramètres");
        Button logout = createMenuButton("Se déconnecter");

        profile.setOnAction(e -> new ProfileWindow().show(stage));
        dashboard.setOnAction(e -> new DashboardWindow().show(stage));
        history.setOnAction(e -> new AdminAuditWindow().show(stage));
        settings.setOnAction(e -> new MainWindow().show(stage));
        logout.setOnAction(e -> new LoginWindow().show(stage));

        VBox card = new VBox(12, header, profile, dashboard, history, settings, logout);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(16));
        card.setPrefWidth(200);
        card.setStyle("-fx-background-color: rgba(17,24,33,0.95); -fx-background-radius: 12px; -fx-border-color: #2f3745; -fx-border-radius: 12px;");
        return card;
    }

    private static Button createMenuButton(String text) {
        String base = "-fx-background-color: #1b2431; -fx-text-fill: #e9edf4; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-border-color: #2f3745; -fx-border-radius: 8px;";
        String hover = "-fx-background-color: #253041; -fx-text-fill: #f5c242; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-border-color: #374458; -fx-border-radius: 8px;";
        Button b = new Button(text);
        b.setStyle(base);
        b.setAlignment(Pos.CENTER_LEFT);
        b.setMinHeight(34);
        b.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(b, javafx.scene.layout.Priority.ALWAYS);
        b.setOnMouseEntered(evt -> b.setStyle(hover));
        b.setOnMouseExited(evt -> b.setStyle(base));
        return b;
    }
}


