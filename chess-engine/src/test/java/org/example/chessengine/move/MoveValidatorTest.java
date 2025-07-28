package org.example.chessengine.move;

import org.example.chessengine.state.Square;
import org.example.chessengine.pieces.Color;
import org.example.chessengine.pieces.King;
import org.example.chessengine.pieces.Pawn;
import org.example.chessengine.state.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoveValidatorTest {

    private MoveValidator validator;
    private Game game;

    @BeforeEach
    void setUp() {
        validator = new MoveValidator();
        game = new Game(); // should initialize white's turn, empty board
    }

    @Test
    void testRejectsMoveFromEmptySquare() {
        Move move = new Move(new Square(4, 1), new Square(4, 3));
        assertFalse(validator.isLegalMove(game, move));
    }

    @Test
    void testRejectsMoveOfWrongColor() {
        Square from = new Square(4, 6); // black pawn
        game.placePiece(from, new Pawn(Color.BLACK));

        Move move = new Move(from, new Square(4, 5));
        assertFalse(validator.isLegalMove(game, move));
    }

    @Test
    void testRejectsMoveNotValidByPieceLogic() {
        Square from = new Square(4, 1); // white pawn
        game.placePiece(from, new Pawn(Color.WHITE));

        Move move = new Move(from, new Square(4, 5)); // illegal jump
        assertFalse(validator.isLegalMove(game, move));
    }

    @Test
    void testRejectsMoveLeavingKingInCheck() {
        // White king
        Square whiteKing = new Square(4, 0);
        game.placePiece(whiteKing, new King(Color.WHITE));

        // White pawn to move
        Square from = new Square(4, 1);
        Square to = new Square(4, 2);
        game.placePiece(from, new Pawn(Color.WHITE));

        // Black rook attacking from h1
        game.placePiece(new Square(7, 0), new org.example.chessengine.pieces.Rook(Color.BLACK));

        Move move = new Move(from, to);

        assertFalse(validator.isLegalMove(game, move));
    }

    @Test
    void testAcceptsValidMove() {
        // King setup
        Square king = new Square(4, 0);
        game.placePiece(king, new King(Color.WHITE));

        // Pawn forward move
        Square from = new Square(4, 1);
        Square to = new Square(4, 2);
        game.placePiece(from, new Pawn(Color.WHITE));

        Move move = new Move(from, to);
        assertTrue(validator.isLegalMove(game, move));
    }
}


