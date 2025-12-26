module Restau {
    requires transitive java.sql; // re-export so consumers see java.sql types
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    exports restau.app;
    exports restau.ui;
    exports restau.dao;
    exports restau.model;
    exports restau.services;
}

 