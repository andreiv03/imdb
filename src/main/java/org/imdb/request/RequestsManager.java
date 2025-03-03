package org.imdb.request;

public interface RequestsManager {
  /**
   * Creates and stores a new request.
   *
   * @param request The request to create.
   * @throws IllegalArgumentException if the request is null.
   */
  void createRequest(Request request);

  /**
   * Removes a request from the system.
   *
   * @param request The request to remove.
   * @throws IllegalArgumentException if the request is null or not found.
   */
  void removeRequest(Request request);
}
