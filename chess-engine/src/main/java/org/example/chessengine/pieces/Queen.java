package org.example.chessengine.pieces;

public class Queen extends DirectionalPiece {
    private static final int[][] QUEEN_DIRS = {
            {1, 0}, {-1, 0}, {0, 1}, {0, -1},
            {1, 1}, {-1, -1}, {1, -1}, {-1, 1}
    };

    public Queen(Color color) {
        super(color, PieceType.QUEEN, QUEEN_DIRS, 7);
    }

    @Override
    public char toFenChar() {
        return (color == Color.WHITE) ? 'Q' : 'q';
    }
}

