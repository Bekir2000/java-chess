package org.example.chessgui.view;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.example.chessengine.pieces.Piece;
import org.example.chessengine.state.Square;

public class BoardView {

    private final int tileSize;
    private final int boardSize;

    private final GridPane board;
    private final Pane dragLayer;
    private final StackPane msgLayer;

    public BoardView(int tileSize, int boardSize) {
        this.tileSize = tileSize;
        this.boardSize = boardSize;

        board = new GridPane();
        board.getStyleClass().add("board-grid");
        board.setPrefSize(tileSize * boardSize, tileSize * boardSize);

        dragLayer = new Pane();
        dragLayer.setPickOnBounds(false);
        dragLayer.setManaged(false);

        msgLayer = new StackPane();
        msgLayer.setPickOnBounds(false);
        msgLayer.setVisible(false);
    }

    public GridPane getBoard() {
        return board;
    }

    public Pane getDragLayer() {
        return dragLayer;
    }

    public StackPane getMsgLayer() {
        return msgLayer;
    }

    public int getTileSize() {
        return tileSize;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public StackPane cellAt(int file, int rank) {
        for (Node n : board.getChildren()) {
            Integer col = GridPane.getColumnIndex(n);
            Integer row = GridPane.getRowIndex(n);
            if (col != null && row != null && col == file && row == 7 - rank) {
                return (StackPane) n;
            }
        }
        return null;
    }

    public ImageView pieceImage(Piece piece) {
        // We'll move the full logic from ChessGui later if needed
        return null; // Placeholder for now
    }
}

