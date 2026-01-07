package restau.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import restau.dao.PlatDAO;
import restau.model.Plat;

public class MenuAdminWindow {

    private final PlatDAO dao = new PlatDAO();
    private FlowPane flow;

    public void show(Stage stage) {
        Label title = new Label("Notre Menu");
        title.setStyle("-fx-text-fill: #f5c242; -fx-font-size: 26px; -fx-font-weight: bold;");

        flow = new FlowPane();
        flow.setHgap(14);
        flow.setVgap(14);
        flow.setPrefWrapLength(1100);
        flow.setPadding(new Insets(4));

        Button refresh = new Button("Actualiser");
        refresh.setStyle("-fx-background-color: linear-gradient(#f7b733, #f5a623); -fx-text-fill: #1f1f1f; -fx-font-weight: bold; -fx-background-radius: 6px;");
        refresh.setOnAction(e -> loadData());

        Button back = new Button("Retour");
        back.setStyle("-fx-background-color: transparent; -fx-text-fill: #e9edf4; -fx-border-color: #2f3747; -fx-border-radius: 6px;");
        back.setOnAction(e -> new MainWindow().show(stage));

        HBox actions = new HBox(10, refresh, back);
        actions.setAlignment(Pos.CENTER_RIGHT);

        ScrollPane scroll = new ScrollPane(flow);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: transparent; -fx-border-color: transparent;");

        VBox panel = new VBox(12, title, scroll, actions);
        panel.setPadding(new Insets(18));
        panel.setStyle("-fx-background-color: rgba(20,22,29,0.92); -fx-background-radius: 12px;");

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

        Scene scene = new Scene(root, 1100, 640);
        stage.setTitle("Menu - Restau App");
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
        flow.getChildren().clear();
        for (Plat plat : dao.listAll()) {
            flow.getChildren().add(createCard(plat));
        }
    }

    private VBox createCard(Plat plat) {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(200);
        imageView.setFitHeight(140);
        imageView.setPreserveRatio(true);
        if (plat.getImageUrl() != null && !plat.getImageUrl().isBlank()) {
            try {
                imageView.setImage(new Image(plat.getImageUrl(), 200, 0, true, true));
            } catch (Exception ignored) {
                imageView.setImage(new Image("https://via.placeholder.com/300x200?text=Plat"));
            }
        } else {
            imageView.setImage(new Image("https://via.placeholder.com/300x200?text=Plat"));
        }

        Label name = new Label(plat.getNom());
        name.setStyle("-fx-text-fill: #f5c242; -fx-font-size: 16px; -fx-font-weight: bold;");

        HBox badges = new HBox(6);
        badges.setAlignment(Pos.CENTER_LEFT);
        if (!plat.isDisponible()) {
            Label badge = new Label("Épuisé");
            badge.setStyle("-fx-background-color: #f5c242; -fx-text-fill: #1f1f1f; -fx-font-weight: bold; -fx-padding: 2 8; -fx-background-radius: 8px;");
            badges.getChildren().add(badge);
        }

        Label cat = new Label(plat.getCategorie() + " • " + String.format("%.2f €", plat.getPrix()));
        cat.setStyle("-fx-text-fill: #c7ced7;");

        Text desc = new Text((plat.getDescription() == null || plat.getDescription().isBlank()) ? "Pas de description." : plat.getDescription());
        desc.setWrappingWidth(200);
        desc.setTextAlignment(TextAlignment.LEFT);
        desc.setStyle("-fx-fill: #d8dde6;");

        Label recl = new Label("Réclamations: " + plat.getReclamations());
        recl.setStyle("-fx-text-fill: #f07c68; -fx-font-weight: bold;");

        FlowPane ingPane = new FlowPane();
        ingPane.setHgap(6);
        ingPane.setVgap(6);
        ingPane.setPrefWrapLength(200);
        if (plat.getIngredient() != null && !plat.getIngredient().isEmpty()) {
            for (String ing : plat.getIngredient()) {
                Label chip = new Label(ing);
                chip.setStyle("-fx-background-color: #253041; -fx-text-fill: #e9edf4; -fx-padding: 4 8; -fx-background-radius: 8px;");
                ingPane.getChildren().add(chip);
            }
        } else {
            Label chip = new Label("Ingrédients non fournis");
            chip.setStyle("-fx-text-fill: #9aa3b5;");
            ingPane.getChildren().add(chip);
        }

        Button edit = new Button("Modifier");
        edit.setStyle("-fx-background-color: #2b7de9; -fx-text-fill: white; -fx-background-radius: 6px;");
        edit.setOnAction(e -> openEditDialog(plat));

        Button complain = new Button("Réclamer");
        complain.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white; -fx-background-radius: 6px;");
        complain.setOnAction(e -> {
            if (dao.incrementReclamation(plat.getId())) {
                showAlert("Réclamation", "Réclamation enregistrée.");
                loadData();
            } else {
                showAlert("Erreur", "Impossible d'enregistrer la réclamation.");
            }
        });

        Button delete = new Button("Supprimer");
        delete.setStyle("-fx-background-color: #b02a37; -fx-text-fill: white; -fx-background-radius: 6px;");
        delete.setOnAction(e -> {
            Alert confirm = new Alert(AlertType.CONFIRMATION, "Supprimer ce plat ?");
            confirm.setHeaderText(null);
            confirm.showAndWait().ifPresent(btn -> {
                if (btn == javafx.scene.control.ButtonType.OK) {
                    if (dao.deletePlat(plat.getId())) {
                        showAlert("Supprimé", "Plat supprimé.");
                        loadData();
                    } else {
                        showAlert("Erreur", "Suppression impossible.");
                    }
                }
            });
        });

        HBox actions = new HBox(8, edit, complain, delete);
        actions.setAlignment(Pos.CENTER_LEFT);

        VBox card = new VBox(8, imageView, name, badges, cat, recl, ingPane, desc, actions);
        card.setPadding(new Insets(10));
        card.setAlignment(Pos.TOP_LEFT);
        card.setStyle("-fx-background-color: rgba(34,38,48,0.95); -fx-background-radius: 10px; -fx-border-color: #2f3745; -fx-border-radius: 10px;");
        card.setPrefWidth(220);
        return card;
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void openEditDialog(Plat plat) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Modifier le plat");
        dialog.setHeaderText(null);

        TextField nom = new TextField(plat.getNom());
        styleField(nom);
        TextField prix = new TextField(String.valueOf(plat.getPrix()));
        styleField(prix);
        TextField image = new TextField(plat.getImageUrl() == null ? "" : plat.getImageUrl());
        styleField(image);
        TextField desc = new TextField(plat.getDescription() == null ? "" : plat.getDescription());
        styleField(desc);

        ComboBox<String> cat = new ComboBox<>();
        cat.getItems().addAll("Plat", "Entrée", "Burger", "Poisson", "Poulet", "Pizza", "Dessert", "Boisson");
        cat.setValue(plat.getCategorie());
        cat.setStyle("-fx-background-color: #0f121a; -fx-text-fill: #d8dde6; -fx-border-color: #1f2430; -fx-border-radius: 6px;");

        CheckBox dispo = new CheckBox("Disponible");
        dispo.setSelected(plat.isDisponible());
        dispo.setStyle("-fx-text-fill: #e6ebf3;");

        VBox box = new VBox(10,
                label("Nom"), nom,
                label("Catégorie"), cat,
                label("Prix"), prix,
                label("Image URL"), image,
                label("Description"), desc,
                dispo
        );
        box.setPadding(new Insets(12));
        box.setStyle("-fx-background-color: #0b0d13;");

        dialog.getDialogPane().setContent(box);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.getDialogPane().setStyle("-fx-background-color: #0b0d13;");

        dialog.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.OK) {
                double p;
                try {
                    p = Double.parseDouble(prix.getText().replace(',', '.'));
                } catch (NumberFormatException ex) {
                    showAlert("Erreur", "Prix invalide.");
                    return;
                }
                boolean ok = dao.updatePlat(
                        plat.getId(),
                        nom.getText().trim(),
                        cat.getValue(),
                        p,
                        dispo.isSelected(),
                        image.getText().trim(),
                        desc.getText().trim()
                );
                if (ok) {
                    showAlert("Succès", "Plat mis à jour.");
                    loadData();
                } else {
                    showAlert("Erreur", "Mise à jour impossible.");
                }
            }
        });
    }

    private void styleField(TextField field) {
        field.setStyle("-fx-background-color: #0f121a; -fx-text-fill: #e6ebf3; -fx-border-color: #1f2430; -fx-background-radius: 6px; -fx-border-radius: 6px;");
    }

    private Label label(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-text-fill: #e6ebf3; -fx-font-size: 12px;");
        return l;
    }
}


