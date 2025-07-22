package org.example.chessengine.state;

import org.example.chessengine.move.Move;
import org.example.chessengine.pieces.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void testEmptyGameInitialization() {
        Game game = new Game();
        assertEquals(Color.WHITE, game.getTurn());
        assertNotNull(game.getBoard());
    }

    @Test
    void testLoadFromFEN() {
        Game game = new Game();
        game.loadFromFEN("8/8/8/8/8/8/3P4/4K2k w - - 0 1");

        assertInstanceOf(Pawn.class, game.getBoard().getPiece(new Square(3, 1)));     // d2
        assertInstanceOf(King.class, game.getBoard().getPiece(new Square(4, 0)));     // e1
        assertInstanceOf(King.class, game.getBoard().getPiece(new Square(7, 0)));     // h1
        assertEquals(Color.WHITE, game.getTurn());
    }

    @Test
    void testGetFENRoundTrip() {
        String fen = "8/8/8/8/8/8/3P4/4K2k b - - 0 1";
        Game game = new Game();
        game.loadFromFEN(fen);

        String result = game.getFEN();
        assertTrue(result.startsWith("8/8/8/8/8/8/3P4/4K2k"));
        assertTrue(result.contains("b"));
    }

    @Test
    void testPlaceAndRemovePiece() {
        Game game = new Game();
        Square d2 = new Square(3, 1);
        game.placePiece(d2, new Pawn(Color.WHITE));
        assertInstanceOf(Pawn.class, game.getBoard().getPiece(d2));

        game.removePiece(d2);
        assertNull(game.getBoard().getPiece(d2));
    }

    @Test
    void testMakeAndUndoMove() {
        Game game = new Game();
        game.loadFromFEN("8/8/8/8/8/8/3P4/4K2k w - - 0 1");

        Square from = new Square(3, 1); // d2
        Square to = new Square(3, 3);   // d4

        Move move = new Move(from, to);
        game.makeMove(move);

        assertNull(game.getBoard().getPiece(from));
        assertInstanceOf(Pawn.class, game.getBoard().getPiece(to));
        assertEquals(Color.BLACK, game.getTurn());

        game.undoMove();
        assertInstanceOf(Pawn.class, game.getBoard().getPiece(from));
        assertNull(game.getBoard().getPiece(to));
        assertEquals(Color.WHITE, game.getTurn());
    }

    @Test
    void testTryMoveLegalAndIllegal() {
        Game game = new Game();
        game.loadFromFEN("8/8/8/8/8/8/3P4/4K2k w - - 0 1");

        Move legal = new Move(new Square(3, 1), new Square(3, 2));
        Move illegal = new Move(new Square(3, 1), new Square(3, 5));

        assertTrue(game.tryMove(legal));
        assertFalse(game.tryMove(illegal));
    }

    @Test
    void testKingTrackingUpdate() {
        Game game = new Game();
        game.loadFromFEN("8/8/8/8/8/8/8/4K2k w - - 0 1");

        Square oldPos = new Square(4, 0);
        Square newPos = new Square(4, 1);
        game.makeMove(new Move(oldPos, newPos));

        assertEquals(newPos, game.getWhiteKingPos());

        game.undoMove();
        assertEquals(oldPos, game.getWhiteKingPos());
    }

    @Test
    void testIsInCheckWhiteAndBlack() {
        Game game = new Game();
        game.loadFromFEN("8/8/8/8/8/8/4r3/4K2k w - - 0 1"); // rook on e2 checking king on e1

        assertTrue(game.isInCheck(Color.WHITE));
        assertFalse(game.isInCheck(Color.BLACK));
    }

    @Test
    void testRemoveKingNullifiesPosition() {
        Game game = new Game();
        game.loadFromFEN("8/8/8/8/8/8/8/4K2k w - - 0 1");
        Square e1 = new Square(4, 0);

        assertNotNull(game.getWhiteKingPos());
        game.removePiece(e1);
        assertNull(game.getWhiteKingPos());
    }

    @Test
    void testUndoDoesNotFailWhenEmpty() {
        Game game = new Game();
        assertDoesNotThrow(game::undoMove);
    }
}
