package io.kw.ui.infrastructure;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;

@ApplicationScoped
public class ResourceManager {

    @Inject
    FXMLLoader fxmlLoader;

    public Parent loadFXML(String filename) throws IOException {
        fxmlLoader.setLocation(getResource("/fxml/" + filename + ".fxml"));
        return fxmlLoader.load();
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
