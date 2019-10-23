package io.kw.ui;


import io.kw.ui.cdi.qualifiers.AppInitialized;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;

public class App {

    @Inject
    FXMLLoader fxmlLoader;

    private void launch(@Observes @AppInitialized Stage stage) {
        try {
            URL fxml = getClass().getClassLoader().getResource("views/Main.fxml");
            URL styleSheets = getClass().getClassLoader().getResource("views/Main.css");
            if (fxml != null && styleSheets != null) {
                Parent main = fxmlLoader.load(fxml.openStream());
                main.getStylesheets().add(styleSheets.toString());
                Scene scene = new Scene(main);
                scene.setFill(Color.TRANSPARENT);
                stage.setScene(scene);
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.show();
            } else {
                System.out.println("Unable to load Stylesheets or Main Fxml");
                System.exit(1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
