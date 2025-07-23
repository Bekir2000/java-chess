package org.example.chessengine.move;

import org.example.chessengine.state.Board;
import org.example.chessengine.state.Game;
import org.example.chessengine.state.Square;
import org.example.chessengine.pieces.Color;
import org.example.chessengine.pieces.Piece;

import java.util.ArrayList;
import java.util.List;

public class MoveGenerator {


    public List<Move> generateLegalMoves(Game game) {
        List<Move> legalMoves = new ArrayList<>();
        Color currentColor = game.getTurn();

        List<Move> pseudoMoves = generatePseudoLegalMoves(game.getBoard(), currentColor);

        for (Move move : pseudoMoves) {
            game.makeMove(move);
            boolean kingSafe = !game.isInCheck(currentColor);
            game.undoMove();

            if (kingSafe) {
                legalMoves.add(move);
            }
        }

        return legalMoves;
    }

    public List<Move> generatePseudoLegalMoves(Board board, Color color) {
        List<Move> moves = new ArrayList<>();

        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                Square square = new Square(file, rank);
                Piece piece = board.getPiece(square);

                if (piece != null && piece.getColor() == color) {
                    moves.addAll(piece.generateMoves(board, square));
                }
            }
        }

        return moves;
    }
}

