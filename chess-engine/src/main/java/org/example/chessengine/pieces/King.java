package org.example.chessengine.pieces;

public class King extends DirectionalPiece {
    private static final int[][] KING_DIRS = {
            {1, 0}, {-1, 0}, {0, 1}, {0, -1},
            {1, 1}, {-1, -1}, {1, -1}, {-1, 1}
    };

    public King(Color color) {
        super(color, PieceType.KING, KING_DIRS, 1);
    }

    @Override
    public char toFenChar() {
        return (color == Color.WHITE) ? 'K' : 'k';
    }
}

