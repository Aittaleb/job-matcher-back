package com.recherche.offre.utils;

public class TestUtils {

    public static String readFile(final String filePath) {
        try (final var inputStream = TestUtils.class.getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("File not found: " + filePath);
            }
            return new String(inputStream.readAllBytes());
        } catch (final Exception e) {
            throw new RuntimeException("Failed to read file: " + filePath, e);
        }
    }
}
