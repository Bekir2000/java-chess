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

class KingTest {

    private Game game;
    private Board board;
    private King whiteKing;
    private King blackKing;

    @BeforeEach
    void setUp() {
        game = new Game();
        board = game.getBoard();
        whiteKing = new King(Color.WHITE);
        blackKing = new King(Color.BLACK);
    }

    @Test
    void testWhiteKingAllAdjacentMovesFromCenter() {
        Square from = new Square(4, 4);
        game.placePiece(from, whiteKing);

        List<Move> moves = whiteKing.generateMoves(board, from);
        assertEquals(8, moves.size());

        int[][] offsets = {
                {1, 0}, {-1, 0}, {0, 1}, {0, -1},
                {1, 1}, {-1, 1}, {1, -1}, {-1, -1}
        };

        for (int[] offset : offsets) {
            Square to = new Square(from.file() + offset[0], from.rank() + offset[1]);
            assertTrue(moves.stream().anyMatch(m -> m.to().equals(to)), "Missing move to " + to);
            assertTrue(whiteKing.isValidMove(board, new Move(from, to, MoveType.NORMAL)));
        }
    }

    @Test
    void testWhiteKingBlockedByFriendlyPiece() {
        Square from = new Square(4, 4);
        Square blocker = new Square(5, 5);
        game.placePiece(from, whiteKing);
        game.placePiece(blocker, new Pawn(Color.WHITE));

        List<Move> moves = whiteKing.generateMoves(board, from);

        assertTrue(moves.stream().noneMatch(m -> m.to().equals(blocker)));
        assertFalse(whiteKing.isValidMove(board, new Move(from, blocker, MoveType.CAPTURE)));
    }

    @Test
    void testWhiteKingCapturesEnemyPiece() {
        Square from = new Square(4, 4);
        Square target = new Square(3, 3);
        game.placePiece(from, whiteKing);
        game.placePiece(target, new Pawn(Color.BLACK));

        List<Move> moves = whiteKing.generateMoves(board, from);
        assertTrue(moves.stream().anyMatch(m -> m.to().equals(target) && m.type() == MoveType.CAPTURE));
        assertTrue(whiteKing.isValidMove(board, new Move(from, target, MoveType.CAPTURE)));
    }

    @Test
    void testKingCannotMoveMoreThanOneSquare() {
        Square from = new Square(4, 4);
        Square tooFar = new Square(4, 6);
        game.placePiece(from, whiteKing);

        Move move = new Move(from, tooFar, MoveType.NORMAL);
        assertFalse(whiteKing.isValidMove(board, move));
        assertTrue(whiteKing.generateMoves(board, from).stream()
                .noneMatch(m -> m.to().equals(tooFar)));
    }

    @Test
    void testKingEdgeOfBoard() {
        Square from = new Square(0, 0); // a1
        game.placePiece(from, whiteKing);

        List<Move> moves = whiteKing.generateMoves(board, from);

        assertTrue(moves.stream().allMatch(m -> m.to().isValid()));
        assertEquals(3, moves.size()); // Only (1,0), (0,1), (1,1)
    }

    @Test
    void testBlackKingSameBehaviorAsWhite() {
        Square from = new Square(3, 3);
        Square to = new Square(3, 2);
        game.placePiece(from, blackKing);

        assertTrue(blackKing.isValidMove(board, new Move(from, to, MoveType.NORMAL)));
        assertTrue(blackKing.generateMoves(board, from).stream().anyMatch(m -> m.to().equals(to)));
    }

    @Test
    void testBlackKingCapturesWhitePiece() {
        Square from = new Square(5, 5);
        Square target = new Square(6, 6);
        game.placePiece(from, blackKing);
        game.placePiece(target, whiteKing);

        assertTrue(blackKing.isValidMove(board, new Move(from, target, MoveType.CAPTURE)));
        assertTrue(blackKing.generateMoves(board, from).stream()
                .anyMatch(m -> m.to().equals(target) && m.type() == MoveType.CAPTURE));
    }
}

