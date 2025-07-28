package org.example.chessgui.app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.example.chessgui.controller.BoardController;
import org.example.chessgui.view.BoardView;


public class ChessGuiApp extends Application {

    private static final int TILE = 80;
    private static final int SIZE = 8;

    @Override
    public void start(Stage stage) {
        BoardView boardView = new BoardView(TILE, SIZE);
        BoardController controller = new BoardController(boardView);

        StackPane root = new StackPane(boardView.getBoard(), boardView.getDragLayer());
        root.getChildren().add(boardView.getMsgLayer());

        Scene scene = new Scene(root, TILE * SIZE, TILE * SIZE);
        scene.getStylesheets().add(getClass().getResource("/css/chess.css").toExternalForm());

        scene.addEventHandler(MouseEvent.MOUSE_RELEASED, controller::onDrop);

        stage.setTitle("Chess");
        stage.setScene(scene);
        stage.show();

        controller.drawBoard();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

