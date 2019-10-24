package io.kw.ui.infrastructure;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.net.URL;

@ApplicationScoped
public class ResourceManager {

    @Inject
    Instance<Object> instance;

    public Parent loadView(String filename) {
        try {
            URL styleSheets = getResource("views/Main.css");
            URL fxml = getResource("views/" + filename + ".fxml");
            if (styleSheets != null && fxml != null) {
                Parent view = FXMLLoader.load(
                        fxml,
                        null,
                        null,
                        param -> instance.select(param).get()
                );
                view.getStylesheets().add(styleSheets.toString());
                return view;
            }
        } catch (Exception e) {
            System.out.println("Error loading FXML file " + filename + ", Error: " + e.getMessage());
        }
        System.out.println("Could not load FXML file " + filename);
        return null;
    }

    private URL getResource(String filename) {
        ClassLoader classLoader = ResourceManager.class.getClassLoader();
        return classLoader.getResource(filename);
    }
}
