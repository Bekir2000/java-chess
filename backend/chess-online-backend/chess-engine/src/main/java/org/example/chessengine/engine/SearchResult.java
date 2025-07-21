package org.example.chessengine.engine;

import org.example.chessengine.move.Move;

public record SearchResult(Move bestMove, int score, int depth) {}

