package restau.ui;

import restau.dao.ProfileDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class ProfileWindow {

    private final ProfileDAO dao = new ProfileDAO();
    private ProfileDAO.ProfileData profile = new ProfileDAO.ProfileData();

    public void show(Stage stage) {
        profile = dao.load();

        Label title = new Label("Profil");
        title.setStyle("-fx-text-fill: #f5c242; -fx-font-size: 20px; -fx-font-weight: bold;");

        Label status = new Label();
        status.setStyle("-fx-text-fill: #f5c242;");

        VBox identity = makeRow("Identité", safe(profile.nom) + " " + safe(profile.prenom), () -> showEditIdentity(stage));
        VBox emailRow = makeRow("E-mail", safe(profile.email), () -> showEditEmail(stage));
        VBox phoneRow = makeRow("Téléphone", safe(profile.phone), () -> showEditPhone(stage));
        VBox addrRow = makeRow("Adresse", safe(profile.address).isEmpty() ? "Non renseignée" : safe(profile.address), () -> showEditAddress(stage));

        Button backBtn = new Button("Retour");
        backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #e9edf4; -fx-border-color: #2f3747; -fx-border-radius: 6px;");
        backBtn.setOnAction(e -> new MainWindow().show(stage));

        VBox list = new VBox(12, identity, emailRow, phoneRow, addrRow);
        list.setPadding(new Insets(10, 0, 10, 0));

        VBox card = new VBox(16, title, list, backBtn, status);
        card.setAlignment(Pos.TOP_LEFT);
        card.setPadding(new Insets(22));
        card.setMaxWidth(760);
        card.setStyle("-fx-background-color: rgba(20,22,29,0.92); -fx-background-radius: 14px;");

        VBox menu = createMenuCard(stage);
        menu.setMinWidth(220);
        menu.setMaxWidth(220);

        HBox layout = new HBox(16, menu, card);
        layout.setAlignment(Pos.CENTER_LEFT);
        layout.setPadding(new Insets(32));

        StackPane root = new StackPane(layout);
        root.setStyle("""
            -fx-background-image: url('https://images.unsplash.com/photo-1467003909585-2f8a72700288?auto=format&fit=crop&w=1600&q=80');
            -fx-background-size: cover;
            -fx-background-position: center center;
            -fx-background-color: rgba(0,0,0,0.45);
            -fx-background-blend-mode: overlay;
        """);

        Scene scene = new Scene(root, 1100, 640);
        stage.setTitle("Profil - Arc Burger");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setWidth(1100);
        stage.setHeight(640);
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.show();
    }

    private VBox makeRow(String label, String value, Runnable onEdit) {
        Label l = new Label(label);
        l.setStyle("-fx-text-fill: #c7ced7; -fx-font-weight: bold;");

        Label v = new Label(value);
        v.setStyle("-fx-text-fill: #e9edf4; -fx-font-size: 14px;");

        Button edit = new Button("✏️");
        edit.setStyle("-fx-background-color: transparent; -fx-text-fill: #f5c242; -fx-font-size: 14px;");
        edit.setOnAction(e -> onEdit.run());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox line = new HBox(10, v, spacer, edit);
        line.setAlignment(Pos.CENTER_LEFT);

        VBox box = new VBox(4, l, line);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-background-color: rgba(34,38,48,0.9); -fx-background-radius: 10px; -fx-border-color: #2f3745; -fx-border-radius: 10px;");
        return box;
    }

    private void showEditIdentity(Stage stage) {
        Label title = new Label("Modifier Identité");
        title.setStyle("-fx-text-fill: #f5c242; -fx-font-size: 18px; -fx-font-weight: bold;");
        TextField nomField = new TextField(safe(profile.nom));
        TextField prenomField = new TextField(safe(profile.prenom));
        Button save = new Button("Enregistrer");
        save.setStyle("-fx-background-color: linear-gradient(#f7b733, #f5a623); -fx-text-fill: #1f1f1f; -fx-font-weight: bold; -fx-background-radius: 6px;");
        save.setOnAction(e -> {
            profile.nom = nomField.getText().trim();
            profile.prenom = prenomField.getText().trim();
            dao.save(profile);
            show(stage);
        });
        Button cancel = new Button("Annuler");
        cancel.setStyle("-fx-background-color: transparent; -fx-text-fill: #e9edf4; -fx-border-color: #2f3747; -fx-border-radius: 6px;");
        cancel.setOnAction(e -> show(stage));
        VBox card = new VBox(12, title, new Label("Nom"), nomField, new Label("Prénom"), prenomField, new HBox(10, save, cancel));
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: rgba(20,22,29,0.92); -fx-background-radius: 12px;");
        StackPane root = new StackPane(card);
        root.setPadding(new Insets(28));
        root.setStyle("-fx-background-color: rgba(0,0,0,0.6);");
        stage.setScene(new Scene(root, 520, 400));
    }

    private void showEditEmail(Stage stage) {
        Label title = new Label("Modifier E-mail");
        title.setStyle("-fx-text-fill: #f5c242; -fx-font-size: 18px; -fx-font-weight: bold;");
        TextField emailField = new TextField(safe(profile.email));
        Button save = new Button("Enregistrer");
        save.setStyle("-fx-background-color: linear-gradient(#f7b733, #f5a623); -fx-text-fill: #1f1f1f; -fx-font-weight: bold; -fx-background-radius: 6px;");
        save.setOnAction(e -> {
            profile.email = emailField.getText().trim();
            dao.save(profile);
            show(stage);
        });
        Button cancel = new Button("Annuler");
        cancel.setStyle("-fx-background-color: transparent; -fx-text-fill: #e9edf4; -fx-border-color: #2f3747; -fx-border-radius: 6px;");
        cancel.setOnAction(e -> show(stage));
        VBox card = new VBox(12, title, new Label("E-mail"), emailField, new HBox(10, save, cancel));
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: rgba(20,22,29,0.92); -fx-background-radius: 12px;");
        StackPane root = new StackPane(card);
        root.setPadding(new Insets(28));
        root.setStyle("-fx-background-color: rgba(0,0,0,0.6);");
        stage.setScene(new Scene(root, 520, 300));
    }

    private void showEditPhone(Stage stage) {
        Label title = new Label("Modifier Téléphone");
        title.setStyle("-fx-text-fill: #f5c242; -fx-font-size: 18px; -fx-font-weight: bold;");
        TextField phoneField = new TextField(safe(profile.phone));
        Button save = new Button("Enregistrer");
        save.setStyle("-fx-background-color: linear-gradient(#f7b733, #f5a623); -fx-text-fill: #1f1f1f; -fx-font-weight: bold; -fx-background-radius: 6px;");
        save.setOnAction(e -> {
            profile.phone = phoneField.getText().trim();
            dao.save(profile);
            show(stage);
        });
        Button cancel = new Button("Annuler");
        cancel.setStyle("-fx-background-color: transparent; -fx-text-fill: #e9edf4; -fx-border-color: #2f3747; -fx-border-radius: 6px;");
        cancel.setOnAction(e -> show(stage));
        VBox card = new VBox(12, title, new Label("Téléphone"), phoneField, new HBox(10, save, cancel));
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: rgba(20,22,29,0.92); -fx-background-radius: 12px;");
        StackPane root = new StackPane(card);
        root.setPadding(new Insets(28));
        root.setStyle("-fx-background-color: rgba(0,0,0,0.6);");
        stage.setScene(new Scene(root, 520, 300));
    }

    private void showEditAddress(Stage stage) {
        Label title = new Label("Modifier Adresse");
        title.setStyle("-fx-text-fill: #f5c242; -fx-font-size: 18px; -fx-font-weight: bold;");
        TextArea addrField = new TextArea(safe(profile.address));
        addrField.setPrefRowCount(3);
        Button save = new Button("Enregistrer");
        save.setStyle("-fx-background-color: linear-gradient(#f7b733, #f5a623); -fx-text-fill: #1f1f1f; -fx-font-weight: bold; -fx-background-radius: 6px;");
        save.setOnAction(e -> {
            profile.address = addrField.getText().trim();
            dao.save(profile);
            show(stage);
        });
        Button cancel = new Button("Annuler");
        cancel.setStyle("-fx-background-color: transparent; -fx-text-fill: #e9edf4; -fx-border-color: #2f3747; -fx-border-radius: 6px;");
        cancel.setOnAction(e -> show(stage));
        VBox card = new VBox(12, title, new Label("Adresse"), addrField, new HBox(10, save, cancel));
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: rgba(20,22,29,0.92); -fx-background-radius: 12px;");
        StackPane root = new StackPane(card);
        root.setPadding(new Insets(28));
        root.setStyle("-fx-background-color: rgba(0,0,0,0.6);");
        stage.setScene(new Scene(root, 520, 360));
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }

    private VBox createMenuCard(Stage stage) {
        Label hello = new Label("Hello, Admin");
        hello.setStyle("-fx-text-fill: #e9edf4; -fx-font-size: 14px; -fx-font-weight: bold;");

        Label avatar = new Label("👩");
        avatar.setStyle("-fx-background-color: #f0c14b; -fx-text-fill: #1f1f1f; -fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 10px; -fx-background-radius: 50%;");

        VBox header = new VBox(6, hello, avatar);
        header.setAlignment(Pos.CENTER);

        Button profileBtn = createMenuButton("Profil");
        Button dashboardBtn = createMenuButton("Dashboard");
        Button settingsBtn = createMenuButton("Paramètres");
        Button logoutBtn = createMenuButton("Se déconnecter");

        profileBtn.setOnAction(e -> new ProfileWindow().show(stage));
        dashboardBtn.setOnAction(e -> new DashboardWindow().show(stage));
        settingsBtn.setOnAction(e -> new MainWindow().show(stage));
        logoutBtn.setOnAction(e -> new LoginWindow().show(stage));

        VBox card = new VBox(12, header, profileBtn, dashboardBtn, settingsBtn, logoutBtn);
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

