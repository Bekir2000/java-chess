package org.example.chessengine.engine;

import org.example.chessengine.state.Board;
import org.example.chessengine.pieces.*;

public class Evaluator {

    public int evaluate(Board board) {
        int score = 0;

        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                Piece piece = board.getPiece(file, rank);
                if (piece != null) {
                    int value = switch (piece.getType()) {
                        case PAWN -> 100;
                        case KNIGHT -> 320;
                        case BISHOP -> 330;
                        case ROOK -> 500;
                        case QUEEN -> 900;
                        case KING -> 20000;
                    };
                    score += piece.getColor() == Color.WHITE ? value : -value;
                }
            }
        }

        return score;
    }
}

