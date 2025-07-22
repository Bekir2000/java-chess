package org.example.chessengine.move;

import org.example.chessengine.board.Square;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MoveTest {

    @Test
    public void testMoveFields() {
        Move move = new Move(new Square(4, 1), new Square(4, 3), MoveType.NORMAL); // e2 -> e4
        assertEquals(new Square(4, 1), move.from());
        assertEquals(new Square(4, 3), move.to());
        assertEquals(MoveType.NORMAL, move.type());
    }

    @Test
    public void testDefaultConstructorSetsTypeToNormal() {
        Move move = new Move(new Square(0, 6), new Square(0, 7)); // a7 -> a8
        assertEquals(MoveType.NORMAL, move.type());
    }

    @Test
    public void testToStringReturnsAlgebraic() {
        Move move = new Move(new Square(6, 0), new Square(5, 2)); // g1 -> f3
        assertEquals("g1->f3", move.toString());
    }

    @Test
    public void testCaptureMoveType() {
        Move move = new Move(new Square(3, 3), new Square(4, 4), MoveType.CAPTURE);
        assertEquals(MoveType.CAPTURE, move.type());
    }

    @Test
    public void testPromotionMoveType() {
        Move move = new Move(new Square(0, 6), new Square(0, 7), MoveType.PROMOTION);
        assertEquals(MoveType.PROMOTION, move.type());
    }

    @Test
    public void testEqualityAndHashCode() {
        Move m1 = new Move(new Square(1, 0), new Square(2, 2), MoveType.NORMAL);
        Move m2 = new Move(new Square(1, 0), new Square(2, 2), MoveType.NORMAL);
        Move m3 = new Move(new Square(1, 0), new Square(2, 2), MoveType.CAPTURE);

        assertEquals(m1, m2, "Moves with same fields should be equal");
        assertNotEquals(m1, m3, "Different types should not be equal");
        assertEquals(m1.hashCode(), m2.hashCode(), "Equal moves should have equal hash codes");
    }
}



