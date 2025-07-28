package org.example.chessengine.engine;

import org.example.chessengine.state.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SearchTest {

    private Game game;
    private Evaluator evaluator;
    private Search search;

    @BeforeEach
    void setup() {
        game = new Game(); // no 'Game' before = assignment to field, not local var
        evaluator = new Evaluator();
        search = new Search(evaluator);
    }

    @Test
    void testSpeedOfSearch() {
        String fen = "r1bq1rk1/pp1n1ppp/2p2n2/2b1p3/2B1P3/2NP1N2/PPP2PPP/R1BQ1RK1 w - - 0 9"; // start pos
        game.loadFromFEN(fen);

        int searchDepth = 5;
        long start = System.nanoTime();
        search.findBestMove(game, searchDepth);
        long end = System.nanoTime();

        long durationMs = (end - start) / 1_000_000;
        System.out.printf("Search depth %d took %d ms %n",searchDepth, durationMs);
    }
}

