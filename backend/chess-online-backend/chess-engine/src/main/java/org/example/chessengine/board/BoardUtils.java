package org.example.chessengine.board;

public class BoardUtils {
    public static boolean isInBounds(int file, int rank) {
        return file >= 0 && file < 8 && rank >= 0 && rank < 8;
    }

    public static int squareToIndex(Square square) {
        return square.rank() * 8 + square.file();
    }

    public static Square indexToSquare(int index) {
        int file = index % 8;
        int rank = index / 8;
        return new Square(file, rank);
    }
}

