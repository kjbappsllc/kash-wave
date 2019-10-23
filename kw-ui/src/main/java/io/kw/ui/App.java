package io.kw.ui;


import io.kw.ui.cdi.AppInitialized;
import io.kw.ui.infrastructure.ResourceManager;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

public class App {

    @Inject
    ResourceManager resourceManager;

    private void launch(@Observes @AppInitialized Stage stage) {
        Parent main = resourceManager.loadView("LoginMain");
        if (main == null) System.exit(100);
        Scene scene = new Scene(main);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
    }
}
