package io.kw.ui.bootstrap;

import io.kw.ui.cdi.AppInitialized;
import javafx.application.Application;
import javafx.stage.Stage;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;
import javax.enterprise.util.AnnotationLiteral;

/**
 * JavaFX App
 */
public class UIBootstrap extends Application {

    private SeContainer container;

    @Override
    public void start(Stage stage) {
        container = SeContainerInitializer.newInstance().initialize();
        container.getBeanManager().fireEvent(stage, new AnnotationLiteral<AppInitialized>() {});
    }

    @Override
    public void stop() {
        container.close();
    }

    public static void main(String[] args) {
        launch();
    }

}