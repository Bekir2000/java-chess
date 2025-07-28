package org.example.chessengine.pieces;

import org.example.chessengine.state.Board;
import org.example.chessengine.state.Game;
import org.example.chessengine.state.Square;
import org.example.chessengine.move.Move;
import org.example.chessengine.move.MoveType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RookTest {

    private Game game;
    private Board board;
    private Rook whiteRook;
    private Rook blackRook;

    @BeforeEach
    void setUp() {
        game = new Game();
        board = game.getBoard();
        whiteRook = new Rook(Color.WHITE);
        blackRook = new Rook(Color.BLACK);
    }

    @Test
    void testWhiteRookMovesUnblockedFromCenter() {
        Square from = new Square(4, 4); // e5
        game.placePiece(from, whiteRook);

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
        game.placePiece(from, whiteRook);
        game.placePiece(blocker, new Pawn(Color.WHITE));

        List<Move> moves = whiteRook.generateMoves(board, from);

        // Should stop before the blocker
        assertTrue(moves.stream().noneMatch(m -> m.to().equals(blocker)));
    }

    @Test
    void testRookCanCaptureEnemyPiece() {
        Square from = new Square(4, 4);
        Square target = new Square(4, 6);
        game.placePiece(from, whiteRook);
        game.placePiece(target, new Pawn(Color.BLACK));

        List<Move> moves = whiteRook.generateMoves(board, from);

        assertTrue(moves.stream().anyMatch(m -> m.to().equals(target) && m.type() == MoveType.CAPTURE));
        assertTrue(whiteRook.isValidMove(board, new Move(from, target, MoveType.CAPTURE)));
    }

    @Test
    void testRookCannotJumpOverEnemy() {
        Square from = new Square(4, 4);
        Square enemy = new Square(4, 5);
        Square behind = new Square(4, 6);
        game.placePiece(from, whiteRook);
        game.placePiece(enemy, new Pawn(Color.BLACK));
        game.placePiece(behind, new Pawn(Color.BLACK));

        List<Move> moves = whiteRook.generateMoves(board, from);
        assertTrue(moves.stream().anyMatch(m -> m.to().equals(enemy)));
        assertFalse(moves.stream().anyMatch(m -> m.to().equals(behind)));
    }

    @Test
    void testBlackRookSameLogicAsWhite() {
        Square from = new Square(3, 3);
        game.placePiece(from, blackRook);
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
        game.placePiece(from, blackRook);
        game.placePiece(target, new Rook(Color.WHITE));

        Move move = new Move(from, target, MoveType.CAPTURE);
        assertTrue(blackRook.isValidMove(board, move));
        assertTrue(blackRook.generateMoves(board, from).stream()
                .anyMatch(m -> m.to().equals(target) && m.type() == MoveType.CAPTURE));
    }

    @Test
    void testRookEdgeOfBoard() {
        Square from = new Square(0, 0);
        game.placePiece(from, whiteRook);

        List<Move> moves = whiteRook.generateMoves(board, from);
        assertTrue(moves.stream().allMatch(m -> m.to().isValid()));
    }
}

