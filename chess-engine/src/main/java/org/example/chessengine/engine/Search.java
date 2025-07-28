package org.example.chessengine.engine;

import org.example.chessengine.move.*;
import org.example.chessengine.pieces.Color;
import org.example.chessengine.state.Game;

import java.util.List;

public class Search {
    private final Evaluator evaluator;
    private final MoveGenerator moveGenerator;

    public Search(Evaluator evaluator) {
        this.evaluator = evaluator;
        this.moveGenerator = new MoveGenerator();
    }

    public Move findBestMove(Game game, int depth) {
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        Move bestMove = null;
        int bestScore = game.getTurn() == Color.WHITE ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        List<Move> legalMoves = moveGenerator.generateLegalMoves(game);

        for (Move move : legalMoves) {
            game.makeMove(move);
            int score = minimax(game, depth - 1, alpha, beta, game.getTurn() == Color.WHITE);
            game.undoMove();

            if (game.getTurn() == Color.WHITE && score > bestScore) {
                bestScore = score;
                bestMove = move;
                alpha = Math.max(alpha, bestScore);
            } else if (game.getTurn() == Color.BLACK && score < bestScore) {
                bestScore = score;
                bestMove = move;
                beta = Math.min(beta, bestScore);
            }
        }

        return bestMove;
    }

    private int minimax(Game game, int depth, int alpha, int beta, boolean maximizingPlayer) {
        if (depth == 0 || game.isGameOver()) {
            return evaluator.evaluate(game.getBoard());
        }

        List<Move> legalMoves = moveGenerator.generateLegalMoves(game);

        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (Move move : legalMoves) {
                game.makeMove(move);
                int eval = minimax(game, depth - 1, alpha, beta, false);
                game.undoMove();
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) break;
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (Move move : legalMoves) {
                game.makeMove(move);
                int eval = minimax(game, depth - 1, alpha, beta, true);
                game.undoMove();
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) break;
            }
            return minEval;
        }
    }
}
