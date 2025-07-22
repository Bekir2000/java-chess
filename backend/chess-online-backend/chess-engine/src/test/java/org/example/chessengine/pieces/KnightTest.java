package org.example.chessengine.pieces;

import org.example.chessengine.board.Board;
import org.example.chessengine.board.Square;
import org.example.chessengine.move.Move;
import org.example.chessengine.move.MoveType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KnightTest {

    private Board board;
    private Knight whiteKnight;
    private Knight blackKnight;

    @BeforeEach
    void setUp() {
        board = new Board();
        whiteKnight = new Knight(Color.WHITE);
        blackKnight = new Knight(Color.BLACK);
    }

    @Test
    void testWhiteKnightAllLegalMovesFromCenter() {
        Square from = new Square(4, 4);
        board.setPiece(from, whiteKnight);

        List<Move> moves = whiteKnight.generateMoves(board, from);

        int[][] offsets = {
                {2, 1}, {1, 2}, {-1, 2}, {-2, 1},
                {-2, -1}, {-1, -2}, {1, -2}, {2, -1}
        };

        for (int[] offset : offsets) {
            Square to = new Square(from.file() + offset[0], from.rank() + offset[1]);
            if (to.isValid()) {
                assertTrue(moves.stream().anyMatch(m -> m.to().equals(to)),
                        "Expected move to " + to);
                assertTrue(whiteKnight.isValidMove(board, new Move(from, to, MoveType.NORMAL)));
            }
        }
    }

    @Test
    void testWhiteKnightCapturesEnemy() {
        Square from = new Square(4, 4);
        Square to = new Square(6, 5);
        board.setPiece(from, whiteKnight);
        board.setPiece(to, blackKnight);

        List<Move> moves = whiteKnight.generateMoves(board, from);
        assertTrue(moves.stream().anyMatch(m -> m.to().equals(to) && m.type() == MoveType.CAPTURE));
        assertTrue(whiteKnight.isValidMove(board, new Move(from, to, MoveType.CAPTURE)));
    }

    @Test
    void testWhiteKnightCannotCaptureFriendly() {
        Square from = new Square(4, 4);
        Square to = new Square(6, 5);
        board.setPiece(from, whiteKnight);
        board.setPiece(to, new Knight(Color.WHITE));

        List<Move> moves = whiteKnight.generateMoves(board, from);
        assertTrue(moves.stream().noneMatch(m -> m.to().equals(to)));
        assertFalse(whiteKnight.isValidMove(board, new Move(from, to, MoveType.CAPTURE)));
    }

    @Test
    void testKnightCanJumpOverPieces() {
        Square from = new Square(4, 4);
        board.setPiece(from, whiteKnight);

        // Place friendly pawns around the knight
        for (int df = -1; df <= 1; df++) {
            for (int dr = -1; dr <= 1; dr++) {
                if (df == 0 && dr == 0) continue;
                Square block = new Square(from.file() + df, from.rank() + dr);
                if (block.isValid()) {
                    board.setPiece(block, new Pawn(Color.WHITE));
                }
            }
        }

        List<Move> moves = whiteKnight.generateMoves(board, from);
        assertFalse(moves.isEmpty(), "Knight should still move regardless of blockers");
    }

    @Test
    void testKnightIgnoresInvalidOffBoardMoves() {
        Square from = new Square(0, 0); // corner
        board.setPiece(from, whiteKnight);

        List<Move> moves = whiteKnight.generateMoves(board, from);
        for (Move move : moves) {
            assertTrue(move.to().isValid(), "Move should not go off board: " + move.to());
        }
    }

    @Test
    void testBlackKnightMovementSameAsWhite() {
        Square from = new Square(3, 3);
        Square to = new Square(5, 4);
        board.setPiece(from, blackKnight);

        List<Move> moves = blackKnight.generateMoves(board, from);
        assertTrue(moves.stream().anyMatch(m -> m.to().equals(to)));
        assertTrue(blackKnight.isValidMove(board, new Move(from, to, MoveType.NORMAL)));
    }

    @Test
    void testBlackKnightCapturesEnemy() {
        Square from = new Square(3, 3);
        Square to = new Square(1, 2);
        board.setPiece(from, blackKnight);
        board.setPiece(to, whiteKnight);

        List<Move> moves = blackKnight.generateMoves(board, from);
        assertTrue(moves.stream().anyMatch(m -> m.to().equals(to) && m.type() == MoveType.CAPTURE));
        assertTrue(blackKnight.isValidMove(board, new Move(from, to, MoveType.CAPTURE)));
    }

    @Test
    void testBlackKnightCannotCaptureFriendly() {
        Square from = new Square(3, 3);
        Square to = new Square(1, 2);
        board.setPiece(from, blackKnight);
        board.setPiece(to, new Knight(Color.BLACK));

        List<Move> moves = blackKnight.generateMoves(board, from);
        assertTrue(moves.stream().noneMatch(m -> m.to().equals(to)));
        assertFalse(blackKnight.isValidMove(board, new Move(from, to, MoveType.CAPTURE)));
    }
}


