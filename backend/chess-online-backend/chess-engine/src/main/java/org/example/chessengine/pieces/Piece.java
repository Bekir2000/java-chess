package org.example.chessengine.pieces;

import org.example.chessengine.board.Square;
import org.example.chessengine.board.Board;
import org.example.chessengine.move.Move;

import java.util.List;

public abstract class Piece {
    protected final Color color;
    protected final PieceType type;

    protected Piece(Color color, PieceType type) {
        this.color = color;
        this.type = type;
    }

    public Color getColor() {
        return color;
    }

    public PieceType getType() {
        return type;
    }

    // Generate possible moves for this piece at a square on a given board
    public abstract List<Move> generateMoves(Board board, Square from);
    public abstract boolean isValidMove(Board board, Move move);
    public abstract char toFenChar();
}

