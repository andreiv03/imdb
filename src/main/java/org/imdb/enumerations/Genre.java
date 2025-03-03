package org.imdb.enumerations;

import java.util.EnumSet;
import java.util.Set;

public enum Genre {
  Action, Adventure, Biography, Comedy, Cooking, Crime, Drama, Fantasy, Horror, Mystery, Romance,
	SF, Thriller, War;

  private static final Set<String> GENRE_NAMES = EnumSet.allOf(Genre.class)
    .stream()
    .map(Enum::name)
    .collect(java.util.stream.Collectors.toSet());

  public static boolean contains(String value) {
    return value != null && GENRE_NAMES.contains(value.toUpperCase());
  }
}
