package restau.ui;

import restau.model.Client;
import restau.dao.ClientDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class ClientsWindow {

    private final ClientDAO dao = new ClientDAO();
    private TableView<Client> table;
    private TextField nomField;
    private TextField prenomField;
    private TextField emailField;
    private PasswordField passField;
    private TextField phoneField;
    private TextField addressField;

    public void show(Stage stage) {
        Label title = new Label("Gérer les clients");
        title.setStyle("-fx-text-fill: #f5c242; -fx-font-size: 20px; -fx-font-weight: bold;");

        table = new TableView<>();
        TableColumn<Client, String> colNom = new TableColumn<Client, String>("Nom");
        colNom.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNom()));
        TableColumn<Client, String> colPrenom = new TableColumn<Client, String>("Prénom");
        colPrenom.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getPrenom()));
        TableColumn<Client, String> colEmail = new TableColumn<Client, String>("Email");
        colEmail.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getUsername()));
        TableColumn<Client, String> colPhone = new TableColumn<Client, String>("Téléphone");
        colPhone.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getPhone()));
        // Use collection-based setAll to avoid varargs unchecked warning
        table.getColumns().setAll(java.util.Arrays.asList(colNom, colPrenom, colEmail, colPhone));
        table.setPrefHeight(260);
        table.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> populate(val));

        nomField = new TextField();
        prenomField = new TextField();
        emailField = new TextField();
        passField = new PasswordField();
        phoneField = new TextField();
        addressField = new TextField();
        nomField.setPromptText("Nom");
        prenomField.setPromptText("Prénom");
        emailField.setPromptText("Email");
        passField.setPromptText("Mot de passe");
        phoneField.setPromptText("Téléphone");
        addressField.setPromptText("Adresse");

        Button saveBtn = new Button("Enregistrer");
        saveBtn.setStyle("-fx-background-color: linear-gradient(#f7b733, #f5a623); -fx-text-fill: #1f1f1f; -fx-font-weight: bold; -fx-background-radius: 6px;");
        saveBtn.setOnAction(e -> save());

        Button deleteBtn = new Button("Supprimer");
        deleteBtn.setStyle("-fx-background-color: #8b1a1a; -fx-text-fill: #f5f5f5; -fx-font-weight: bold; -fx-background-radius: 6px;");
        deleteBtn.setOnAction(e -> delete());

        Button backBtn = new Button("Retour");
        backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #e9edf4; -fx-border-color: #2f3747; -fx-border-radius: 6px;");
        backBtn.setOnAction(e -> new MainWindow().show(stage));

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.addRow(0, new Label("Nom"), nomField);
        form.addRow(1, new Label("Prénom"), prenomField);
        form.addRow(2, new Label("Email"), emailField);
        form.addRow(3, new Label("Téléphone"), phoneField);
        form.addRow(4, new Label("Adresse"), addressField);
        form.addRow(5, new Label("Mot de passe"), passField);
        form.getChildren().stream()
                .filter(n -> n instanceof Label)
                .forEach(n -> ((Label) n).setStyle("-fx-text-fill: #c7ced7;"));

        HBox actions = new HBox(10, saveBtn, deleteBtn, backBtn);
        actions.setAlignment(Pos.CENTER_RIGHT);

        VBox right = new VBox(12, title, table, form, actions);
        right.setPadding(new Insets(16));
        right.setStyle("-fx-background-color: rgba(20,22,29,0.92); -fx-background-radius: 12px;");

        BorderPane root = new BorderPane();
        root.setCenter(right);
        root.setPadding(new Insets(24));
        root.setStyle("""
            -fx-background-image: url('https://images.unsplash.com/photo-1467003909585-2f8a72700288?auto=format&fit=crop&w=1600&q=80');
            -fx-background-size: cover;
            -fx-background-position: center center;
            -fx-background-color: rgba(0,0,0,0.45);
            -fx-background-blend-mode: overlay;
        """);

        Stage s = stage == null ? new Stage() : stage;
        s.setTitle("Clients - Arc Burger");
        s.setScene(new Scene(root, 1100, 640));
        s.setResizable(true);
        s.setWidth(1100);
        s.setHeight(640);
        s.setMinWidth(900);
        s.setMinHeight(600);
        s.show();

        refresh();
    }

    private void refresh() {
        table.getItems().setAll(dao.listAll());
    }

    private void populate(Client c) {
        if (c == null) return;
        nomField.setText(c.getNom());
        prenomField.setText(c.getPrenom());
        emailField.setText(c.getUsername());
        phoneField.setText(c.getPhone());
        addressField.setText(c.getAddress());
        passField.setText(c.getPassword());
    }

    private void save() {
        String nom = nomField.getText().trim();
        String prenom = prenomField.getText().trim();
        String email = emailField.getText().trim();
        String pwd = passField.getText().trim();
        String phone = phoneField.getText().trim();
        String addr = addressField.getText().trim();
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || pwd.isEmpty()) {
            return;
        }
        Client selected = table.getSelectionModel().getSelectedItem();
        try {
            if (selected == null) {
                Client added = dao.add(nom, prenom, email, pwd, phone, addr);
                if (added != null) {
                    refresh();
                    table.getSelectionModel().select(added);
                }
            } else {
                dao.update(selected.getId(), nom, prenom, email, pwd, phone, addr);
                refresh();
            }
            clearForm();
        } catch (SQLIntegrityConstraintViolationException dup) {
            new Alert(Alert.AlertType.ERROR, "Email déjà utilisé. Choisissez un autre email.").showAndWait();
        } catch (SQLException ex) {
            new Alert(Alert.AlertType.ERROR, "Erreur SQL : " + ex.getMessage()).showAndWait();
        }
    }

    private void delete() {
        Client selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        try {
            dao.delete(selected.getId());
            refresh();
            clearForm();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void clearForm() {
        table.getSelectionModel().clearSelection();
        nomField.clear();
        prenomField.clear();
        emailField.clear();
        phoneField.clear();
        addressField.clear();
        passField.clear();
    }
}

