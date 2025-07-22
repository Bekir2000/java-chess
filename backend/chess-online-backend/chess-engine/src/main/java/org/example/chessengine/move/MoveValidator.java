package org.example.chessengine.move;

import org.example.chessengine.pieces.Piece;
import org.example.chessengine.state.Game;

public class MoveValidator {

    public boolean isLegalMove(Game game, Move move) {
        Piece piece = game.getBoard().getPiece(move.from());
        if (piece == null || piece.getColor() != game.getTurn()) return false;

        if (!piece.isValidMove(game.getBoard(), move)) return false;

        // Temporarily make the move
        game.makeMove(move);
        boolean kingIsInCheck = game.isInCheck(game.getTurn().opposite());
        game.undoMove(); // Restore state

        return !kingIsInCheck;
    }
}


