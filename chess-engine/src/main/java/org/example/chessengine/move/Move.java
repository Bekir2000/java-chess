package org.example.chessengine.move;

import org.example.chessengine.state.Square;

public record Move(Square from, Square to, MoveType type) {

    public Move(Square from, Square to) {
        this(from, to, MoveType.NORMAL);
    }

    public String toUCI() {
        return from.toAlgebraic() + to.toAlgebraic(); // Add promotion if needed
    }

    @Override
    public String toString() {
        return from + "->" + to;
    }
}

