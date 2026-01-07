package restau.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import restau.dao.AuthDAO;

public class LoginWindow {

    public void show(Stage stage) {
        String brand = "Restaurant Arc Raiders";

        Label title = new Label("BIG");
        title.setStyle(
                "-fx-text-fill: #f5c242; -fx-font-size: 16px; -fx-font-weight: bold; -fx-letter-spacing: 1.2px;");

        Label subtitle = new Label(brand);
        subtitle.setStyle("-fx-text-fill: white; -fx-font-size: 28px; -fx-font-weight: bold;");

        Label tagline = new Label("Connectez-vous pour découvrir nos menus");
        tagline.setStyle("-fx-text-fill: #bfc4cc; -fx-font-size: 12px;");

        TextField username = new TextField();
        username.setPromptText("Email");
        username.getStyleClass().add("arc-field");
        username.setMaxWidth(280);

        PasswordField password = new PasswordField();
        password.setPromptText("Mot de passe");
        password.getStyleClass().add("arc-field");
        password.setMaxWidth(280);

        Label status = new Label();
        status.setStyle("-fx-text-fill: #f5c242;");

        Button loginButton = new Button("Se connecter");
        loginButton.setDefaultButton(true);
        loginButton.setMinHeight(32);
        loginButton.setMaxWidth(220);
        loginButton.setStyle(
                "-fx-background-color: linear-gradient(#f7b733, #f5a623); -fx-text-fill: #1f1f1f; -fx-font-weight: bold; -fx-background-radius: 6px;");
        loginButton.setOnAction(evt -> {
            if (username.getText().isBlank() || password.getText().isBlank()) {
                status.setText("Veuillez saisir utilisateur et mot de passe.");
                return;
            }
            AuthDAO.AuthResult auth = new AuthDAO().authenticate(username.getText(), password.getText());
            if (auth.success) {
                status.setText("Bienvenue " + auth.displayName + " (" + auth.role + ")");
                if (auth.client) {
                    new MenuAdminWindow().show(stage);
                } else {
                    new MainWindow().show(stage);
                }
            } else {
                status.setText("Identifiants invalides.");
            }
        });

        Hyperlink signupLink = new Hyperlink("Créer un compte");
        signupLink.setOnAction(evt -> new RegistrationWindow().show(stage));
        signupLink.setStyle("-fx-text-fill: #f5c242;");

        Hyperlink forgotLink = new Hyperlink("Mot de passe oublié ?");
        forgotLink.setStyle("-fx-text-fill: #9aa0aa;");

        HBox actions = new HBox(8, signupLink, forgotLink);
        actions.setAlignment(Pos.CENTER);

        VBox form = new VBox(8, title, subtitle, tagline, username, password, loginButton, actions, status);
        form.setAlignment(Pos.CENTER);
        form.setPadding(new Insets(20));
        form.setPrefWidth(300);
        form.setStyle("-fx-background-color: rgba(20,22,29,0.9); -fx-background-radius: 10px;");

        StackPane root = new StackPane(form);
        root.setPadding(new Insets(28));
        root.setStyle(
                """
                            -fx-background-image: url('https://images.unsplash.com/photo-1504674900247-0877df9cc836?auto=format&fit=crop&w=1600&q=80');
                            -fx-background-size: cover;
                            -fx-background-position: center center;
                            -fx-background-color: rgba(0,0,0,0.45);
                            -fx-background-blend-mode: overlay;
                        """);

        Scene scene = new Scene(root, 1100, 640);
        applyCommonStyles(scene);
        stage.setTitle("Login - " + brand);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setWidth(1100);
        stage.setHeight(640);
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.show();
    }

    private void applyCommonStyles(Scene scene) {
        scene.getStylesheets().add("data:text/css," +
                ".arc-field{-fx-background-color:%23111821;-fx-text-fill:%23e9edf4;-fx-background-radius:6px;-fx-border-radius:6px;-fx-prompt-text-fill:%236b7180;-fx-border-color:%23253041;-fx-border-width:1px;-fx-padding:10px;}"
                +
                ".hyperlink{-fx-underline:false;}" +
                ".label{-fx-font-family:'Segoe UI';}" +
                "");
    }
}
