package org.example.chessengine.state;

import org.example.chessengine.fen.FENUtils;
import org.example.chessengine.move.Move;
import org.example.chessengine.move.MoveGenerator;
import org.example.chessengine.move.MoveValidator;
import org.example.chessengine.pieces.*;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class Game {
    private final Board board;
    private Color turn;
    private final Deque<MoveHistoryEntry> history;

    private Square whiteKingPos;
    private Square blackKingPos;

    private record MoveHistoryEntry(Move move, Piece movedPiece, Piece capturedPiece) {}

    public Game(Board board, Color startingColor) {
        this.board = board;
        this.turn = startingColor;
        this.history = new ArrayDeque<>();
        this.whiteKingPos = findKing(Color.WHITE);
        this.blackKingPos = findKing(Color.BLACK);
    }

    public Game() {
        this(new Board(), Color.WHITE);
    }

    public void loadFromFEN(String fen) {
        Game fromFEN = FENUtils.fromFEN(fen);
        FENUtils.overwriteGameState(this, fromFEN);
    }

    public String getFEN() {
        return FENUtils.toFEN(this);
    }

    public boolean tryMove(Move move) {
        MoveValidator validator = new MoveValidator();
        if (validator.isLegalMove(this, move)) {
            makeMove(move);
            return true;
        }
        return false;
    }

    public void makeMove(Move move) {
        Piece movedPiece = board.getPiece(move.from());
        Piece capturedPiece = board.getPiece(move.to());

        history.push(new MoveHistoryEntry(move, movedPiece, capturedPiece));
        board.setPiece(move.to(), movedPiece);
        board.removePiece(move.from());
        updateKingPositionIfNeeded(move.to(), movedPiece);
        turn = turn.opposite();
    }

    public void undoMove() {
        if (history.isEmpty()) return;

        MoveHistoryEntry entry = history.pop();
        board.setPiece(entry.move().from(), entry.movedPiece());
        board.setPiece(entry.move().to(), entry.capturedPiece());
        updateKingPositionIfNeeded(entry.move().from(), entry.movedPiece());
        turn = turn.opposite();
    }

    public boolean isInCheck(Color color) {
        Square kingSquare = (color == Color.WHITE) ? whiteKingPos : blackKingPos;
        if (kingSquare == null) return false;

        Color opponent = color.opposite();
        MoveGenerator generator = new MoveGenerator();
        List<Move> opponentMoves = generator.generateAllMoves(board, opponent);

        for (Move move : opponentMoves) {
            if (move.to().equals(kingSquare)) {
                return true;
            }
        }

        return false;
    }

    public void placePiece(Square square, Piece piece) {
        board.setPiece(square, piece);
        updateKingPositionIfNeeded(square, piece);
    }

    public void removePiece(Square square) {
        Piece removed = board.getPiece(square);
        board.setPiece(square, null);

        if (removed instanceof King king) {
            if (king.getColor() == Color.WHITE) whiteKingPos = null;
            else blackKingPos = null;
        }
    }

    public Board getBoard() {
        return board;
    }

    public Color getTurn() {
        return turn;
    }

    public Square getWhiteKingPos() {
        return whiteKingPos;
    }

    public Square getBlackKingPos() {
        return blackKingPos;
    }

    public void setTurn(Color turn) {
        this.turn = turn;
    }

    public void setWhiteKingPos(Square square) {
        this.whiteKingPos = square;
    }

    public void setBlackKingPos(Square square) {
        this.blackKingPos = square;
    }

    public void clearHistory() {
        this.history.clear();
    }

    private void updateKingPositionIfNeeded(Square square, Piece piece) {
        if (piece instanceof King) {
            if (piece.getColor() == Color.WHITE) {
                whiteKingPos = square;
            } else {
                blackKingPos = square;
            }
        }
    }

    private Square findKing(Color color) {
        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                Square square = new Square(file, rank);
                Piece piece = board.getPiece(square);
                if (piece != null && piece.getColor() == color && piece.getType() == PieceType.KING) {
                    return square;
                }
            }
        }
        return null;
    }
}



