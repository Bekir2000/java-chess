package org.example.chessgui.controller;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;
import org.example.chessengine.engine.Evaluator;
import org.example.chessengine.engine.Search;
import org.example.chessengine.move.Move;
import org.example.chessengine.move.MoveGenerator;
import org.example.chessengine.pieces.Color;
import org.example.chessengine.pieces.Piece;
import org.example.chessengine.state.Game;
import org.example.chessengine.state.Square;
import org.example.chessgui.view.BoardView;

import java.util.ArrayList;
import java.util.List;

public class BoardController {

    private final Game game = new Game("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    private final BoardView view;

    private Square selectedSq;
    private StackPane selectedPane;
    private ImageView dragImg;
    private final List<Node> hintDots = new ArrayList<>();
    private final AudioClip moveSfx;

    public BoardController(BoardView view) {
        this.view = view;
        this.moveSfx = new AudioClip(getClass().getResource("/sounds/Move.mp3").toExternalForm());
    }

    public void drawBoard() {
        view.getBoard().getChildren().clear();
        int size = view.getBoardSize();
        int tile = view.getTileSize();

        for (int r = 7; r >= 0; r--) {
            for (int f = 0; f < size; f++) {
                Square sq = new Square(f, r);
                Region tileRegion = new Region();
                tileRegion.setPrefSize(tile, tile);
                tileRegion.getStyleClass().addAll("tile", ((f + r) & 1) == 0 ? "light" : "dark");

                StackPane cell = new StackPane(tileRegion);
                if (sq.equals(selectedSq)) {
                    cell.getStyleClass().add("selected");
                }

                Piece p = game.getBoard().getPiece(sq);
                if (p != null) {
                    ImageView img = pieceView(p);
                    if (img != null) {
                        cell.getChildren().add(img);
                        img.setOnMousePressed(e -> onDragStart(e, sq, img, cell));
                        img.setOnMouseDragged(this::onDrag);
                    }
                }

                view.getBoard().add(cell, f, 7 - r);
            }
        }
    }

    private void onDragStart(MouseEvent e, Square from, ImageView srcImg, StackPane cell) {
        Piece pc = game.getBoard().getPiece(from);
        if (pc.getColor() != game.getTurn()) return;

        selectedSq = from;
        selectedPane = cell;
        cell.getStyleClass().add("selected");

        srcImg.setVisible(false);

        dragImg = new ImageView(srcImg.getImage());
        dragImg.setFitWidth(srcImg.getFitWidth());
        dragImg.setFitHeight(srcImg.getFitHeight());
        view.getDragLayer().getChildren().add(dragImg);

        Point2D p = srcImg.localToScene(e.getX(), e.getY());
        dragImg.relocate(p.getX() - dragImg.getFitWidth() / 2, p.getY() - dragImg.getFitHeight() / 2);

        showHints(from);
        e.consume();
    }

    private void onDrag(MouseEvent e) {
        if (dragImg != null) {
            dragImg.relocate(
                    e.getSceneX() - dragImg.getFitWidth() / 2,
                    e.getSceneY() - dragImg.getFitHeight() / 2
            );
        }
    }

    public void onDrop(MouseEvent e) {
        if (dragImg == null || selectedSq == null) return;

        Point2D o = view.getBoard().localToScene(0, 0);
        int file = (int) ((e.getSceneX() - o.getX()) / view.getTileSize());
        int rank = view.getBoardSize() - 1 - (int) ((e.getSceneY() - o.getY()) / view.getTileSize());

        boolean moved = false;
        if (file >= 0 && file < 8 && rank >= 0 && rank < 8) {
            Move m = new Move(selectedSq, new Square(file, rank));
            moved = game.tryMove(m);
            if (moved) {
                animateMove(m);
                moveSfx.play();
            }
        }

        view.getDragLayer().getChildren().clear();
        hintDots.forEach(d -> ((Pane) d.getParent()).getChildren().remove(d));
        hintDots.clear();
        if (selectedPane != null) selectedPane.getStyleClass().remove("selected");

        selectedSq = null;
        selectedPane = null;
        dragImg = null;

        if (moved && game.isGameOver()) {
            drawBoard();
            showGameOver(game.getGameResult());
        }

        if (moved && game.getTurn() == Color.BLACK && !game.isGameOver()) {
            Move botMove = new Search(new Evaluator()).findBestMove(game, 5);
            if (botMove != null) {
                game.tryMove(botMove);
                moveSfx.play();
                animateMove(botMove);
                if (game.isGameOver()) {
                    drawBoard();
                    showGameOver(game.getGameResult());
                }
            }
        } else if (!moved) {
            drawBoard();
        }

        e.consume();
    }

    private void showHints(Square from) {
        MoveGenerator gen = new MoveGenerator();
        for (Move mv : gen.generateLegalMoves(game)) {
            if (!mv.from().equals(from)) continue;

            Square to = mv.to();
            StackPane cell = view.cellAt(to.file(), to.rank());
            if (cell == null) continue;

            Region dot = new Region();
            dot.getStyleClass().add("hint");
            cell.getChildren().add(dot);
            hintDots.add(dot);
        }
    }

    private void animateMove(Move move) {
        StackPane fromCell = view.cellAt(move.from().file(), move.from().rank());
        StackPane toCell = view.cellAt(move.to().file(), move.to().rank());
        if (fromCell == null || toCell == null) return;

        for (Node n : fromCell.getChildren()) {
            if (n instanceof ImageView img) {
                ImageView ghost = new ImageView(img.getImage());
                ghost.setFitWidth(img.getFitWidth());
                ghost.setFitHeight(img.getFitHeight());
                ghost.setPreserveRatio(true);
                ghost.setSmooth(false);

                Point2D start = fromCell.localToScene(0, 0);
                Point2D end = toCell.localToScene(0, 0);

                ghost.setTranslateX(start.getX() - view.getBoard().getLayoutX());
                ghost.setTranslateY(start.getY() - view.getBoard().getLayoutY());
                view.getDragLayer().getChildren().add(ghost);

                TranslateTransition tt = new TranslateTransition(Duration.millis(200), ghost);
                tt.setToX(end.getX() - view.getBoard().getLayoutX());
                tt.setToY(end.getY() - view.getBoard().getLayoutY());
                tt.setOnFinished(evt -> {
                    view.getDragLayer().getChildren().remove(ghost);
                    drawBoard();
                });
                tt.play();
                break;
            }
        }
    }

    private ImageView pieceView(Piece pc) {
        if (pc == null) return null;
        String c = pc.getColor() == Color.WHITE ? "w" : "b";
        String t = switch (pc.getType()) {
            case PAWN -> "P";
            case ROOK -> "R";
            case KNIGHT -> "N";
            case BISHOP -> "B";
            case QUEEN -> "Q";
            case KING -> "K";
        };
        ImageView v = new ImageView(new Image(
                getClass().getResourceAsStream("/images/pieces/" + c + t + ".png")));
        v.setFitWidth(view.getTileSize() * 0.8);
        v.setFitHeight(view.getTileSize() * 0.8);
        v.setPreserveRatio(true);
        v.setSmooth(false);
        v.getStyleClass().add("piece-image");
        return v;
    }

    private void showGameOver(String text) {
        StackPane msgLayer = view.getMsgLayer();
        msgLayer.getChildren().clear();

        Region backdrop = new Region();
        backdrop.setId("msg-layer");
        backdrop.setPrefSize(view.getBoard().getPrefWidth(), view.getBoard().getPrefHeight());

        Label label = new Label(text);
        label.setId("game-over-label");

        StackPane card = new StackPane(label);
        card.setId("game-over-card");

        msgLayer.getChildren().addAll(backdrop, card);
        msgLayer.setVisible(true);
        msgLayer.setOpacity(0);

        FadeTransition ft = new FadeTransition(Duration.millis(400), msgLayer);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();

        msgLayer.setOnMouseClicked(e -> msgLayer.setVisible(false));
    }
}

