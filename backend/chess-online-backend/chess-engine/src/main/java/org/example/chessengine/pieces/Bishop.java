package org.example.chessengine.pieces;

public class Bishop extends DirectionalPiece {
    private static final int[][] BISHOP_DIRS = {
            {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
    };

    public Bishop(Color color) {
        super(color, PieceType.BISHOP, BISHOP_DIRS, 7);
    }
}

