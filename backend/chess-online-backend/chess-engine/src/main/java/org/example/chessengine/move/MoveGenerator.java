package org.example.chessengine.move;

import org.example.chessengine.state.Board;
import org.example.chessengine.state.Square;
import org.example.chessengine.pieces.Color;
import org.example.chessengine.pieces.Piece;

import java.util.ArrayList;
import java.util.List;

public class MoveGenerator {

    /**
     * Generate all pseudo-legal moves for the given player.
     * (Does not account for moving into check.)
     */
    public List<Move> generateAllMoves(Board board, Color color) {
        List<Move> allMoves = new ArrayList<>();

        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                Square square = new Square(file, rank);
                Piece piece = board.getPiece(square);

                if (piece != null && piece.getColor() == color) {
                    List<Move> pieceMoves = piece.generateMoves(board, square);
                    allMoves.addAll(pieceMoves);
                }
            }
        }

        return allMoves;
    }
}

