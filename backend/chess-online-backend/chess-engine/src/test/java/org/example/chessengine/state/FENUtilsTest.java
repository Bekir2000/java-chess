package org.example.chessengine.state;

import org.example.chessengine.board.Board;
import org.example.chessengine.board.Square;
import org.example.chessengine.pieces.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FENUtilsTest {

    @Test
    void testFromFENParsesPiecePlacementAndTurn() {
        String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        Game game = FENUtils.fromFEN(fen);
        Board board = game.getBoard();

        // Sample piece checks
        assertInstanceOf(Rook.class, board.getPiece(new Square(0, 0)));
        assertEquals(Color.WHITE, board.getPiece(new Square(0, 0)).getColor());

        assertInstanceOf(Knight.class, board.getPiece(new Square(1, 7)));
        assertEquals(Color.BLACK, board.getPiece(new Square(1, 7)).getColor());

        assertNull(board.getPiece(new Square(4, 4))); // empty square

        assertEquals(Color.WHITE, game.getTurn());
    }

    @Test
    void testToFENGeneratesCorrectString() {
        String initialFEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        Game game = FENUtils.fromFEN(initialFEN);
        String generated = FENUtils.toFEN(game);

        assertTrue(generated.startsWith("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w"));
    }

    @Test
    void testRoundTripFEN() {
        String fen = "r1bqkbnr/pppppppp/2n5/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq - 0 1";
        Game game = FENUtils.fromFEN(fen);
        String backToFEN = FENUtils.toFEN(game);

        // Ignore fields you don't track (castling, ep, counters)
        assertTrue(backToFEN.startsWith("r1bqkbnr/pppppppp/2n5/8/4P3/8/PPPP1PPP/RNBQKBNR b"));
    }

    @Test
    void testEmptyBoardFEN() {
        String fen = "8/8/8/8/8/8/8/8 w - - 0 1";
        Game game = FENUtils.fromFEN(fen);
        Board board = game.getBoard();

        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                assertNull(board.getPiece(new Square(file, rank)));
            }
        }
    }

    @Test
    void testInvalidPieceThrows() {
        String badFEN = "rnbqkbnx/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        assertThrows(IllegalArgumentException.class, () -> FENUtils.fromFEN(badFEN));
    }
}


