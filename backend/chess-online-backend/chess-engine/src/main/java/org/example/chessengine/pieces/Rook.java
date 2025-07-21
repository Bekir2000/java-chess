package org.example.chessengine.pieces;

public class Rook extends DirectionalPiece {
    private static final int[][] ROOK_DIRS = {
            {1, 0}, {-1, 0}, {0, 1}, {0, -1}
    };

    public Rook(Color color) {
        super(color, PieceType.ROOK, ROOK_DIRS, 7);
    }
}
