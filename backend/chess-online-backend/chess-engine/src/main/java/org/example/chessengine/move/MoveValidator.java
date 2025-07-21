package org.example.chessengine.move;

import org.example.chessengine.pieces.Color;
import org.example.chessengine.state.Game;

import java.util.ArrayList;
import java.util.List;

public class MoveValidator {

    public List<Move> generateLegalMoves(Game game, Color color) {
        List<Move> legalMoves = new ArrayList<>();
        MoveGenerator generator = new MoveGenerator();
        List<Move> pseudoMoves = generator.generateAllMoves(game.getBoard(), color);

        for (Move move : pseudoMoves) {
            game.makeMove(move);
            if (!game.isInCheck(color)) {
                legalMoves.add(move);
            }
            game.undoMove();
        }

        return legalMoves;
    }
}

