module com.src.rksp6 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires org.apache.commons.io;
    requires xstream;

    opens com.src.rksp6.object;
    exports com.src.rksp6;
    opens com.src.rksp6;
}