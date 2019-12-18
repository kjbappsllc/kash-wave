package io.kw.ui.gui.bootstrap;

import io.kw.ui.gui.cdi.GuiInitialized;
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
        container.getBeanManager().fireEvent(stage, new AnnotationLiteral<GuiInitialized>() {});
    }

    @Override
    public void stop() {
        container.close();
    }

    public static void main(String[] args) {
        launch();
    }
}