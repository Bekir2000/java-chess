package org.example.chessengine.pieces;

import org.example.chessengine.board.Board;
import org.example.chessengine.board.Square;
import org.example.chessengine.move.Move;
import org.example.chessengine.move.MoveType;

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
        int promotionRank = (color == Color.WHITE) ? 7 : 0;

        // One step forward
        Square oneStep = new Square(from.file(), from.rank() + direction);
        if (oneStep.isValid() && board.isEmpty(oneStep)) {
            MoveType type = oneStep.rank() == promotionRank ? MoveType.PROMOTION : MoveType.NORMAL;
            moves.add(new Move(from, oneStep, type));

            // Two steps from starting rank
            boolean onStartRank = (color == Color.WHITE && from.rank() == 1)
                    || (color == Color.BLACK && from.rank() == 6);
            Square twoSteps = new Square(from.file(), from.rank() + 2 * direction);
            if (onStartRank && board.isEmpty(twoSteps)) {
                moves.add(new Move(from, twoSteps, MoveType.NORMAL));
            }
        }

        // Captures
        int[] fileOffsets = {-1, 1};
        for (int offset : fileOffsets) {
            Square diag = new Square(from.file() + offset, from.rank() + direction);
            if (diag.isValid()) {
                Piece target = board.getPiece(diag);
                if (target != null && target.getColor() != this.color) {
                    MoveType type = diag.rank() == promotionRank ? MoveType.PROMOTION : MoveType.CAPTURE;
                    moves.add(new Move(from, diag, type));
                }

                // TODO: En Passant
            }
        }

        return moves;
    }

    @Override
    public boolean isValidMove(Board board, Move move) {
        Square from = move.from();
        Square to = move.to();

        int df = to.file() - from.file();
        int dr = to.rank() - from.rank();
        int direction = (color == Color.WHITE) ? 1 : -1;
        int startRank = (color == Color.WHITE) ? 1 : 6;
        int promotionRank = (color == Color.WHITE) ? 7 : 0;

        if (!to.isValid()) return false;

        Piece target = board.getPiece(to);

        // Move forward by 1
        if (df == 0 && dr == direction) {
            if (target == null) return true;
        }

        // Move forward by 2 from starting rank
        if (df == 0 && dr == 2 * direction && from.rank() == startRank) {
            Square intermediate = new Square(from.file(), from.rank() + direction);
            if (target == null && board.isEmpty(intermediate)) return true;
        }

        // Capture diagonally
        if (Math.abs(df) == 1 && dr == direction) {
            if (target != null && target.getColor() != this.color) return true;

            // TODO: En passant
        }

        return false;
    }

    @Override
    public char toFenChar() {
        return (color == Color.WHITE) ? 'P' : 'p';
    }
}

