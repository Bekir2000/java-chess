package org.example.chessengine.engine;

import org.example.chessengine.state.Game;
import org.example.chessengine.pieces.*;

public class Evaluator {

    public int evaluate(Game game) {
        int score = 0;

        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                Piece piece = game.getBoard().getPiece(new org.example.chessengine.state.Square(file, rank));
                if (piece != null) {
                    int value = getPieceValue(piece.getType());
                    score += piece.getColor() == Color.WHITE ? value : -value;
                }
            }
        }

        return score;
    }

    private int getPieceValue(PieceType type) {
        return switch (type) {
            case PAWN -> 100;
            case KNIGHT, BISHOP -> 300;
            case ROOK -> 500;
            case QUEEN -> 900;
            case KING -> 10000;
        };
    }
}

