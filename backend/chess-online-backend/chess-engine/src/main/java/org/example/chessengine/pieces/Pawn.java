package org.example.chessengine.pieces;

import org.example.chessengine.board.Board;
import org.example.chessengine.board.Square;
import org.example.chessengine.move.Move;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {

    public Pawn(Color color) {
        super(color, PieceType.PAWN);
    }

    @Override
    public List<Move> generateMoves(Board board, Square from) {
        List<Move> moves = new ArrayList<>();
        int direction = (color == Color.WHITE) ? 1 : -1;

        // One step forward
        Square oneStep = new Square(from.file(), from.rank() + direction);
        if (oneStep.isValid() && board.isEmpty(oneStep)) {
            moves.add(new Move(from, oneStep));

            // Two steps from start
            boolean onStartRank = (color == Color.WHITE && from.rank() == 1)
                    || (color == Color.BLACK && from.rank() == 6);

            Square twoSteps = new Square(from.file(), from.rank() + 2 * direction);
            if (onStartRank && board.isEmpty(twoSteps)) {
                moves.add(new Move(from, twoSteps));
            }
        }

        // Captures
        int[] fileOffsets = {-1, 1};
        for (int offset : fileOffsets) {
            Square diag = new Square(from.file() + offset, from.rank() + direction);
            if (diag.isValid()) {
                Piece targetPiece = board.getPiece(diag);
                if (targetPiece != null && targetPiece.getColor() != this.color) {
                    moves.add(new Move(from, diag));
                }
                // TODO: handle en passant
            }
        }

        // TODO: Add promotion

        return moves;
    }
}

