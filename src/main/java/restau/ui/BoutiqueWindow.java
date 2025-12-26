package restau.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import restau.dao.PlatDAO;
import restau.model.Plat;

public class BoutiqueWindow {

    private final PlatDAO dao = new PlatDAO();
    private TableView<Plat> table;
    private TextArea description;

    public void show(Stage stage) {
        Label title = new Label("Boutique - Choisissez vos plats");
        title.setStyle("-fx-text-fill: #f5c242; -fx-font-size: 20px; -fx-font-weight: bold;");

        table = new TableView<>();
        TableColumn<Plat, String> nomCol = new TableColumn<>("Nom");
        nomCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNom()));
        TableColumn<Plat, String> catCol = new TableColumn<>("Catégorie");
        catCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getCategorie()));
        TableColumn<Plat, String> priceCol = new TableColumn<>("Prix");
        priceCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(String.format("%.2f €", c.getValue().getPrix())));

        table.getColumns().setAll(java.util.Arrays.asList(nomCol, catCol, priceCol));
        table.setPrefHeight(320);
        table.getSelectionModel().selectedItemProperty().addListener((obs, old, val) -> showDesc(val));

        description = new TextArea();
        description.setEditable(false);
        description.setWrapText(true);
        description.setPromptText("Description du plat");
        description.setPrefRowCount(4);

        Button refresh = new Button("Actualiser");
        refresh.setStyle("-fx-background-color: linear-gradient(#f7b733, #f5a623); -fx-text-fill: #1f1f1f; -fx-font-weight: bold; -fx-background-radius: 6px;");
        refresh.setOnAction(e -> loadData());

        Button logout = new Button("Se déconnecter");
        logout.setStyle("-fx-background-color: transparent; -fx-text-fill: #e9edf4; -fx-border-color: #2f3747; -fx-border-radius: 6px;");
        logout.setOnAction(e -> new LoginWindow().show(stage));

        HBox actions = new HBox(10, refresh, logout);
        actions.setAlignment(Pos.CENTER_RIGHT);

        VBox panel = new VBox(12, title, table, description, actions);
        panel.setPadding(new Insets(18));
        panel.setStyle("-fx-background-color: rgba(20,22,29,0.92); -fx-background-radius: 12px;");

        BorderPane root = new BorderPane(panel);
        root.setPadding(new Insets(24));
        root.setStyle("""
            -fx-background-image: url('https://images.unsplash.com/photo-1504674900247-0877df9cc836?auto=format&fit=crop&w=1600&q=80');
            -fx-background-size: cover;
            -fx-background-position: center center;
            -fx-background-color: rgba(0,0,0,0.45);
            -fx-background-blend-mode: overlay;
        """);

        Scene scene = new Scene(root, 1100, 640);
        stage.setTitle("Boutique - Arc Burger");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setWidth(1100);
        stage.setHeight(640);
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.show();

        loadData();
    }

    private void loadData() {
        table.getItems().setAll(dao.listAvailable());
        showDesc(table.getSelectionModel().getSelectedItem());
    }

    private void showDesc(Plat plat) {
        if (plat == null) {
            description.clear();
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(plat.getNom()).append(" - ").append(plat.getCategorie()).append("\n");
        sb.append(String.format("Prix : %.2f €\n", plat.getPrix()));
        if (plat.getDescription() != null && !plat.getDescription().isBlank()) {
            sb.append("\n").append(plat.getDescription());
        } else {
            sb.append("\nAucune description.");
        }
        description.setText(sb.toString());
    }
}


