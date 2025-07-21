package org.example.chessengine.pieces;

import org.example.chessengine.board.Board;
import org.example.chessengine.board.Square;
import org.example.chessengine.move.Move;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {

    private static final int[][] DIRECTIONS = {
            {1, 1}, {1, -1}, {-1, 1}, {-1, -1} // diagonals
    };

    public Bishop(Color color) {
        super(color, PieceType.BISHOP);
    }

    @Override
    public List<Move> generateMoves(Board board, Square from) {
        List<Move> moves = new ArrayList<>();

        for (int[] direction : DIRECTIONS) {
            int file = from.file();
            int rank = from.rank();

            while (true) {
                file += direction[0];
                rank += direction[1];
                Square target = new Square(file, rank);

                if (!target.isValid()) break;

                Piece targetPiece = board.getPiece(target);
                if (targetPiece == null) {
                    moves.add(new Move(from, target));
                } else {
                    if (targetPiece.getColor() != this.color) {
                        moves.add(new Move(from, target));
                    }
                    break;
                }
            }
        }

        return moves;
    }
}
