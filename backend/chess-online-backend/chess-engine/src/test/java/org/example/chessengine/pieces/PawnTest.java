package org.example.chessengine.pieces;

import org.example.chessengine.board.Board;
import org.example.chessengine.board.Square;
import org.example.chessengine.move.Move;
import org.example.chessengine.move.MoveType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PawnTest {

    private Board board;
    private Pawn whitePawn;
    private Pawn blackPawn;

    @BeforeEach
    void setUp() {
        board = new Board();
        whitePawn = new Pawn(Color.WHITE);
        blackPawn = new Pawn(Color.BLACK);
    }

    // --------- WHITE PAWN TESTS ----------

    @Test
    void testWhitePawnSingleStep() {
        testValidMove(whitePawn, new Square(4, 1), new Square(4, 2), MoveType.NORMAL);
    }

    @Test
    void testWhitePawnDoubleStepFromStart() {
        testValidMove(whitePawn, new Square(3, 1), new Square(3, 3), MoveType.NORMAL);
    }

    @Test
    void testWhitePawnBlockedForward() {
        Square from = new Square(2, 1);
        Square block = new Square(2, 2);
        board.setPiece(from, whitePawn);
        board.setPiece(block, new Pawn(Color.BLACK));

        assertFalse(whitePawn.isValidMove(board, new Move(from, block, MoveType.NORMAL)));
        assertTrue(whitePawn.generateMoves(board, from).isEmpty());
    }

    @Test
    void testWhitePawnDiagonalCapture() {
        Square from = new Square(4, 4);
        Square target = new Square(5, 5);
        board.setPiece(from, whitePawn);
        board.setPiece(target, blackPawn);
        testValidMove(whitePawn, from, target, MoveType.CAPTURE);
    }

    @Test
    void testWhitePawnInvalidDiagonalCapture() {
        Square from = new Square(4, 4);
        Square diag = new Square(5, 5);
        board.setPiece(from, whitePawn);

        assertFalse(whitePawn.isValidMove(board, new Move(from, diag, MoveType.CAPTURE)));
        assertTrue(whitePawn.generateMoves(board, from).stream().noneMatch(m -> m.to().equals(diag)));
    }

    @Test
    void testWhitePawnPromotion() {
        Square from = new Square(4, 6);
        Square to = new Square(4, 7);
        board.setPiece(from, whitePawn);
        testValidMove(whitePawn, from, to, MoveType.PROMOTION);
    }

    @Test
    void testWhitePawnCannotMoveBackward() {
        Square from = new Square(4, 4);
        Square to = new Square(4, 3);
        board.setPiece(from, whitePawn);
        assertFalse(whitePawn.isValidMove(board, new Move(from, to, MoveType.NORMAL)));
        assertTrue(whitePawn.generateMoves(board, from).stream().noneMatch(m -> m.to().equals(to)));
    }

    @Test
    void testWhitePawnCannotDoubleStepOffStart() {
        Square from = new Square(3, 3);
        Square to = new Square(3, 5);
        board.setPiece(from, whitePawn);
        assertFalse(whitePawn.isValidMove(board, new Move(from, to, MoveType.NORMAL)));
        assertTrue(whitePawn.generateMoves(board, from).stream().noneMatch(m -> m.to().equals(to)));
    }

    @Test
    void testWhitePawnInvalidLongMove() {
        Square from = new Square(4, 1);
        Square to = new Square(4, 4);
        board.setPiece(from, whitePawn);
        assertFalse(whitePawn.isValidMove(board, new Move(from, to, MoveType.NORMAL)));
    }

    // --------- BLACK PAWN TESTS ----------

    @Test
    void testBlackPawnSingleStep() {
        testValidMove(blackPawn, new Square(4, 6), new Square(4, 5), MoveType.NORMAL);
    }

    @Test
    void testBlackPawnDoubleStepFromStart() {
        testValidMove(blackPawn, new Square(3, 6), new Square(3, 4), MoveType.NORMAL);
    }

    @Test
    void testBlackPawnBlockedForward() {
        Square from = new Square(2, 6);
        Square block = new Square(2, 5);
        board.setPiece(from, blackPawn);
        board.setPiece(block, new Pawn(Color.WHITE));

        assertFalse(blackPawn.isValidMove(board, new Move(from, block, MoveType.NORMAL)));
        assertTrue(blackPawn.generateMoves(board, from).isEmpty());
    }

    @Test
    void testBlackPawnDiagonalCapture() {
        Square from = new Square(4, 4);
        Square target = new Square(5, 3);
        board.setPiece(from, blackPawn);
        board.setPiece(target, whitePawn);
        testValidMove(blackPawn, from, target, MoveType.CAPTURE);
    }

    @Test
    void testBlackPawnInvalidDiagonalCapture() {
        Square from = new Square(4, 4);
        Square diag = new Square(5, 3);
        board.setPiece(from, blackPawn);

        assertFalse(blackPawn.isValidMove(board, new Move(from, diag, MoveType.CAPTURE)));
        assertTrue(blackPawn.generateMoves(board, from).stream().noneMatch(m -> m.to().equals(diag)));
    }

    @Test
    void testBlackPawnPromotion() {
        Square from = new Square(4, 1);
        Square to = new Square(4, 0);
        board.setPiece(from, blackPawn);
        testValidMove(blackPawn, from, to, MoveType.PROMOTION);
    }

    @Test
    void testBlackPawnCannotMoveBackward() {
        Square from = new Square(4, 4);
        Square to = new Square(4, 5);
        board.setPiece(from, blackPawn);
        assertFalse(blackPawn.isValidMove(board, new Move(from, to, MoveType.NORMAL)));
        assertTrue(blackPawn.generateMoves(board, from).stream().noneMatch(m -> m.to().equals(to)));
    }

    @Test
    void testBlackPawnCannotDoubleStepOffStart() {
        Square from = new Square(3, 4);
        Square to = new Square(3, 2);
        board.setPiece(from, blackPawn);
        assertFalse(blackPawn.isValidMove(board, new Move(from, to, MoveType.NORMAL)));
        assertTrue(blackPawn.generateMoves(board, from).stream().noneMatch(m -> m.to().equals(to)));
    }

    @Test
    void testBlackPawnInvalidLongMove() {
        Square from = new Square(4, 6);
        Square to = new Square(4, 3);
        board.setPiece(from, blackPawn);
        assertFalse(blackPawn.isValidMove(board, new Move(from, to, MoveType.NORMAL)));
    }

    // --------- HELPER ---------

    private void testValidMove(Pawn pawn, Square from, Square to, MoveType type) {
        board.setPiece(from, pawn);
        Move move = new Move(from, to, type);
        List<Move> moves = pawn.generateMoves(board, from);

        assertTrue(pawn.isValidMove(board, move), "isValidMove should return true");
        assertTrue(moves.stream().anyMatch(m -> m.to().equals(to) && m.type() == type),
                "generateMoves should include the move");
    }
}

