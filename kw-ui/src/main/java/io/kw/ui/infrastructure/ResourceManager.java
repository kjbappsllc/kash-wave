package io.kw.ui.infrastructure;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.net.URL;

@ApplicationScoped
public class ResourceManager {

    @Inject
    FXMLLoader fxmlLoader;

    public Parent loadView(String filename) {
        fxmlLoader.setLocation(getResource("views/" + filename + ".fxml"));
        try {
            return fxmlLoader.load();
        } catch (Exception e) {
            System.out.println("Error loading FXML file " + filename);
        }
        return null;
    }

    private URL getResource(String filename) {
        ClassLoader classLoader = ResourceManager.class.getClassLoader();
        URL resource = classLoader.getResource(filename);
        if (resource == null) {
            throw new IllegalArgumentException("Resource " + filename + " cannot be found");
        }
        return resource;
    }
}
