package io.kw.ui.controllers.onboarding;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import io.kw.ui.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class OnboardingController implements Initializable {

    @FXML
    private Pane pane;

    @FXML
    void closeApp(MouseEvent event) {
        App.closeApplication();
    }

    @FXML
    void minimizeApp(MouseEvent event) {
        App.minimizeApplication();
    }

    @FXML
    void getStartedPressed(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // TODO
    }
}
