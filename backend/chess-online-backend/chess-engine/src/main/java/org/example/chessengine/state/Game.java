package org.example.chessengine.state;

import org.example.chessengine.board.Board;
import org.example.chessengine.move.Move;
import org.example.chessengine.pieces.Color;
import org.example.chessengine.pieces.Piece;

import java.util.ArrayDeque;
import java.util.Deque;

public class Game {
    private final Board board;
    private Color turn;
    private final Deque<MoveHistoryEntry> history;

    public Game(Board board, Color startingColor) {
        this.board = board;
        this.turn = startingColor;
        this.history = new ArrayDeque<>();
    }

    public Board getBoard() {
        return board;
    }

    public Color getTurn() {
        return turn;
    }

    public void makeMove(Move move) {
        Piece movedPiece = board.getPiece(move.getFrom());
        Piece capturedPiece = board.getPiece(move.getTo());

        MoveHistoryEntry entry = new MoveHistoryEntry(move, movedPiece, capturedPiece);
        history.push(entry);

        board.setPiece(move.getTo(), movedPiece);
        board.removePiece(move.getFrom());

        turn = turn.opposite();
    }

    public void undoMove() {
        if (history.isEmpty()) return;

        MoveHistoryEntry entry = history.pop();
        board.setPiece(entry.move().getFrom(), entry.movedPiece());
        board.setPiece(entry.move().getTo(), entry.capturedPiece());

        turn = turn.opposite();
    }

    public boolean isInCheck(Color color) {
        // Placeholder for now
        return false;
    }
}

