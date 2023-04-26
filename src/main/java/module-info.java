module sicof.main {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens sicof.main to javafx.fxml;
    exports sicof.main;
    exports sicof.controller;
    opens sicof.controller to javafx.fxml;
    exports sicof.model;
    opens sicof.model to javafx.fxml;
    exports sicof.helpers;
    opens sicof.helpers to javafx.fxml;
}