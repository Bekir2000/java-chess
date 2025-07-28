package org.example.chessengine.move;

import org.example.chessengine.pieces.*;
import org.example.chessengine.state.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MoveGeneratorTest {

    private MoveGenerator generator;
    private Game game;

    @BeforeEach
    public void setup() {
        generator = new MoveGenerator();
        game = new Game();
    }

    @Test
    public void testPseudoLegalMovesForPawn() {
        game.placePiece(new Square(4, 1), new Pawn(Color.WHITE)); // e2

        List<Move> pseudoMoves = generator.generateLegalMoves(game); // 1 or 2 forward

        assertTrue(pseudoMoves.stream().anyMatch(m -> m.to().equals(new Square(4, 2)))); // e3
        assertTrue(pseudoMoves.stream().anyMatch(m -> m.to().equals(new Square(4, 3)))); // e4
    }

    @Test
    public void testKingCannotMoveIntoCheck() {
        // Black king on e8, white rook on e1
        game.placePiece(new Square(4, 7), new King(Color.BLACK)); // e8
        game.placePiece(new Square(4, 0), new Rook(Color.WHITE)); // e1
        game.setTurn(Color.BLACK);

        List<Move> legalMoves = generator.generateLegalMoves(game);

        for (Move move : legalMoves) {
            game.makeMove(move);
            assertFalse(game.isInCheck(Color.BLACK));
            game.undoMove();
        }
    }

    @Test
    public void testStalemateGeneratesNoMoves() {
        // Classic stalemate: black king in corner, white king + queen dominate
        game.clearHistory();
        game = new Game();
        game.placePiece(new Square(7, 7), new King(Color.BLACK)); // h8
        game.placePiece(new Square(5, 6), new Queen(Color.WHITE)); // f7
        game.placePiece(new Square(6, 5), new King(Color.WHITE)); // g6
        game.setTurn(Color.BLACK);

        List<Move> legalMoves = generator.generateLegalMoves(game);

        assertEquals(0, legalMoves.size());
        assertTrue(game.getGameResult().contains("Stalemate"));
    }

    @Test
    public void testCheckmateGeneratesNoMoves() {
        // Fool's mate
        game.loadFromFEN("rnb1kbnr/pppp1ppp/8/4p3/6Pq/5P2/PPPPP2P/RNBQKBNR w KQkq - 0 3");

        game.getBoard().print();
        List<Move> legalMoves = generator.generateLegalMoves(game);

        assertEquals(0, legalMoves.size());
        assertTrue(game.getGameResult().contains("Checkmate"));
    }

    @Test
    public void testSelectedOpeningPosition() {

        game.loadFromFEN("r1bqk2r/pppp1ppp/2n2n2/2b1p3/2B1P3/2NP1N2/PPP2PPP/R1BQ1RK1 b kq - 7 5");

        game.getBoard().print();

        game.getBoard().print();
        List<Move> legalMoves = generator.generateLegalMoves(game);

        assertEquals(0, legalMoves.size());
        assertTrue(game.getGameResult().contains("Checkmate"));
    }
}

