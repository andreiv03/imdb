package org.imdb.interfaces;

/**
 * Represents an observer in the Observer pattern. Classes implementing this interface can receive
 * notifications.
 */
public interface Observer {
  /**
   * Receives a notification from a Subject.
   *
   * @param notification The message being sent.
   * @throws IllegalArgumentException if the notification is null or empty.
   */
  void receiveNotification(String notification);
}
