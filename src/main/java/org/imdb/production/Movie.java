package org.imdb.production;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;
import lombok.Setter;
import org.imdb.utils.JsonUtils;

@JsonTypeName("Movie")
@Getter
@Setter
public class Movie extends Production {
  private String duration;
  private int releaseYear;

  public Movie() {
    this(null, null, 0, null, 0);
  }

  public Movie(String title, String plot, double averageRating, String duration, int releaseYear) {
    super(title, "Movie", plot, averageRating);
    this.duration = duration;
    this.releaseYear = releaseYear;
  }

  @Override
  public String toString() {
    return JsonUtils.toJson(this);
  }
}
