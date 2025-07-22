package org.example.chessengine.pieces;

import org.example.chessengine.board.Board;
import org.example.chessengine.board.Square;
import org.example.chessengine.move.Move;
import org.example.chessengine.move.MoveType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RookTest {

    private Board board;
    private Rook whiteRook;
    private Rook blackRook;

    @BeforeEach
    void setUp() {
        board = new Board();
        whiteRook = new Rook(Color.WHITE);
        blackRook = new Rook(Color.BLACK);
    }

    @Test
    void testWhiteRookMovesUnblockedFromCenter() {
        Square from = new Square(4, 4); // e5
        board.setPiece(from, whiteRook);

        List<Move> moves = whiteRook.generateMoves(board, from);

        // There should be 14 legal moves (7 in each direction)
        assertEquals(14, moves.size());

        assertTrue(moves.stream().anyMatch(m -> m.to().equals(new Square(4, 0))));
        assertTrue(moves.stream().anyMatch(m -> m.to().equals(new Square(0, 4))));
        assertTrue(moves.stream().anyMatch(m -> m.to().equals(new Square(7, 4))));
    }

    @Test
    void testRookBlockedByFriendlyPiece() {
        Square from = new Square(4, 4);
        Square blocker = new Square(4, 6); // forward
        board.setPiece(from, whiteRook);
        board.setPiece(blocker, new Pawn(Color.WHITE));

        List<Move> moves = whiteRook.generateMoves(board, from);

        // Should stop before the blocker
        assertTrue(moves.stream().noneMatch(m -> m.to().equals(blocker)));
    }

    @Test
    void testRookCanCaptureEnemyPiece() {
        Square from = new Square(4, 4);
        Square target = new Square(4, 6);
        board.setPiece(from, whiteRook);
        board.setPiece(target, new Pawn(Color.BLACK));

        List<Move> moves = whiteRook.generateMoves(board, from);

        assertTrue(moves.stream().anyMatch(m -> m.to().equals(target) && m.type() == MoveType.CAPTURE));
        assertTrue(whiteRook.isValidMove(board, new Move(from, target, MoveType.CAPTURE)));
    }

    @Test
    void testRookCannotJumpOverEnemy() {
        Square from = new Square(4, 4);
        Square enemy = new Square(4, 5);
        Square behind = new Square(4, 6);
        board.setPiece(from, whiteRook);
        board.setPiece(enemy, new Pawn(Color.BLACK));
        board.setPiece(behind, new Pawn(Color.BLACK));

        List<Move> moves = whiteRook.generateMoves(board, from);
        assertTrue(moves.stream().anyMatch(m -> m.to().equals(enemy)));
        assertFalse(moves.stream().anyMatch(m -> m.to().equals(behind)));
    }

    @Test
    void testBlackRookSameLogicAsWhite() {
        Square from = new Square(3, 3);
        board.setPiece(from, blackRook);
        List<Move> moves = blackRook.generateMoves(board, from);

        assertTrue(moves.stream().anyMatch(m -> m.to().equals(new Square(3, 0))));
        assertTrue(moves.stream().anyMatch(m -> m.to().equals(new Square(3, 7))));
        assertTrue(moves.stream().anyMatch(m -> m.to().equals(new Square(0, 3))));
        assertTrue(moves.stream().anyMatch(m -> m.to().equals(new Square(7, 3))));
    }

    @Test
    void testBlackRookCapturesWhitePiece() {
        Square from = new Square(3, 3);
        Square target = new Square(3, 6);
        board.setPiece(from, blackRook);
        board.setPiece(target, new Rook(Color.WHITE));

        Move move = new Move(from, target, MoveType.CAPTURE);
        assertTrue(blackRook.isValidMove(board, move));
        assertTrue(blackRook.generateMoves(board, from).stream()
                .anyMatch(m -> m.to().equals(target) && m.type() == MoveType.CAPTURE));
    }

    @Test
    void testRookEdgeOfBoard() {
        Square from = new Square(0, 0);
        board.setPiece(from, whiteRook);

        List<Move> moves = whiteRook.generateMoves(board, from);
        assertTrue(moves.stream().allMatch(m -> m.to().isValid()));
    }
}

