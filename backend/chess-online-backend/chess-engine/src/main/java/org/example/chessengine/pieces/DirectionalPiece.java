package org.example.chessengine.pieces;

import org.example.chessengine.board.Board;
import org.example.chessengine.board.Square;
import org.example.chessengine.move.Move;
import org.example.chessengine.move.MoveType;

import java.util.ArrayList;
import java.util.List;

public abstract class DirectionalPiece extends Piece {

    protected final int[][] directions;
    protected final int maxSteps;

    protected DirectionalPiece(Color color, PieceType type, int[][] directions, int maxSteps) {
        super(color, type);
        this.directions = directions;
        this.maxSteps = maxSteps;
    }

    @Override
    public boolean isValidMove(Board board, Move move) {
        Square from = move.getFrom();
        Square to = move.getTo();

        int dFile = to.file() - from.file();
        int dRank = to.rank() - from.rank();

        for (int[] dir : directions) {
            int fStep = dir[0], rStep = dir[1];
            for (int step = 1; step <= maxSteps; step++) {
                int f = from.file() + fStep * step;
                int r = from.rank() + rStep * step;
                Square current = new Square(f, r);
                if (!current.isValid()) break;

                if (f == to.file() && r == to.rank()) {
                    var target = board.getPiece(to);
                    return target == null || target.getColor() != color;
                }

                if (board.getPiece(current) != null) break;
            }
        }

        return false;
    }

    @Override
    public List<Move> generateMoves(Board board, Square from) {
        List<Move> moves = new ArrayList<>();

        for (int[] dir : directions) {
            int f = from.file();
            int r = from.rank();
            for (int step = 1; step <= maxSteps; step++) {
                f += dir[0];
                r += dir[1];
                Square to = new Square(f, r);
                if (!to.isValid()) break;

                var target = board.getPiece(to);
                if (target == null) {
                    moves.add(new Move(from, to, MoveType.NORMAL));
                } else {
                    if (target.getColor() != color) {
                        moves.add(new Move(from, to, MoveType.CAPTURE));
                    }
                    break;
                }
            }
        }

        return moves;
    }
}
