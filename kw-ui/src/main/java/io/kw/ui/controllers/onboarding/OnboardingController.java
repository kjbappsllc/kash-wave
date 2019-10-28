package io.kw.ui.controllers.onboarding;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class OnboardingController {

    @FXML
    private Pane pane;

    @FXML
    void closeApp(MouseEvent event) {
        System.exit(0);
    }

    @FXML
    void getStartedPressed(ActionEvent event) {

    }

}
