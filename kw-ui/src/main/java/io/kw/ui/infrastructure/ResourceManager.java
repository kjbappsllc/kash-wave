package io.kw.ui.infrastructure;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javax.inject.Inject;
import java.net.URL;

public class ResourceManager {

    @Inject
    FXMLLoader loader;

    public Parent loadView(String filename) {
        try {
            Parent view = loader.load(getResource("views/" + filename + ".fxml"));
            return view;
        } catch (Exception e) {
            System.out.println("Error loading FXML file " + filename + ", Error: " + e.getMessage());
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
