package org.example.chessengine.board;

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

    public void setPiece(Square square, Piece piece) {
        squares[square.file()][square.rank()] = piece;
    }

    public void removePiece(Square square) {
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
}
