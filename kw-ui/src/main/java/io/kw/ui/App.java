package io.kw.ui;

import io.kw.ui.cdi.AppInitialized;
import io.kw.ui.infrastructure.ResourceManager;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class App {

    private static final Rectangle2D SCREEN_BOUNDS = Screen.getPrimary().getVisualBounds();
    private static Stage mainStage;

    @Inject
    ResourceManager resourceManager;

    private void launch(@Observes @AppInitialized Stage stage) {
        mainStage = stage;
        Parent main = resourceManager.loadView("Onboarding");
        if (main == null) System.exit(100);
        Scene scene = new Scene(main);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        setViewAsDraggable(main);
        stage.show();
    }

    public static Stage getMainStage() {
        return mainStage;
    }

    public static void setViewAsDraggable(Parent root) {
        AtomicReference<AtomicReferenceArray<Double>> offset_XY = new AtomicReference<>(new AtomicReferenceArray<>(new Double[0]));
        root.setOnMousePressed((MouseEvent p) -> {
            offset_XY.set(new AtomicReferenceArray<>(new Double[]{p.getSceneX(), p.getSceneY()}));
        });

        root.setOnMouseDragged((MouseEvent d) -> {
            mainStage.setOpacity(0.95);
            if (d.getScreenY() < (SCREEN_BOUNDS.getMaxY() - 20))
                mainStage.setY(d.getScreenY() - offset_XY.get().get(1));
            mainStage.setX(d.getScreenX() - offset_XY.get().get(0));
        });

        root.setOnMouseReleased((MouseEvent r) -> {
            mainStage.setOpacity(1.0);
            if (mainStage.getY() < 0.0) mainStage.setY(0.0);
        });
    }

    public static void minimizeApp() {
        mainStage.setIconified(true);
    }
}
