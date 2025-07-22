package org.example.chessengine.state;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SquareTest {

    @Test
    void testFromAlgebraic() {
        Square square = Square.fromAlgebraic("e2");
        assertEquals(4, square.file());
        assertEquals(1, square.rank());
    }

    @Test
    void testToAlgebraic() {
        Square square = new Square(3, 6);
        assertEquals("d7", square.toAlgebraic());
    }

    @Test
    void testToStringEqualsToAlgebraic() {
        Square square = new Square(1, 2);
        assertEquals("b3", square.toString());
    }

    @Test
    void testIsValidReturnsTrueForValidSquares() {
        assertTrue(new Square(0, 0).isValid()); // a1
        assertTrue(new Square(7, 7).isValid()); // h8
        assertTrue(new Square(3, 4).isValid()); // d5
    }

    @Test
    void testIsValidReturnsFalseForInvalidSquares() {
        assertFalse(new Square(-1, 3).isValid());
        assertFalse(new Square(3, -1).isValid());
        assertFalse(new Square(8, 0).isValid());
        assertFalse(new Square(0, 8).isValid());
    }

    @Test
    void testEqualsAndHashCode() {
        Square s1 = new Square(2, 5);
        Square s2 = new Square(2, 5);
        Square s3 = new Square(5, 2);

        assertEquals(s1, s2);
        assertNotEquals(s1, s3);
        assertEquals(s1.hashCode(), s2.hashCode());
    }
}



