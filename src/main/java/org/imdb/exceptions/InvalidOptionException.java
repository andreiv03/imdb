package org.imdb.exceptions;

/**
 * Exception thrown when an invalid option is selected.
 */
public class InvalidOptionException extends RuntimeException {
  public InvalidOptionException(String message) {
    super(message);
  }
}
