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

class QueenTest {

    private Game game;
    private Board board;
    private Queen whiteQueen;
    private Queen blackQueen;

    @BeforeEach
    void setUp() {
        game = new Game();
        board = game.getBoard();
        whiteQueen = new Queen(Color.WHITE);
        blackQueen = new Queen(Color.BLACK);
    }

    @Test
    void testWhiteQueenFullRangeFromCenter() {
        Square from = new Square(3, 3); // d4
        game.placePiece(from, whiteQueen);
        List<Move> moves = whiteQueen.generateMoves(board, from);

        // From d4, queen has 27 possible moves (7 each in 4 directions - minus overlaps)
        assertEquals(27, moves.size());

        // Examples
        assertTrue(moves.stream().anyMatch(m -> m.to().equals(new Square(3, 7)))); // up
        assertTrue(moves.stream().anyMatch(m -> m.to().equals(new Square(3, 0)))); // down
        assertTrue(moves.stream().anyMatch(m -> m.to().equals(new Square(7, 3)))); // right
        assertTrue(moves.stream().anyMatch(m -> m.to().equals(new Square(0, 3)))); // left
        assertTrue(moves.stream().anyMatch(m -> m.to().equals(new Square(6, 6)))); // diagonal
        assertTrue(moves.stream().anyMatch(m -> m.to().equals(new Square(0, 0)))); // diagonal
    }

    @Test
    void testQueenStopsAtFriendlyPiece() {
        Square from = new Square(3, 3);
        Square blocker = new Square(3, 5); // vertical
        game.placePiece(from, whiteQueen);
        game.placePiece(blocker, new Pawn(Color.WHITE));

        List<Move> moves = whiteQueen.generateMoves(board, from);

        // Should stop before blocker
        assertTrue(moves.stream().noneMatch(m -> m.to().equals(blocker)));
    }

    @Test
    void testQueenCapturesEnemyPiece() {
        Square from = new Square(3, 3);
        Square target = new Square(3, 6); // vertical
        game.placePiece(from, whiteQueen);
        game.placePiece(target, new Rook(Color.BLACK));

        List<Move> moves = whiteQueen.generateMoves(board, from);
        assertTrue(moves.stream().anyMatch(m -> m.to().equals(target) && m.type() == MoveType.CAPTURE));
        assertTrue(whiteQueen.isValidMove(board, new Move(from, target, MoveType.CAPTURE)));
    }

    @Test
    void testQueenCannotJumpOverPiece() {
        Square from = new Square(3, 3);
        Square blocker = new Square(3, 4);
        Square behind = new Square(3, 5);
        game.placePiece(from, whiteQueen);
        game.placePiece(blocker, new Rook(Color.BLACK));
        game.placePiece(behind, new Rook(Color.BLACK));

        List<Move> moves = whiteQueen.generateMoves(board, from);

        assertTrue(moves.stream().anyMatch(m -> m.to().equals(blocker))); // capture
        assertFalse(moves.stream().anyMatch(m -> m.to().equals(behind))); // cannot jump
    }

    @Test
    void testBlackQueenSameBehaviorAsWhite() {
        Square from = new Square(4, 4);
        game.placePiece(from, blackQueen);
        List<Move> moves = blackQueen.generateMoves(board, from);

        assertTrue(moves.stream().anyMatch(m -> m.to().equals(new Square(7, 7))));
        assertTrue(moves.stream().anyMatch(m -> m.to().equals(new Square(1, 1))));
        assertTrue(moves.stream().anyMatch(m -> m.to().equals(new Square(4, 0))));
        assertTrue(moves.stream().anyMatch(m -> m.to().equals(new Square(0, 4))));
    }

    @Test
    void testBlackQueenCapturesWhitePiece() {
        Square from = new Square(4, 4);
        Square target = new Square(4, 2);
        game.placePiece(from, blackQueen);
        game.placePiece(target, new Queen(Color.WHITE));

        Move move = new Move(from, target, MoveType.CAPTURE);
        assertTrue(blackQueen.isValidMove(board, move));
        assertTrue(blackQueen.generateMoves(board, from).stream()
                .anyMatch(m -> m.to().equals(target) && m.type() == MoveType.CAPTURE));
    }

    @Test
    void testQueenEdgeOfBoard() {
        Square from = new Square(0, 0);
        game.placePiece(from, whiteQueen);

        List<Move> moves = whiteQueen.generateMoves(board, from);
        assertTrue(moves.stream().allMatch(m -> m.to().isValid()));
    }
}


