package io.kw.ui.infrastructure;

import io.kw.ui.cdi.FXMLLoaderFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;

@ApplicationScoped
public class ResourceManager {

    @Inject
    Instance<Object> instance;

    public Parent loadView(String filename) {
        try {
            URL styleSheets = getClass().getClassLoader().getResource("views/Main.css");
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
            throw new Exception("Unable to construct view " + filename);
        } catch (Exception e) {
            System.out.println("Error loading FXML file " + filename + ", Error: " + e.getMessage());
        }
        return null;
    }

    private URL getResource(String filename) {
        ClassLoader classLoader = ResourceManager.class.getClassLoader();
        return classLoader.getResource(filename);
    }

    private FXMLLoader createDefaultFXMLLoader() {
        return new FXMLLoader();
    }
}
