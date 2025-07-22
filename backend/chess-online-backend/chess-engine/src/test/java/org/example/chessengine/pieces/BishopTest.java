package org.example.chessengine.pieces;

import org.example.chessengine.board.Board;
import org.example.chessengine.board.Square;
import org.example.chessengine.move.Move;
import org.example.chessengine.move.MoveType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BishopTest {

    private Board board;
    private Bishop whiteBishop;
    private Bishop blackBishop;

    @BeforeEach
    void setUp() {
        board = new Board();
        whiteBishop = new Bishop(Color.WHITE);
        blackBishop = new Bishop(Color.BLACK);
    }

    @Test
    void testWhiteBishopMovesUnblockedFromCenter() {
        Square from = new Square(3, 3); // d4
        board.setPiece(from, whiteBishop);
        List<Move> moves = whiteBishop.generateMoves(board, from);

        // There are 13 possible diagonal destinations from center
        int expectedMoves = 13;
        assertEquals(expectedMoves, moves.size());

        // Check example diagonals
        assertTrue(moves.stream().anyMatch(m -> m.to().equals(new Square(6, 6))));
        assertTrue(moves.stream().anyMatch(m -> m.to().equals(new Square(0, 0))));
    }

    @Test
    void testBishopStopsBeforeFriendlyPiece() {
        Square from = new Square(2, 2); // c3
        Square blocker = new Square(4, 4); // e5
        board.setPiece(from, whiteBishop);
        board.setPiece(blocker, new Pawn(Color.WHITE));

        List<Move> moves = whiteBishop.generateMoves(board, from);

        // Should stop at d4 (3,3) but not reach e5 (4,4)
        assertTrue(moves.stream().anyMatch(m -> m.to().equals(new Square(3, 3))));
        assertFalse(moves.stream().anyMatch(m -> m.to().equals(blocker)));
    }

    @Test
    void testBishopCanCaptureEnemyPiece() {
        Square from = new Square(2, 2); // c3
        Square target = new Square(4, 4); // e5
        board.setPiece(from, whiteBishop);
        board.setPiece(target, new Pawn(Color.BLACK));

        List<Move> moves = whiteBishop.generateMoves(board, from);

        assertTrue(moves.stream().anyMatch(m -> m.to().equals(target) && m.type() == MoveType.CAPTURE));
        assertTrue(whiteBishop.isValidMove(board, new Move(from, target, MoveType.CAPTURE)));
    }

    @Test
    void testBishopCannotJumpOverPiece() {
        Square from = new Square(2, 2);
        Square block = new Square(3, 3);
        Square behind = new Square(4, 4);
        board.setPiece(from, whiteBishop);
        board.setPiece(block, new Pawn(Color.BLACK));
        board.setPiece(behind, new Pawn(Color.BLACK));

        List<Move> moves = whiteBishop.generateMoves(board, from);

        assertTrue(moves.stream().anyMatch(m -> m.to().equals(block)));
        assertFalse(moves.stream().anyMatch(m -> m.to().equals(behind)));
    }

    @Test
    void testBlackBishopSameMovesAsWhite() {
        Square from = new Square(3, 3);
        board.setPiece(from, blackBishop);
        List<Move> moves = blackBishop.generateMoves(board, from);

        assertTrue(moves.stream().anyMatch(m -> m.to().equals(new Square(0, 0))));
        assertTrue(moves.stream().anyMatch(m -> m.to().equals(new Square(6, 6))));
    }

    @Test
    void testBlackBishopCapturesWhite() {
        Square from = new Square(1, 1);
        Square target = new Square(3, 3);
        board.setPiece(from, blackBishop);
        board.setPiece(target, whiteBishop);

        Move move = new Move(from, target, MoveType.CAPTURE);
        assertTrue(blackBishop.isValidMove(board, move));
        assertTrue(blackBishop.generateMoves(board, from).stream()
                .anyMatch(m -> m.to().equals(target) && m.type() == MoveType.CAPTURE));
    }

    @Test
    void testBishopEdgeOfBoard() {
        Square from = new Square(0, 0); // a1
        board.setPiece(from, whiteBishop);

        List<Move> moves = whiteBishop.generateMoves(board, from);
        assertTrue(moves.stream().allMatch(m -> m.to().isValid()));
    }
}

