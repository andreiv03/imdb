package org.imdb.interfaces;

/**
 * Represents a subject in the Observer pattern. Classes implementing this interface allow observers
 * to register and receive notifications.
 *
 * @param <T> The type of observer that can subscribe.
 */
public interface Subject<T extends Observer> {
  /**
   * Registers an observer to receive notifications.
   *
   * @param observer The observer to register.
   * @throws IllegalArgumentException if the observer is null.
   */
  void registerObserver(T observer);

  /**
   * Removes a registered observer.
   *
   * @param observer The observer to remove.
   */
  void removeObserver(T observer);

  void notifyObservers();
}
