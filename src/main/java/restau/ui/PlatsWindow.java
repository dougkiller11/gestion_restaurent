package restau.ui;

import restau.dao.PlatDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlatsWindow {

    public void show(Stage stage) {
        Label title = new Label("Nouveau Plat");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #f5c242;");

        TextField nomField = new TextField();
        nomField.setPromptText("Nom du plat");
        nomField.setPrefWidth(420);
        styleField(nomField);

        TextField prixField = new TextField();
        prixField.setPromptText("Prix (ex: 12.50)");
        prixField.setPrefWidth(420);
        styleField(prixField);

        ComboBox<String> catField = new ComboBox<>();
        catField.getItems().addAll(
                "Plat",
                "Entrée",
                "Burger",
                "Poisson",
                "Poulet",
                "Pizza",
                "Dessert",
                "Boisson"
        );
        catField.setPromptText("Catégorie");
        catField.setPrefWidth(420);
        catField.setStyle("-fx-background-color: #0f121a; -fx-text-fill: #d8dde6; -fx-border-color: #1f2430; -fx-border-radius: 6px;");

        List<String> defaultIngredients = Arrays.asList(
                "Boeuf", "Champignon", "Creme Fraiche", "Fromage", "Mozzarella",
                "Oignon", "Olives", "Tomate", "Poulet", "Poivron", "Basilic", "Saumon", "Thon"
        );
        VBox ingredientsBox = new VBox(6);
        ingredientsBox.setPadding(new Insets(8));
        for (String ing : defaultIngredients) {
            CheckBox cb = new CheckBox(ing);
            cb.setStyle("-fx-text-fill: #e6ebf3;");
            ingredientsBox.getChildren().add(cb);
        }
        ScrollPane ingredientsPane = new ScrollPane(ingredientsBox);
        ingredientsPane.setPrefHeight(220);
        ingredientsPane.setFitToWidth(true);
        ingredientsPane.setStyle("-fx-background: #0f121a; -fx-border-color: #1f2430; -fx-border-radius: 6px; -fx-background-radius: 6px;");

        TextField imageField = new TextField();
        imageField.setPromptText("URL de l'image (http://...)");
        imageField.setPrefWidth(420);
        styleField(imageField);

        TextArea descField = new TextArea();
        descField.setPromptText("Description");
        descField.setPrefRowCount(3);
        descField.setPrefWidth(420);
        descField.setStyle("-fx-control-inner-background: #0f121a; -fx-text-fill: #e6ebf3; -fx-border-color: #1f2430; -fx-background-radius: 6px; -fx-border-radius: 6px;");

        CheckBox dispoBox = new CheckBox("Disponible");
        dispoBox.setSelected(true);
        dispoBox.setStyle("-fx-text-fill: #e6ebf3;");

        Button saveBtn = new Button("Ajouter le plat");
        saveBtn.setDefaultButton(true);
        saveBtn.setPrefWidth(200);
        saveBtn.setStyle("-fx-background-color: linear-gradient(#f7b733, #f5a623); -fx-text-fill: #1f1f1f; -fx-font-weight: bold; -fx-background-radius: 8px;");

        Button backBtn = new Button("Retour");
        backBtn.setOnAction(e -> new MainWindow().show(stage));
        backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #e9edf4; -fx-border-color: #2f3747; -fx-border-radius: 6px;");

        saveBtn.setOnAction(e -> {
            String nom = nomField.getText().trim();
            String cat = catField.getValue() == null ? "" : catField.getValue().trim();
            String prixTxt = prixField.getText().trim();
            String img = imageField.getText().trim();
            String desc = descField.getText().trim();

            if (nom.isEmpty() || cat.isEmpty() || prixTxt.isEmpty()) {
                showAlert("Champs requis", "Nom, catégorie et prix sont obligatoires.");
                return;
            }
            double prix;
            try {
                prix = Double.parseDouble(prixTxt.replace(',', '.'));
            } catch (NumberFormatException ex) {
                showAlert("Prix invalide", "Merci de saisir un nombre, ex: 12.50");
                return;
            }

            List<String> selectedIng = new ArrayList<>();
            ingredientsBox.getChildren().stream()
                    .filter(n -> n instanceof CheckBox && ((CheckBox) n).isSelected())
                    .forEach(n -> selectedIng.add(((CheckBox) n).getText()));

            int createdId = new PlatDAO().addPlat(
                    nom,
                    cat,
                    prix,
                    dispoBox.isSelected(),
                    img.isEmpty() ? null : img,
                    desc.isEmpty() ? null : desc,
                    selectedIng
            );

            if (createdId > 0) {
                showAlert("Succès", "Plat ajouté avec succès.");
                nomField.clear();
                catField.setValue(null);
                prixField.clear();
                imageField.clear();
                descField.clear();
                dispoBox.setSelected(true);
                ingredientsBox.getChildren().forEach(n -> {
                    if (n instanceof CheckBox cb) cb.setSelected(false);
                });
            } else {
                showAlert("Erreur", "Impossible d'ajouter le plat, réessayez.");
            }
        });

        VBox form = new VBox(12,
                nomField,
                prixField,
                catField,
                new Label("Ingrédients :"),
                ingredientsPane,
                imageField,
                descField,
                dispoBox
        );
        form.setAlignment(Pos.CENTER_LEFT);
        form.setPadding(new Insets(4, 0, 8, 0));
        form.getChildren().stream()
                .filter(n -> n instanceof Label)
                .forEach(n -> ((Label) n).setStyle("-fx-text-fill: #e6ebf3; -fx-font-size: 13px;"));

        HBox actions = new HBox(12, backBtn, saveBtn);
        actions.setAlignment(Pos.CENTER);

        VBox card = new VBox(18, title, form, actions);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPadding(new Insets(22));
        card.setStyle("-fx-background-color: #0b0d13; -fx-background-radius: 14px; -fx-border-color: #1f2430; -fx-border-radius: 14px;");

        VBox nav = AdminNavFactory.create(stage);
        nav.setMinWidth(220);

        HBox layout = new HBox(16, nav, card);
        layout.setAlignment(Pos.TOP_LEFT);
        layout.setPadding(new Insets(26));

        StackPane root = new StackPane(layout);
        root.setStyle("-fx-background-color: #0a0c12;");

        Scene scene = new Scene(root, 900, 820);
        stage.setTitle("Plats - Nouveau plat");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private void styleField(TextField field) {
        field.setStyle("-fx-background-color: #0f121a; -fx-text-fill: #e6ebf3; -fx-border-color: #1f2430; -fx-background-radius: 6px; -fx-border-radius: 6px;");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

