package org.example.chessengine.state;

import org.example.chessengine.pieces.Piece;

public class Board {
    private final Piece[][] squares;

    public Board() {
        this.squares = new Piece[8][8];
    }

    public Piece getPiece(int file, int rank) {
        return squares[file][rank];
    }

    public Piece getPiece(Square square) {
        return squares[square.file()][square.rank()];
    }

    protected void setPiece(Square square, Piece piece) {
        squares[square.file()][square.rank()] = piece;
    }

    protected void removePiece(Square square) {
        squares[square.file()][square.rank()] = null;
    }

    public boolean isEmpty(Square square) {
        return getPiece(square) == null;
    }

    public Board copy() {
        Board copy = new Board();
        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                copy.squares[file][rank] = this.squares[file][rank];
            }
        }
        return copy;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int rank = 7; rank >= 0; rank--) {
            sb.append(rank + 1).append(" "); // Rank number
            for (int file = 0; file < 8; file++) {
                Piece piece = squares[file][rank];
                sb.append(piece != null ? piece.toFenChar() : ".").append(" ");
            }
            sb.append("\n");
        }
        sb.append("  a b c d e f g h");
        return sb.toString();
    }

    public void print() {
        System.out.println(this);
    }
}
