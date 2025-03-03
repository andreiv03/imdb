package org.imdb.production;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.imdb.user.User;
import org.imdb.utils.Database;
import org.imdb.utils.JsonUtils;

@Getter
@Setter
public class Rating implements Comparable<Rating> {
  private String username;
  private int rating;
  private String comment;
  private String productionTitle;

  public Rating() {
    this(null, 0, null);
  }

  public Rating(String username, int rating, String comment) {
    this.username = username;
    this.rating = rating;
    this.comment = comment;
    this.productionTitle = null;
  }

  @Override
  public String toString() {
    return JsonUtils.toJson(this);
  }

  @Override
  public int compareTo(@NonNull Rating other) {
    Database database = Database.getInstance();

    User u1 = database.getUsers()
      .stream()
      .filter(user -> user.getUsername().equals(username))
      .findFirst()
      .orElse(null);

    User u2 = database.getUsers()
      .stream()
      .filter(user -> user.getUsername().equals(other.getUsername()))
      .findFirst()
      .orElse(null);

    if (u1 == null || u2 == null) {
      return 0;
    }

    return Integer.compare(Integer.parseInt(u2.getExperience()),
      Integer.parseInt(u1.getExperience()));
  }
}
