package org.example.chessengine.pieces;

import org.example.chessengine.state.Board;
import org.example.chessengine.state.Square;
import org.example.chessengine.move.Move;
import org.example.chessengine.move.MoveType;

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

            if (!target.isValid()) continue;

            Piece targetPiece = board.getPiece(target);
            if (targetPiece == null) {
                moves.add(new Move(from, target, MoveType.NORMAL));
            } else if (targetPiece.getColor() != this.color) {
                moves.add(new Move(from, target, MoveType.CAPTURE));
            }
        }

        return moves;
    }

    @Override
    public boolean isValidMove(Board board, Move move) {
        Square from = move.from();
        Square to = move.to();

        int df = Math.abs(from.file() - to.file());
        int dr = Math.abs(from.rank() - to.rank());

        if (!to.isValid()) return false;

        boolean isKnightJump = (df == 2 && dr == 1) || (df == 1 && dr == 2);
        if (!isKnightJump) return false;

        Piece target = board.getPiece(to);
        return target == null || target.getColor() != this.color;
    }

    @Override
    public char toFenChar() {
        return (color == Color.WHITE) ? 'N' : 'n';
    }
}


