package org.imdb.request;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class RequestsHolder {
  private static final List<Request> requests = new CopyOnWriteArrayList<>();

  private RequestsHolder() {
    throw new UnsupportedOperationException("Cannot instantiate utility class.");
  }

  public static List<Request> getRequests() {
    return Collections.unmodifiableList(requests);
  }

  public static void addRequest(Request request) {
    if (request == null) {
      throw new IllegalArgumentException("Request cannot be null.");
    }

    requests.add(request);
  }

  public static void removeRequest(Request request) {
    requests.remove(request);
  }
}
