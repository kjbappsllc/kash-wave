package io.kw.ui.gui.controllers.onboarding;

import io.kw.ui.gui.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

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
