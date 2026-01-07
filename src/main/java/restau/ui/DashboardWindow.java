package restau.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;

public class DashboardWindow {

    public void show(Stage stage) {
        Label title = new Label("Dashboard");
        title.setStyle("-fx-text-fill: #f5c242; -fx-font-size: 22px; -fx-font-weight: bold;");

        GridPane cards = new GridPane();
        cards.setHgap(16);
        cards.setVgap(16);

        cards.add(card("Total Menu", "325", "\uD83C\uDF7D"), 0, 0);
        cards.add(card("Total Revenue", "$425k", "$"), 1, 0);
        cards.add(card("Total Orders", "415", "\uD83D\uDCBC"), 0, 1);
        cards.add(card("Total Customers", "985", "\uD83D\uDC64"), 1, 1);

        Label trendingTitle = new Label("Trending Items");
        trendingTitle.setStyle("-fx-text-fill: #e9edf4; -fx-font-size: 16px; -fx-font-weight: bold;");

        ListView<String> trending = new ListView<>();
        trending.getItems().addAll(mockTrending());
        trending.setPrefHeight(260);

        Button backBtn = new Button("Retour");
        backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #e9edf4; -fx-border-color: #2f3747; -fx-border-radius: 6px;");
        backBtn.setOnAction(e -> new MainWindow().show(stage));

        VBox panel = new VBox(16, title, cards, trendingTitle, trending, backBtn);
        panel.setPadding(new Insets(20));
        panel.setStyle("-fx-background-color: rgba(20,22,29,0.92); -fx-background-radius: 14px;");
        panel.setMaxWidth(820);

        VBox menu = AdminNavFactory.create(stage);
        menu.setMinWidth(220);
        menu.setMaxWidth(220);

        HBox layout = new HBox(16, menu, panel);
        layout.setAlignment(Pos.CENTER_LEFT);
        layout.setPadding(new Insets(28));

        StackPane root = new StackPane(layout);
        root.setStyle("""
            -fx-background-image: url('https://images.unsplash.com/photo-1467003909585-2f8a72700288?auto=format&fit=crop&w=1600&q=80');
            -fx-background-size: cover;
            -fx-background-position: center center;
            -fx-background-color: rgba(0,0,0,0.45);
            -fx-background-blend-mode: overlay;
        """);

        Scene scene = new Scene(root, 1100, 640);
        stage.setTitle("Dashboard - Arc Burger");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setWidth(1100);
        stage.setHeight(640);
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.show();
    }


    private VBox card(String title, String value, String icon) {
        Label t = new Label(title);
        t.setStyle("-fx-text-fill: #c7ced7; -fx-font-size: 14px; -fx-font-weight: bold;");
        Label v = new Label(value + "  " + icon);
        v.setStyle("-fx-text-fill: #e9edf4; -fx-font-size: 22px; -fx-font-weight: bold;");

        VBox box = new VBox(6, t, v);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(14));
        box.setStyle("-fx-background-color: rgba(34,38,48,0.95); -fx-background-radius: 12px; -fx-border-color: #2f3745; -fx-border-radius: 12px;");
        box.setPrefWidth(200);
        return box;
    }

    private List<String> mockTrending() {
        return Arrays.asList(
                "#1  Biryani Pulav   $12.00   Main Course   ↑158 ventes (+20%)",
                "#2  Burgers         $42.00   Snack         ↓18 ventes (-0.5%)",
                "#3  Dal Palak       $60.00   Main Course   ↑258 ventes (+15%)",
                "#4  Pan Noodles     $10.00   Snack         ↑58 ventes (+3%)"
        );
    }
}

