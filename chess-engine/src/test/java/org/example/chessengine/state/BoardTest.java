package org.example.chessengine.state;

import org.example.chessengine.pieces.Color;
import org.example.chessengine.pieces.Pawn;
import org.example.chessengine.pieces.Piece;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void testSetAndGetPiece() {
        Board board = new Board();
        Square square = new Square(3, 3); // d4
        Piece pawn = new Pawn(Color.WHITE);

        board.setPiece(square, pawn);
        assertEquals(pawn, board.getPiece(square));
        assertEquals(pawn, board.getPiece(3, 3));
    }

    @Test
    void testRemovePiece() {
        Board board = new Board();
        Square square = new Square(2, 2); // c3
        board.setPiece(square, new Pawn(Color.BLACK));

        board.removePiece(square);
        assertNull(board.getPiece(square));
    }

    @Test
    void testIsEmpty() {
        Board board = new Board();
        Square empty = new Square(1, 1);
        Square filled = new Square(1, 2);

        board.setPiece(filled, new Pawn(Color.BLACK));

        assertTrue(board.isEmpty(empty));
        assertFalse(board.isEmpty(filled));
    }

    @Test
    void testCopyCreatesDeepClone() {
        Board original = new Board();
        Square square = new Square(4, 4);
        Piece pawn = new Pawn(Color.WHITE);
        original.setPiece(square, pawn);

        Board copy = original.copy();

        // Check content copied
        assertEquals(pawn, copy.getPiece(square));

        // Check deep copy: modify original, copy stays same
        original.removePiece(square);
        assertNull(original.getPiece(square));
        assertEquals(pawn, copy.getPiece(square));
    }
}
