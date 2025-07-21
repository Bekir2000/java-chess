package org.example.chessengine.move;

import org.example.chessengine.board.Square;

public class Move {
    private final Square from;
    private final Square to;
    private final MoveType type;

    public Move(Square from, Square to) {
        this(from, to, MoveType.NORMAL);
    }

    public Move(Square from, Square to, MoveType type) {
        this.from = from;
        this.to = to;
        this.type = type;
    }

    public Square getFrom() {
        return from;
    }

    public Square getTo() {
        return to;
    }

    public MoveType getType() {
        return type;
    }
}

