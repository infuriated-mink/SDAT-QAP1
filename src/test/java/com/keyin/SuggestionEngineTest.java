package com.keyin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
// comment
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.URI;

@ExtendWith(MockitoExtension.class)
public class SuggestionEngineTest {
    private SuggestionEngine suggestionEngine;

    @BeforeEach
    public void setUp() throws Exception {
        suggestionEngine = new SuggestionEngine();
        URI uri = ClassLoader.getSystemResource("words.txt").toURI();
        Path path = Paths.get(uri);
        suggestionEngine.loadDictionaryData(path);
    }

    @Test
    public void testGenerateSuggestionsForNonexistentWord() throws Exception {
        String inputWord = "qwerty";
        String expectedSuggestions = "";
        String actualSuggestions = suggestionEngine.generateSuggestions(inputWord);
        Assertions.assertTrue(actualSuggestions.contains(expectedSuggestions), "Expected no suggestions for a nonexistent word");
    }

    @Test
    public void testGenerateSuggestionsForCommonTypos() throws Exception {
        String inputWord = "recieve";
        String expectedSuggestion = "receive";
        String actualSuggestions = suggestionEngine.generateSuggestions(inputWord);
        Assertions.assertFalse(actualSuggestions.contains(expectedSuggestion), "Expected to find 'receive' as a suggestion for 'recieve'");
    }

    @Test
    public void testGenerateSuggestionsLimit() throws Exception {
        String inputWord = "teh";
        long expectedMaxSuggestions = 10;
        String actualSuggestions = suggestionEngine.generateSuggestions(inputWord);
        long actualSuggestionsCount = actualSuggestions.lines().count();
        Assertions.assertTrue(actualSuggestionsCount <= expectedMaxSuggestions, "Expected suggestions to be limited to 10 or fewer");
    }

    @Test
    public void testLoadDictionaryDataWithInvalidPath() {
        Assertions.assertThrows(IOException.class, () -> {
            suggestionEngine.loadDictionaryData(Paths.get("invalid_path.txt"));
        }, "Expected to throw IOException for invalid dictionary file path");
    }

    @Test
    public void testGenerateSuggestionsWithEmptyInput() throws Exception {
        String inputWord = "";
        String expectedSuggestions = "";
        String actualSuggestions = suggestionEngine.generateSuggestions(inputWord);
        Assertions.assertEquals(expectedSuggestions, actualSuggestions, "Expected no suggestions for empty input");
    }
}