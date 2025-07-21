package org.example.chessengine.state;

import org.example.chessengine.move.Move;
import org.example.chessengine.pieces.Piece;

public record MoveHistoryEntry(Move move, Piece movedPiece, Piece capturedPiece) {
}

