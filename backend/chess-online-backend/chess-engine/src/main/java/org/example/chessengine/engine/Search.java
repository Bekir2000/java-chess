package org.example.chessengine.engine;

import org.example.chessengine.state.Game;
import org.example.chessengine.move.Move;
import org.example.chessengine.move.MoveValidator;
import org.example.chessengine.pieces.Color;

public class Search {

    private final Evaluator evaluator;

    public Search(Evaluator evaluator) {
        this.evaluator = evaluator;
    }

    public SearchResult findBestMove(Game game, int depth) {
        Color side = game.getTurn();
        Move bestMove = null;
        int bestScore = Integer.MIN_VALUE;

        MoveValidator validator = new MoveValidator();
        for (Move move : validator.generateLegalMoves(game, side)) {
            game.makeMove(move);
            int score = minimax(game, depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
            game.undoMove();

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }

        return new SearchResult(bestMove, bestScore, depth);
    }

    private int minimax(Game game, int depth, int alpha, int beta, boolean maximizingPlayer) {
        if (depth == 0) {
            return evaluator.evaluate(game);
        }

        MoveValidator validator = new MoveValidator();
        Color side = game.getTurn();

        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (Move move : validator.generateLegalMoves(game, side)) {
                game.makeMove(move);
                int eval = minimax(game, depth - 1, alpha, beta, false);
                game.undoMove();
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) break; // beta cutoff
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (Move move : validator.generateLegalMoves(game, side)) {
                game.makeMove(move);
                int eval = minimax(game, depth - 1, alpha, beta, true);
                game.undoMove();
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) break; // alpha cutoff
            }
            return minEval;
        }
    }
}

