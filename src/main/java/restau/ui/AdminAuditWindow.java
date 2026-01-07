package restau.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import restau.dao.PlatDAO;
import restau.model.Plat;

public class AdminAuditWindow {

    private final PlatDAO dao = new PlatDAO();
    private TableView<Plat> table;

    public void show(Stage stage) {
        Label title = new Label("Historique des plats");
        title.setStyle("-fx-text-fill: #f5c242; -fx-font-size: 20px; -fx-font-weight: bold;");

        table = new TableView<>();
        TableColumn<Plat, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(String.valueOf(c.getValue().getId())));
        idCol.setPrefWidth(60);

        TableColumn<Plat, String> nomCol = new TableColumn<>("Nom");
        nomCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNom()));
        nomCol.setPrefWidth(180);

        TableColumn<Plat, String> catCol = new TableColumn<>("Catégorie");
        catCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getCategorie()));
        catCol.setPrefWidth(120);

        TableColumn<Plat, String> prixCol = new TableColumn<>("Prix");
        prixCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(String.format("%.2f €", c.getValue().getPrix())));
        prixCol.setPrefWidth(90);

        TableColumn<Plat, String> dispoCol = new TableColumn<>("Statut");
        dispoCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().isDisponible() ? "Disponible" : "Épuisé"
        ));
        dispoCol.setPrefWidth(110);

        table.getColumns().setAll(java.util.Arrays.asList(idCol, nomCol, catCol, prixCol, dispoCol));
        table.setPrefHeight(420);

        Button refresh = new Button("Actualiser");
        refresh.setStyle("-fx-background-color: linear-gradient(#f7b733, #f5a623); -fx-text-fill: #1f1f1f; -fx-font-weight: bold; -fx-background-radius: 6px;");
        refresh.setOnAction(e -> loadData());

        Button back = new Button("Retour");
        back.setStyle("-fx-background-color: transparent; -fx-text-fill: #e9edf4; -fx-border-color: #2f3747; -fx-border-radius: 6px;");
        back.setOnAction(e -> new MainWindow().show(stage));

        HBox actions = new HBox(10, back, refresh);
        actions.setAlignment(Pos.CENTER_LEFT);

        VBox panel = new VBox(12, title, table, actions);
        panel.setPadding(new Insets(18));
        panel.setStyle("-fx-background-color: rgba(20,22,29,0.92); -fx-background-radius: 12px;");
        panel.setMaxWidth(820);

        VBox nav = AdminNavFactory.create(stage);
        nav.setMinWidth(220);

        HBox layout = new HBox(16, nav, panel);
        layout.setAlignment(Pos.CENTER_LEFT);
        layout.setPadding(new Insets(24));

        BorderPane root = new BorderPane(layout);
        root.setStyle("""
            -fx-background-image: url('https://images.unsplash.com/photo-1504674900247-0877df9cc836?auto=format&fit=crop&w=1600&q=80');
            -fx-background-size: cover;
            -fx-background-position: center center;
            -fx-background-color: rgba(0,0,0,0.45);
            -fx-background-blend-mode: overlay;
        """);

        Scene scene = new Scene(root, 900, 640);
        stage.setTitle("Historique des plats - Admin");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setWidth(900);
        stage.setHeight(640);
        stage.setMinWidth(820);
        stage.setMinHeight(600);
        stage.show();

        loadData();
    }

    private void loadData() {
        table.getItems().setAll(dao.listAll());
    }
}

