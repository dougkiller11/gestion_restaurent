package restau.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import restau.dao.AuthDAO;
import restau.ui.LoginWindow;

public class RegistrationWindow {

    public void show(Stage stage) {
        String brand = "Arc Burger";

        Label title = new Label("PREMIUM");
        title.setStyle("-fx-text-fill: #f5c242; -fx-font-size: 16px; -fx-font-weight: bold; -fx-letter-spacing: 1.2px;");

        Label subtitle = new Label("Rejoindre " + brand);
        subtitle.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");

        Label tagline = new Label("Créez votre compte pour commander");
        tagline.setStyle("-fx-text-fill: #bfc4cc; -fx-font-size: 12px;");

        TextField nom = new TextField();
        nom.setPromptText("Nom");
        nom.getStyleClass().add("arc-field");

        TextField prenom = new TextField();
        prenom.setPromptText("Prénom");
        prenom.getStyleClass().add("arc-field");

        TextField username = new TextField();
        username.setPromptText("Email ou utilisateur");
        username.getStyleClass().add("arc-field");

        PasswordField password = new PasswordField();
        password.setPromptText("Mot de passe");
        password.getStyleClass().add("arc-field");

        PasswordField confirm = new PasswordField();
        confirm.setPromptText("Confirmer le mot de passe");
        confirm.getStyleClass().add("arc-field");

        Label status = new Label();
        status.setStyle("-fx-text-fill: #f5c242;");

        Button registerButton = new Button("S'inscrire");
        registerButton.setDefaultButton(true);
        registerButton.setMinHeight(38);
        registerButton.setMaxWidth(Double.MAX_VALUE);
        registerButton.setStyle("-fx-background-color: linear-gradient(#f7b733, #f5a623); -fx-text-fill: #1f1f1f; -fx-font-weight: bold; -fx-background-radius: 6px;");
        registerButton.setOnAction(evt -> {
            if (nom.getText().isBlank() || prenom.getText().isBlank()
                    || username.getText().isBlank()
                    || password.getText().isBlank() || confirm.getText().isBlank()) {
                status.setText("Tous les champs sont obligatoires.");
                return;
            }
            if (!password.getText().equals(confirm.getText())) {
                status.setText("Les mots de passe ne correspondent pas.");
                return;
            }
            boolean ok = new AuthDAO().registerClient(nom.getText(), prenom.getText(),
                    username.getText(), password.getText());
            if (ok) {
                status.setText("Inscription réussie, vous pouvez vous connecter.");
                new LoginWindow().show(stage);
            } else {
                status.setText("Echec de l'inscription (utilisateur déjà pris ?).");
            }
        });

        Button backButton = new Button("Retour");
        backButton.setOnAction(evt -> new LoginWindow().show(stage));
        backButton.setStyle("-fx-background-color: transparent; -fx-border-color: #2e3747; -fx-border-radius: 6px; -fx-text-fill: #d8dde6;");

        VBox form = new VBox(12, title, subtitle, tagline, nom, prenom, username, password, confirm, registerButton, backButton, status);
        form.setAlignment(Pos.CENTER);
        form.setPadding(new Insets(32));
        form.setPrefWidth(420);
        form.setStyle("-fx-background-color: rgba(20,22,29,0.92); -fx-background-radius: 12px;");

        StackPane root = new StackPane(form);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #0f141b, #1b232e);");

        Scene scene = new Scene(root, 960, 560);
        applyCommonStyles(scene);
        stage.setTitle("Inscription - " + brand);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private void applyCommonStyles(Scene scene) {
        scene.getStylesheets().add("data:text/css," +
                ".arc-field{-fx-background-color:%23111821;-fx-text-fill:%23e9edf4;-fx-background-radius:6px;-fx-border-radius:6px;-fx-prompt-text-fill:%236b7180;-fx-border-color:%23253041;-fx-border-width:1px;-fx-padding:10px;}" +
                ".hyperlink{-fx-underline:false;}" +
                ".label{-fx-font-family:'Segoe UI';}" +
                "");
    }
}

