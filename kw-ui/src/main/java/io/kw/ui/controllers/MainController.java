package io.kw.ui.controllers;

import io.kw.ui.infrastructure.ResourceManager;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @Inject
    ResourceManager resourceManager;

    @FXML
    private VBox vbox;

    @FXML
    void launchSignIn(ActionEvent event) {
        transitionMask(0.45, vbox.getLayoutX() * 21, "SignIn");
    }

    @FXML
    void launchSignUp(ActionEvent event) {
        transitionMask(0.45, 14, "SignUp");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadViewToVBox("SignUp");
    }

    private void transitionMask(double duration, double offset, String view) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(duration), vbox);
        translateTransition.setToX(offset);
        translateTransition.setInterpolator(Interpolator.EASE_OUT);
        translateTransition.play();
        translateTransition.setOnFinished((e) -> {
            loadViewToVBox(view);
        });
    }

    private void loadViewToVBox(String view) {
        Parent currentView = resourceManager.loadView(view);
        vbox.getChildren().removeAll();
        vbox.getChildren().setAll(currentView);
    }
}