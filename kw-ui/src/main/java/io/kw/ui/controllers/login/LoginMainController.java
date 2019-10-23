package io.kw.ui.controllers.login;

import io.kw.ui.infrastructure.ResourceManager;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginMainController implements Initializable {

    @Inject
    ResourceManager resourceManager;

    @FXML
    private VBox vbox;

    @FXML
    private Pane pane;

    private static int OFFSET = 35;

    @FXML
    void launchSignIn(ActionEvent event) {
        transitionMask((pane.getWidth() - vbox.getWidth()) - OFFSET, "SignIn");
    }

    @FXML
    void launchSignUp(ActionEvent event) { transitionMask(OFFSET, "SignUp"); }

    @FXML
    void closeApp(MouseEvent event) { System.exit(0); }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        transitionMask(OFFSET, "SignUp");
    }

    private void transitionMask(double offset, String view) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.45), vbox);
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