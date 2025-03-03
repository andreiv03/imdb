package org.imdb.utils;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Generator {
  private static final SecureRandom RANDOM = new SecureRandom();
  private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
  private static final String NUMBERS = "0123456789";
  private static final String SPECIALS = "!@#$%^&*";
  private static final String ALL_CHARACTERS = UPPERCASE + LOWERCASE + NUMBERS + SPECIALS;

  private Generator() {
    throw new UnsupportedOperationException("Cannot instantiate utility class.");
  }

  public static String generateUsername(String firstName, String lastName) {
    return String.format("%s_%s_%d", firstName.toLowerCase(), lastName.toLowerCase(),
      RANDOM.nextInt(1_000));
  }

  public static String generateEmail(String firstName, String lastName) {
    return String.format("%s.%s@example.com", firstName.toLowerCase(), lastName.toLowerCase());
  }

  public static String generateStrongPassword(int length) {
    if (length < 4) {
      throw new IllegalArgumentException("Password length must be at least 4 characters.");
    }

    List<Character> characters =
      new ArrayList<>(List.of(UPPERCASE.charAt(RANDOM.nextInt(UPPERCASE.length())),
        LOWERCASE.charAt(RANDOM.nextInt(LOWERCASE.length())),
        NUMBERS.charAt(RANDOM.nextInt(NUMBERS.length())),
        SPECIALS.charAt(RANDOM.nextInt(SPECIALS.length()))));

    IntStream.range(4, length)
      .mapToObj(index -> ALL_CHARACTERS.charAt(RANDOM.nextInt(ALL_CHARACTERS.length())))
      .forEach(characters::add);

    Collections.shuffle(characters);
    return characters.stream().map(String::valueOf).collect(Collectors.joining());
  }

  public static String generateId() {
    return String.valueOf(RANDOM.nextInt(1_000_000));
  }
}
