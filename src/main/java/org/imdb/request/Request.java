package org.imdb.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.imdb.enumerations.RequestStatus;
import org.imdb.enumerations.RequestType;
import org.imdb.interfaces.Subject;
import org.imdb.user.User;
import org.imdb.utils.Generator;
import org.imdb.utils.JsonUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Getter
@Setter
public class Request implements Subject<User> {
  @JsonIgnore
  private final ArrayList<User> observers = new ArrayList<>();
  private final String id;
  private RequestType type;
  private LocalDateTime createdDate;
  private String username;
  private String actorName;
  private String movieTitle;
  private String to;
  private String description;
  private RequestStatus status;

  public Request() {
    this(null, null, null, null, null, null, null);
  }

  public Request(RequestType type, LocalDateTime createdDate, String username, String actorName,
    String movieTitle, String to, String description) {
    this.id = Generator.generateId();
    this.type = type;
    this.createdDate = createdDate;
    this.username = username;
    this.actorName = actorName;
    this.movieTitle = movieTitle;
    this.to = to;
    this.description = description;
    this.status = RequestStatus.PENDING;
  }

  @Override
  public String toString() {
    return JsonUtils.toJson(this);
  }

  @Override
  public void registerObserver(User observer) {
    if (observer == null || observers.contains(observer)) {
      throw new IllegalArgumentException("Observer is null or already registered.");
    }

    observers.add(observer);
  }

  @Override
  public void removeObserver(User observer) {
    observers.remove(observer);
  }

  @Override
  public void notifyObservers() {
    String message = String.format("Your request (id: %s) has been %s.", id,
      status == RequestStatus.APPROVED ? "approved" : "rejected");

    for (User user : observers) {
      user.receiveNotification(message);
    }
  }
}
