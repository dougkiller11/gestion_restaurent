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
import javafx.util.Callback;

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
    private ComboBox<String> roleField;
    private ComboBox<String> statutField;
    private CheckBox actifField;

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
        TableColumn<Client, String> colStatut = new TableColumn<Client, String>("Statut");
        colStatut.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getStatut() == null || c.getValue().getStatut().isBlank() ? "-" : c.getValue().getStatut()
        ));
        TableColumn<Client, String> colRole = new TableColumn<Client, String>("Rôle");
        colRole.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getRole() == null ? "client" : c.getValue().getRole()
        ));
        TableColumn<Client, String> colActif = new TableColumn<Client, String>("Actif");
        colActif.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().isActif() ? "Oui" : "Non"
        ));
        TableColumn<Client, Void> colActions = new TableColumn<>("Actions");
        colActions.setCellFactory(getActionsCellFactory());
        colActions.setPrefWidth(120);
        // Use collection-based setAll to avoid varargs unchecked warning
        table.getColumns().setAll(java.util.Arrays.asList(colNom, colPrenom, colEmail, colPhone, colStatut, colRole, colActif, colActions));
        table.setPrefHeight(420);
        table.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> populate(val));

        nomField = new TextField();
        prenomField = new TextField();
        emailField = new TextField();
        passField = new PasswordField();
        phoneField = new TextField();
        addressField = new TextField();
        roleField = new ComboBox<>();
        roleField.getItems().addAll("client", "employe");
        roleField.setPromptText("Rôle");
        statutField = new ComboBox<>();
        statutField.getItems().addAll("En service", "En repos", "En congé");
        statutField.setPromptText("Statut (employé)");
        actifField = new CheckBox("Actif");
        actifField.setSelected(true);
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
        form.addRow(6, new Label("Rôle"), roleField);
        form.addRow(7, new Label("Statut"), statutField);
        form.addRow(8, new Label("Actif"), actifField);
        form.getChildren().stream()
                .filter(n -> n instanceof Label)
                .forEach(n -> ((Label) n).setStyle("-fx-text-fill: #c7ced7;"));

        HBox actions = new HBox(10, saveBtn, deleteBtn, backBtn);
        actions.setAlignment(Pos.CENTER_RIGHT);

        VBox formCard = new VBox(12, form, actions);
        formCard.setPadding(new Insets(16));
        formCard.setStyle("-fx-background-color: rgba(17,19,25,0.95); -fx-background-radius: 12px; -fx-border-color: #2f3747; -fx-border-radius: 12px;");

        VBox tableCard = new VBox(10, table);
        tableCard.setPadding(new Insets(12));
        tableCard.setStyle("-fx-background-color: rgba(20,22,29,0.92); -fx-background-radius: 12px;");

        VBox right = new VBox(14, title, tableCard, formCard);
        right.setPadding(new Insets(16));

        VBox nav = AdminNavFactory.create(stage);
        nav.setMinWidth(220);

        HBox layout = new HBox(16, nav, right);
        layout.setAlignment(Pos.CENTER_LEFT);
        layout.setPadding(new Insets(24));

        BorderPane root = new BorderPane(layout);
        root.setStyle("""
            -fx-background-image: url('https://images.unsplash.com/photo-1467003909585-2f8a72700288?auto=format&fit=crop&w=1600&q=80');
            -fx-background-size: cover;
            -fx-background-position: center center;
            -fx-background-color: rgba(0,0,0,0.45);
            -fx-background-blend-mode: overlay;
        """);

        Stage s = stage == null ? new Stage() : stage;
        s.setTitle("Clients - Arc Burger");
        s.setScene(new Scene(root, 1300, 820));
        s.setResizable(true);
        s.setWidth(1300);
        s.setHeight(820);
        s.setMinWidth(1180);
        s.setMinHeight(720);
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
        String role = c.getRole() == null ? "client" : c.getRole();
        roleField.setValue(role);
        if ("employe".equals(role)) {
            statutField.setDisable(false);
            statutField.setValue(c.getStatut() == null || c.getStatut().isBlank() ? "En service" : c.getStatut());
        } else {
            statutField.setDisable(true);
            statutField.setValue("---");
        }
        actifField.setSelected(c.isActif());
    }

    private void save() {
        String nom = nomField.getText().trim();
        String prenom = prenomField.getText().trim();
        String email = emailField.getText().trim();
        String pwd = passField.getText().trim();
        String phone = phoneField.getText().trim();
        String addr = addressField.getText().trim();
        String role = roleField.getValue() == null ? "client" : roleField.getValue();
        String statut = "employe".equals(role) ? statutField.getValue() : "---";
        boolean actif = actifField.isSelected();
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || pwd.isEmpty()) {
            return;
        }
        Client selected = table.getSelectionModel().getSelectedItem();
        try {
            if (selected == null) {
                Client added = dao.add(nom, prenom, email, pwd, phone, addr, role, statut, actif);
                if (added != null) {
                    refresh();
                    table.getSelectionModel().select(added);
                }
            } else {
                dao.update(selected.getId(), nom, prenom, email, pwd, phone, addr, role, statut, actif);
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
        roleField.setValue(null);
        statutField.setDisable(false);
        statutField.setValue(null);
        actifField.setSelected(true);
    }

    private Callback<TableColumn<Client, Void>, TableCell<Client, Void>> getActionsCellFactory() {
        return col -> new TableCell<>() {
            private final Button editBtn = new Button("M");
            private final Button delBtn = new Button("S");
            {
                editBtn.setStyle("-fx-background-color: #2b7de9; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6px; -fx-padding: 4 8;");
                delBtn.setStyle("-fx-background-color: #b02a37; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6px; -fx-padding: 4 8;");
                editBtn.setOnAction(e -> {
                    Client c = getTableView().getItems().get(getIndex());
                    getTableView().getSelectionModel().select(c);
                    populate(c);
                });
                delBtn.setOnAction(e -> {
                    Client c = getTableView().getItems().get(getIndex());
                    try {
                        dao.delete(c.getId());
                        refresh();
                        clearForm();
                    } catch (SQLException ex) {
                        new Alert(Alert.AlertType.ERROR, "Erreur suppression: " + ex.getMessage()).showAndWait();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(6, editBtn, delBtn);
                    box.setAlignment(Pos.CENTER);
                    setGraphic(box);
                }
            }
        };
    }
}

