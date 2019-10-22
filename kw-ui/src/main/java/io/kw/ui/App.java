package io.kw.ui;


import io.kw.ui.cdi.qualifiers.Initialized;
import io.kw.ui.infrastructure.ResourceManager;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

public class App {

    @Inject
    ResourceManager resourceManager;

    private void launch(@Observes @Initialized Stage stage) {
        Parent main = resourceManager.loadFXML("primary");
        if (main == null) System.exit(1);
        Scene scene = new Scene(main);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
    }
}
