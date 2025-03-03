package org.imdb.production;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.imdb.enumerations.Genre;
import org.imdb.interfaces.Observer;
import org.imdb.interfaces.Subject;
import org.imdb.user.User;
import org.imdb.utils.Database;

import java.util.ArrayList;
import java.util.List;

// @formatter:off
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes({
		@JsonSubTypes.Type(value = Movie.class, name = "Movie"),
		@JsonSubTypes.Type(value = Series.class, name = "Series")
})
// @formatter:on
@Getter
@Setter
public abstract class Production implements Comparable<Production>, Subject<User> {
  @JsonIgnore
  private final List<User> observers = new ArrayList<>();
  protected String title;
  protected String type;
  protected List<String> directors;
  protected List<String> actors;
  protected List<Genre> genres;
  protected List<Rating> ratings;
  protected String plot;
  protected double averageRating;
  @JsonIgnore
  protected String responsible;

  public Production() {
    this(null, null, null, 0);
  }

  public Production(String title, String type, String plot, double averageRating) {
    this.title = title;
    this.type = type;
    this.directors = new ArrayList<>();
    this.actors = new ArrayList<>();
    this.genres = new ArrayList<>();
    this.ratings = new ArrayList<>();
    this.plot = plot;
    this.averageRating = averageRating;
    this.responsible = "ADMIN";

    initializeObservers();
  }

  private void initializeObservers() {
    for (Rating rating : ratings) {
      rating.setProductionTitle(title);

      for (User user : Database.getInstance().getUsers()) {
        if (user.getUsername().equals(rating.getUsername())) {
          registerObserver(user);
        }
      }
    }
  }

  @Override
  public int compareTo(@NonNull Production other) {
    return title.compareToIgnoreCase(other.title);
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
    String message = String.format("The production \"%s\" has just received a new review.", title);

    for (Observer observer : observers) {
      observer.receiveNotification(message);
    }
  }
}
