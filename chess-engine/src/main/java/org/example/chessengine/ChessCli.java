package org.example.chessengine;

import org.example.chessengine.engine.Evaluator;
import org.example.chessengine.engine.Search;
import org.example.chessengine.move.Move;
import org.example.chessengine.move.MoveGenerator;
import org.example.chessengine.pieces.Color;
import org.example.chessengine.state.Game;

import java.util.List;
import java.util.Scanner;

public class ChessCli {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Game game = new Game("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        Evaluator evaluator = new Evaluator();
        Search engine = new Search(evaluator);
        MoveGenerator moveGenerator = new MoveGenerator();

        System.out.println("Welcome to Chess CLI!");
        System.out.println("Enter moves in UCI format (e.g. e2e4, g1f3)");
        System.out.println("Type 'exit' to quit.\n");

        while (true) {
            // Print board
            game.getBoard().print();

            if (game.getTurn() == Color.WHITE) {
                // Human (White)
                System.out.print("\nYour move: ");
                String input = scanner.nextLine().trim().toLowerCase();

                if (input.equals("exit") || input.equals("quit")) {
                    System.out.println("Exiting game.");
                    break;
                }

                Move userMove = parseUciMove(input, game, moveGenerator);
                if (userMove == null) {
                    System.out.println("Invalid move. Try again.");
                    continue;
                }

                game.makeMove(userMove);
            } else {
                // Engine (Black)
                System.out.println("\nEngine thinking...");
                Move engineMove = engine.findBestMove(game, 5);
                if (engineMove == null) {
                    System.out.println("Engine has no legal moves. Game over.");
                    break;
                }
                System.out.println("Engine plays: " + engineMove.toUCI());
                game.makeMove(engineMove);
            }

            if (isGameOver(game, moveGenerator)) {
                game.getBoard().print();
                System.out.println("Game over.");
                break;
            }
        }
    }

    private static boolean isGameOver(Game game, MoveGenerator moveGenerator) {
        List<Move> legalMoves = moveGenerator.generateLegalMoves(game);
        return legalMoves.isEmpty();
    }

    private static Move parseUciMove(String input, Game game, MoveGenerator moveGenerator) {
        List<Move> legalMoves = moveGenerator.generateLegalMoves(game);
        for (Move move : legalMoves) {
            if (move.toUCI().equals(input)) {
                return move;
            }
        }
        return null;
    }
}


