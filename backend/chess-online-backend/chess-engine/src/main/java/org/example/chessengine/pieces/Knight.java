package org.example.chessengine.pieces;

import org.example.chessengine.board.Board;
import org.example.chessengine.board.Square;
import org.example.chessengine.move.Move;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {

    private static final int[][] DIRECTIONS = {
            {1, 2}, {2, 1}, {2, -1}, {1, -2},
            {-1, -2}, {-2, -1}, {-2, 1}, {-1, 2}
    };

    public Knight(Color color) {
        super(color, PieceType.KNIGHT);
    }

    @Override
    public List<Move> generateMoves(Board board, Square from) {
        List<Move> moves = new ArrayList<>();
        for (int[] d : DIRECTIONS) {
            int file = from.file() + d[0];
            int rank = from.rank() + d[1];
            Square target = new Square(file, rank);
            if (target.isValid()) {
                Piece targetPiece = board.getPiece(target);
                if (targetPiece == null || targetPiece.getColor() != this.color) {
                    moves.add(new Move(from, target));
                }
            }
        }
        return moves;
    }
}

