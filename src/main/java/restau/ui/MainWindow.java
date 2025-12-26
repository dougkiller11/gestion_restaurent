package restau.ui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Priority;
import javafx.geometry.Pos;
import javafx.stage.Stage;
public class MainWindow {

    public void show(Stage stage) {
        String brand = "Arc Burger";

        Label title = new Label("Restaurant Manager");
        title.setStyle("-fx-text-fill: #f5c242; -fx-font-size: 22px; -fx-font-weight: bold;");

        Label subtitle = new Label(brand);
        subtitle.setStyle("-fx-text-fill: white; -fx-font-size: 30px; -fx-font-weight: bold;");

        Label tagline = new Label("Pilotez vos plats, commandes et clients en un clin d'œil");
        tagline.setStyle("-fx-text-fill: #d8dde6;");

        VBox headerBox = new VBox(6, title, subtitle, tagline);
        headerBox.setAlignment(Pos.CENTER);

        Button platsBtn = createPrimaryButton("Gérer les plats");
        platsBtn.setOnAction(e -> new PlatsWindow().show(stage));

        Button commandesBtn = createSecondaryButton("Commandes");
        Button clientsBtn = createPrimaryButton("Gérer les clients");
        clientsBtn.setOnAction(e -> new ClientsWindow().show(stage));

        VBox cardPlats = createCard("Plats", "Ajoutez, catégorisez, mettez à jour vos plats.", platsBtn);
        VBox cardCmd = createCard("Commandes", "Suivez le statut et les paiements.", commandesBtn);
        VBox cardCli = createCard("Clients", "Gérez vos clients et leur historique.", clientsBtn);

        HBox cards = new HBox(16, cardPlats, cardCmd, cardCli);
        cards.setAlignment(Pos.CENTER);
        cards.setPadding(new Insets(20, 0, 0, 0));

        VBox panel = new VBox(20, headerBox, cards);
        panel.setAlignment(Pos.CENTER);
        panel.setPadding(new Insets(24));
        panel.setStyle("-fx-background-color: rgba(20,22,29,0.92); -fx-background-radius: 16px;");
        panel.setMaxWidth(820);

        VBox menuCard = createMenuCard(stage);
        menuCard.setMinWidth(220);
        menuCard.setMaxWidth(220);

        HBox layout = new HBox(16, menuCard, panel);
        layout.setAlignment(Pos.CENTER_LEFT);
        layout.setPadding(new Insets(28));

        StackPane root = new StackPane(layout);
        root.setStyle("""
            -fx-background-image: url('https://images.unsplash.com/photo-1467003909585-2f8a72700288?auto=format&fit=crop&w=1600&q=80');
            -fx-background-size: cover;
            -fx-background-position: center center;
            -fx-background-color: rgba(0,0,0,0.45);
            -fx-background-blend-mode: overlay;
        """);

        Scene scene = new Scene(root, 1100, 640);
        stage.setTitle("Restaurant Manager - " + brand);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setWidth(1100);
        stage.setHeight(640);
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.show();
    }

    private VBox createCard(String title, String desc, Button action) {
        Label t = new Label(title);
        t.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        Label d = new Label(desc);
        d.setWrapText(true);
        d.setStyle("-fx-text-fill: #c7ced7;");

        VBox box = new VBox(8, t, d, action);
        box.setAlignment(Pos.TOP_LEFT);
        box.setPadding(new Insets(14));
        box.setPrefWidth(260);
        box.setStyle("-fx-background-color: rgba(34,38,48,0.95); -fx-background-radius: 10px; -fx-border-color: #2f3745; -fx-border-radius: 10px;");
        return box;
    }

    private Button createPrimaryButton(String text) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color: linear-gradient(#f7b733, #f5a623); -fx-text-fill: #1f1f1f; -fx-font-weight: bold; -fx-background-radius: 6px;");
        b.setMinHeight(34);
        b.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(b, Priority.ALWAYS);
        return b;
    }

    private Button createSecondaryButton(String text) {
        Button b = new Button(text);
        b.setStyle("-fx-background-color: #253041; -fx-text-fill: #e9edf4; -fx-font-weight: bold; -fx-background-radius: 6px; -fx-border-color: #374458; -fx-border-radius: 6px;");
        b.setMinHeight(32);
        b.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(b, Priority.ALWAYS);
        return b;
    }

    private VBox createMenuCard(Stage stage) {
        Label hello = new Label("Hello, Admin");
        hello.setStyle("-fx-text-fill: #e9edf4; -fx-font-size: 14px; -fx-font-weight: bold;");

        Label avatar = new Label("👩");
        avatar.setStyle("-fx-background-color: #f0c14b; -fx-text-fill: #1f1f1f; -fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 10px; -fx-background-radius: 50%;");

        VBox header = new VBox(6, hello, avatar);
        header.setAlignment(Pos.CENTER);

        Button profile = createMenuButton("Profil");
        Button wallet = createMenuButton("Dashboard");
        Button settings = createMenuButton("Paramètres");
        Button logout = createMenuButton("Se déconnecter");

        profile.setOnAction(e -> new ProfileWindow().show(stage));
        wallet.setOnAction(e -> new DashboardWindow().show(stage));
        settings.setOnAction(e -> new MainWindow().show(stage));
        logout.setOnAction(e -> new LoginWindow().show(stage));

        profile.setMaxWidth(Double.MAX_VALUE);
        wallet.setMaxWidth(Double.MAX_VALUE);
        settings.setMaxWidth(Double.MAX_VALUE);
        logout.setMaxWidth(Double.MAX_VALUE);

        VBox card = new VBox(12, header, profile, wallet, settings, logout);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(16));
        card.setPrefWidth(200);
        card.setStyle("-fx-background-color: rgba(17,24,33,0.95); -fx-background-radius: 12px; -fx-border-color: #2f3745; -fx-border-radius: 12px;");
        return card;
    }

    private Button createMenuButton(String text) {
        String base = "-fx-background-color: #1b2431; -fx-text-fill: #e9edf4; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-border-color: #2f3745; -fx-border-radius: 8px;";
        String hover = "-fx-background-color: #253041; -fx-text-fill: #f5c242; -fx-font-weight: bold; -fx-background-radius: 8px; -fx-border-color: #374458; -fx-border-radius: 8px;";
        Button b = new Button(text);
        b.setStyle(base);
        b.setAlignment(Pos.CENTER_LEFT);
        b.setMinHeight(34);
        b.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(b, Priority.ALWAYS);
        b.setOnMouseEntered(evt -> b.setStyle(hover));
        b.setOnMouseExited(evt -> b.setStyle(base));
        return b;
    }
}

