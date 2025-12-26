package restau.ui;

import restau.dao.PlatDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class PlatsWindow {

    public void show(Stage stage) {
        Label title = new Label("Ajouter un plat");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #f5c242;");

        TextField nomField = new TextField();
        nomField.setPromptText("Nom du plat");
        nomField.setMaxWidth(360);

        ComboBox<String> catField = new ComboBox<>();
        catField.getItems().addAll(
                "Entree",
                "Burger",
                "Poisson",
                "Poulet",
                "Pizza",
                "Desert",
                "Boisson"
        );
        catField.setPromptText("Catégorie");
        catField.setEditable(false);
        catField.setMaxWidth(360);

        TextField prixField = new TextField();
        prixField.setPromptText("Prix (ex: 12.50)");
        prixField.setMaxWidth(360);

        TextField imageField = new TextField();
        imageField.setPromptText("URL image ou chemin local");
        imageField.setMaxWidth(360);

        TextArea descField = new TextArea();
        descField.setPromptText("Description du plat / ingrédients");
        descField.setPrefRowCount(3);
        descField.setMaxWidth(360);

        CheckBox dispoBox = new CheckBox("Disponible");
        dispoBox.setSelected(true);
        dispoBox.setStyle("-fx-text-fill: #d8dde6;");

        Label status = new Label();
        status.setStyle("-fx-text-fill: #f5c242;");

        Button saveBtn = new Button("Enregistrer");
        saveBtn.setDefaultButton(true);
        saveBtn.setStyle("-fx-background-color: linear-gradient(#f7b733, #f5a623); -fx-text-fill: #1f1f1f; -fx-font-weight: bold; -fx-background-radius: 6px;");
        saveBtn.setOnAction(e -> {
            String nom = nomField.getText().trim();
            String cat = catField.getValue() == null ? "" : catField.getValue().trim();
            String prixTxt = prixField.getText().trim();
            String img = imageField.getText().trim();
            String desc = descField.getText().trim();

            if (nom.isEmpty() || cat.isEmpty() || prixTxt.isEmpty()) {
                status.setText("Nom, catégorie et prix sont obligatoires.");
                return;
            }
            double prix;
            try {
                prix = Double.parseDouble(prixTxt.replace(',', '.'));
            } catch (NumberFormatException ex) {
                status.setText("Prix invalide.");
                return;
            }

            new PlatDAO().addPlat(nom, cat, prix, dispoBox.isSelected(), img.isEmpty() ? null : img, desc.isEmpty() ? null : desc);
            status.setText("Plat ajouté.");
            nomField.clear();
            catField.setValue(null);
            prixField.clear();
            imageField.clear();
            descField.clear();
            dispoBox.setSelected(true);
        });

        Button backBtn = new Button("Retour");
        backBtn.setOnAction(e -> new MainWindow().show(stage));
        backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #e9edf4; -fx-border-color: #2f3747; -fx-border-radius: 6px;");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.addRow(0, new Label("Nom"), nomField);
        form.addRow(1, new Label("Catégorie"), catField);
        form.addRow(2, new Label("Prix"), prixField);
        form.addRow(3, new Label("Image"), imageField);
        form.addRow(4, new Label("Description"), descField);
        form.addRow(5, new Label(""), dispoBox);
        form.getChildren().stream()
                .filter(n -> n instanceof Label)
                .forEach(n -> ((Label) n).setStyle("-fx-text-fill: #c7ced7;"));

        HBox actions = new HBox(10, saveBtn, backBtn);
        actions.setAlignment(Pos.CENTER_RIGHT);

        VBox card = new VBox(14, title, form, actions, status);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: rgba(20,22,29,0.92); -fx-background-radius: 12px;");

        StackPane root = new StackPane(card);
        root.setPadding(new Insets(28));
        root.setStyle("""
            -fx-background-image: url('https://images.unsplash.com/photo-1504674900247-0877df9cc836?auto=format&fit=crop&w=1600&q=80');
            -fx-background-size: cover;
            -fx-background-position: center center;
            -fx-background-color: rgba(0,0,0,0.45);
            -fx-background-blend-mode: overlay;
        """);

        Scene scene = new Scene(root, 720, 520);
        stage.setTitle("Plats - Restaurant Manager");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}

