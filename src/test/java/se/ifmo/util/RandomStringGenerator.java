package se.ifmo.util;

import java.util.Random;

public class RandomStringGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random RANDOM = new Random();
    
    public static void main(String[] args) {
        System.out.println(generateString());
    }

    // generates non-empty string
    public static String generateString() {
        int length = RANDOM.nextInt(31) + 1; // 1 to 30. its the length of the result string
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            result.append(CHARACTERS.charAt(index));
        }
        return result.toString();
    }
    public static int generate(int min, int max) {
        return min + (int)(Math.random() * ((max - min) + 1));
    }
}
