package io.kw.ui.bootstrap;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;
import java.io.IOException;
import java.net.URL;

/**
 * JavaFX App
 */
public class UIBootstrap extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        final SeContainer container = SeContainerInitializer.newInstance().initialize();
        scene = new Scene(loadFXML("primary"));
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    private static URL getResource(String filename) {
        ClassLoader classLoader = UIBootstrap.class.getClassLoader();
        URL resource = classLoader.getResource(filename);
        if (resource == null) {
            throw new IllegalArgumentException("Resource " + filename + " cannot be found");
        }
        return resource;
    }

    public static void main(String[] args) {
        launch();
    }

}