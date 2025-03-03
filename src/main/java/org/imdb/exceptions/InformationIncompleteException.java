package org.imdb.exceptions;

/**
 * Exception thrown when required information is missing or incomplete.
 */
public class InformationIncompleteException extends RuntimeException {
  public InformationIncompleteException(String message) {
    super(message);
  }
}
