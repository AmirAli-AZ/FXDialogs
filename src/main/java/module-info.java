module FXDialogs {
    requires javafx.controls;
    requires javafx.media;
    requires org.jetbrains.annotations;
    requires java.desktop;
    requires com.google.gson;

    opens com.amirali.fxdialogs.notifications to com.google.gson;
    exports com.amirali.fxdialogs;
}