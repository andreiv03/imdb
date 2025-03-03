package org.imdb.exceptions;

/**
 * Exception thrown when an invalid command is entered.
 */
public class InvalidCommandException extends RuntimeException {
  public InvalidCommandException(String message) {
    super(message);
  }
}
