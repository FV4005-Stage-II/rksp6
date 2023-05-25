module com.src.rksp6 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires org.apache.commons.io;
    requires xstream;
    requires spark.core;
    requires org.eclipse.jetty.websocket.api;
    requires retrofit2;
    requires retrofit2.converter.scalars;
    requires java.sql;

    opens com.src.rksp6.object;
    exports com.src.rksp6;
    opens com.src.rksp6;
    opens com.src.rksp6.Servers;
    exports com.src.rksp6.Servers;
    exports com.src.rksp6.Clients;
    opens com.src.rksp6.Clients;
}