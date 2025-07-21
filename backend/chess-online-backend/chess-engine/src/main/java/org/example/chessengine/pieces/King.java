package org.example.chessengine.pieces;

import org.example.chessengine.board.Board;
import org.example.chessengine.board.Square;
import org.example.chessengine.move.Move;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {

    private static final int[][] DIRECTIONS = {
            {1, 0}, {-1, 0}, {0, 1}, {0, -1},
            {1, 1}, {-1, -1}, {1, -1}, {-1, 1}
    };

    public King(Color color) {
        super(color, PieceType.KING);
    }

    @Override
    public List<Move> generateMoves(Board board, Square from) {
        List<Move> moves = new ArrayList<>();

        for (int[] direction : DIRECTIONS) {
            int file = from.file() + direction[0];
            int rank = from.rank() + direction[1];
            Square target = new Square(file, rank);

            if (!target.isValid()) continue;

            Piece targetPiece = board.getPiece(target);
            if (targetPiece == null || targetPiece.getColor() != this.color) {
                moves.add(new Move(from, target));
            }
        }

        // TODO: Add castling support

        return moves;
    }
}

