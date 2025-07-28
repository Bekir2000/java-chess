package org.example.chessengine.move;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.chessengine.state.Game;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class MoveGeneratorFromJsonTest {

    static class FenTestCase {
        public String fen;
        public List<String> expectedMoves;
        public String category;

        @Override
        public String toString() {
            return "[" + category + "] " + fen;
        }
    }

    static Stream<FenTestCase> provideFenTestCases() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = MoveGeneratorFromJsonTest.class.getResourceAsStream("/fen_move_tests.json");
        if (is == null) {
            throw new IllegalStateException("Could not find 'fen_move_tests.json' in resources.");
        }
        List<FenTestCase> testCases = mapper.readValue(is, new TypeReference<>() {});
        return testCases.stream();
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("provideFenTestCases")
    void testLegalMovesFromFen(FenTestCase testCase) {
        Game game = new Game();
        game.loadFromFEN(testCase.fen);

        MoveGenerator generator = new MoveGenerator();
        List<String> actual = generator.generateLegalMoves(game).stream()
                .map(Move::toUCI)
                .sorted()
                .toList();

        List<String> expected = testCase.expectedMoves.stream()
                .sorted()
                .toList();


        assertAll(
                () -> assertEquals(expected.size(), actual.size(),
                        "Move count mismatch for FEN: " + testCase.fen + " (" + testCase.category + ")"),
                () -> assertIterableEquals(
                        expected,
                        actual,
                        "Move content mismatch for FEN: " + testCase.fen + " (" + testCase.category + ")")
        );

    }
}




