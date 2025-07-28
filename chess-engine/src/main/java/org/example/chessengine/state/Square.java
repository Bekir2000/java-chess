package org.example.chessengine.state;

public record Square(int file, int rank) {


    public static Square fromAlgebraic(String notation) {
        int file = notation.charAt(0) - 'a';
        int rank = notation.charAt(1) - '1';
        return new Square(file, rank);
    }

    public String toAlgebraic() {
        return "" + (char)('a' + file) + (rank + 1);
    }

    public boolean isValid() {
        return file >= 0 && file < 8 && rank >= 0 && rank < 8;
    }

    @Override
    public String toString() {
        return "" + (char) ('a' + file) + (rank + 1);
    }
}
