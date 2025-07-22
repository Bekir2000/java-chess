package org.example.chessengine.fen;

import org.example.chessengine.state.Board;
import org.example.chessengine.state.Game;
import org.example.chessengine.state.Square;
import org.example.chessengine.pieces.*;

public class FENUtils {

    public static Game fromFEN(String fen) {
        String[] parts = fen.split(" ");
        String piecePlacement = parts[0];
        String activeColor = parts[1];

        Color turn = activeColor.equals("w") ? Color.WHITE : Color.BLACK;
        Board board = new Board();
        Game game = new Game(board, turn); // assumes Game constructor initializes Board internally

        int rank = 7;
        int file = 0;

        for (char c : piecePlacement.toCharArray()) {
            if (c == '/') {
                rank--;
                file = 0;
            } else if (Character.isDigit(c)) {
                file += Character.getNumericValue(c);
            } else {
                Color color = Character.isUpperCase(c) ? Color.WHITE : Color.BLACK;
                Piece piece = switch (Character.toLowerCase(c)) {
                    case 'p' -> new Pawn(color);
                    case 'n' -> new Knight(color);
                    case 'b' -> new Bishop(color);
                    case 'r' -> new Rook(color);
                    case 'q' -> new Queen(color);
                    case 'k' -> new King(color);
                    default -> throw new IllegalArgumentException("Unknown piece: " + c);
                };
                game.placePiece(new Square(file, rank), piece);
                file++;
            }
        }

        return game;
    }

    public static String toFEN(Game game) {
        StringBuilder sb = new StringBuilder();
        Board board = game.getBoard();

        for (int rank = 7; rank >= 0; rank--) {
            int emptyCount = 0;
            for (int file = 0; file < 8; file++) {
                Square square = new Square(file, rank);
                Piece piece = board.getPiece(square);

                if (piece == null) {
                    emptyCount++;
                } else {
                    if (emptyCount > 0) {
                        sb.append(emptyCount);
                        emptyCount = 0;
                    }
                    sb.append(pieceToFENChar(piece));
                }
            }
            if (emptyCount > 0) {
                sb.append(emptyCount);
            }
            if (rank > 0) sb.append('/');
        }

        // Turn
        sb.append(' ');
        sb.append(game.getTurn() == Color.WHITE ? 'w' : 'b');

        // Placeholders
        sb.append(" KQkq"); // TODO: castling
        sb.append(" -");    // TODO: en passant
        sb.append(" 0 1");  // TODO: halfmove, fullmove

        return sb.toString();
    }

    private static char pieceToFENChar(Piece piece) {
        char c;
        switch (piece.getType()) {
            case PAWN -> c = 'p';
            case KNIGHT -> c = 'n';
            case BISHOP -> c = 'b';
            case ROOK -> c = 'r';
            case QUEEN -> c = 'q';
            case KING -> c = 'k';
            default -> throw new IllegalArgumentException("Unknown piece type");
        }
        return piece.getColor() == Color.WHITE ? Character.toUpperCase(c) : c;
    }
}

